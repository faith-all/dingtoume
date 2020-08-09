package me.dingtou.service.impl;

import me.dingtou.constant.FundGroupExtraEnum;
import me.dingtou.domain.*;
import me.dingtou.exception.BizException;
import me.dingtou.mapper.FundDailyPriceMapper;
import me.dingtou.mapper.FundGroupDetailMapper;
import me.dingtou.mapper.TradeCalendarMapper;
import me.dingtou.mapper.UserFundGroupMapper;
import me.dingtou.service.FundGroupService;
import me.dingtou.service.FundPullService;
import me.dingtou.util.FundGroupExtraUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by qiyan on 2017/6/18.
 */
@Service
public class FundGroupServiceImpl implements FundGroupService {

    /**
     * AVERAGE_STRATEGY 均线策略 key:均线 value:低于均线后金额上浮乘数
     */
    public static final Map<Integer, Double> AVERAGE_STRATEGY = new ConcurrentHashMap<>(4, 1.0f);

    static {
        // 低于30日均线 目标增量1.5倍
        AVERAGE_STRATEGY.put(30, 1.5);
        // 低于60日均线 目标增量2倍
        AVERAGE_STRATEGY.put(60, 2.0);
        // 低于120日均线 目标增量2.5倍
        AVERAGE_STRATEGY.put(120, 2.5);
    }

    @Autowired
    private UserFundGroupMapper userFundGroupMapper;

    @Autowired
    private FundGroupDetailMapper fundGroupDetailMapper;

    @Autowired
    private TradeCalendarMapper tradeCalendarMapper;

    @Autowired
    private FundDailyPriceMapper fundDailyPriceMapper;

    @Autowired
    private FundPullService fundPullService;

    @Override
    public List<UserFundGroup> queryAllUserFundGroup() {
        UserFundGroupExample query = new UserFundGroupExample();
        query.createCriteria().andStatusEqualTo(1);
        return userFundGroupMapper.selectByExample(query);
    }

    @Override
    public List<UserFundGroup> queryFundGroupByUserId(long userId) {
        UserFundGroupExample query = new UserFundGroupExample();
        query.createCriteria()
                .andUserIdEqualTo(userId)
                .andStatusEqualTo(1);
        return userFundGroupMapper.selectByExample(query);
    }

    @Override
    public UserFundGroup queryFundGroup(long fundGroupId) {
        return userFundGroupMapper.selectByPrimaryKey((int) fundGroupId);
    }

    @Override
    public List<FundGroupDetail> queryFundGroupDetails(long fundGroupId) {
        FundGroupDetailExample query = new FundGroupDetailExample();
        query.createCriteria()
                .andFundGroupIdEqualTo((int) fundGroupId);
        return fundGroupDetailMapper.selectByExample(query);
    }

    @Override
    public void buildFundGroupTradeCalendar(long fundGroupId, Date nearbyDate) throws BizException {
        UserFundGroup fundGroup = queryFundGroup(fundGroupId);
        if (null == fundGroup) {
            return;
        }
        // 支持不同策略需要修改交易日历生成规则
        // 目前代码支持价值策略以及价格策略
        try {
            //默认使用任务开始时间
            Date next = fundGroup.getStartDate();
            int targetPrice = 0;
            int increment = fundGroup.getIncrement();

            CronExpression cron = new CronExpression(fundGroup.getCron());

            //查询最后生成的任务
            TradeCalendarExample lastQuery = new TradeCalendarExample();
            lastQuery.createCriteria()
                    .andFundGroupIdEqualTo(fundGroup.getFundGroupId())
                    .andTradeDateLessThanOrEqualTo(nearbyDate);
            lastQuery.setOrderByClause("trade_date desc limit 1");
            List<TradeCalendar> lastList = tradeCalendarMapper.selectByExample(lastQuery);
            if (null != lastList && !lastList.isEmpty()) {
                TradeCalendar last = lastList.get(0);
                next = last.getTradeDate();
                targetPrice = last.getTargetPrice();
            }

            while (next.before(nearbyDate)) {
                // 只生成最近交易任务的目标金额
                targetPrice = calculateTargetPrice(fundGroup, targetPrice, increment);
                next = cron.getNextValidTimeAfter(next);

                TradeCalendarExample query = new TradeCalendarExample();
                query.createCriteria().andFundGroupIdEqualTo(fundGroup.getFundGroupId()).andTradeDateEqualTo(next);
                List<TradeCalendar> dbObjs = tradeCalendarMapper.selectByExample(query);
                if (null == dbObjs || dbObjs.isEmpty()) {
                    TradeCalendar tradeCalendar = new TradeCalendar();
                    tradeCalendar.setFundGroupId(fundGroup.getFundGroupId());
                    tradeCalendar.setTradeStatus(0);
                    tradeCalendar.setTradeDate(next);
                    tradeCalendar.setTargetPrice(targetPrice);
                    tradeCalendarMapper.insert(tradeCalendar);
                } else {
                    // 目标金额发生变化时更新目标（）
                    TradeCalendar dbObj = dbObjs.get(0);
                    if (null != dbObj && dbObj.getTargetPrice().intValue() < targetPrice) {
                        dbObj.setTargetPrice(targetPrice);
                        tradeCalendarMapper.updateByPrimaryKey(dbObj);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BizException("表达式异常:" + fundGroup.getCron(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("生成交易日历异常fundGroupId：" + fundGroupId, e);
        }
    }

    /**
     * 新增根据均线浮动目标金额功能
     */
    private int calculateTargetPrice(UserFundGroup fundGroup, int lastTargetPrice, int increment) {
        int targetPrice;

        List<FundGroupDetail> groupDetails = queryFundGroupDetails(fundGroup.getFundGroupId());
        if (null == groupDetails || groupDetails.isEmpty()) {
            return lastTargetPrice + increment;
        }
        groupDetails.sort((o1, o2) -> o2.getOrderRate() - o1.getOrderRate());
        FundDailyPriceExample query = new FundDailyPriceExample();
        query.setOrderByClause("price_date desc limit 120");
        String fundCode = groupDetails.get(0).getFundCode();
        query.createCriteria().andFundCodeEqualTo(fundCode);
        List<FundDailyPrice> prices = fundDailyPriceMapper.selectByExample(query);
        if (null != prices && !prices.isEmpty()) {
            // 计算均线平均价格
            int strategySize = AVERAGE_STRATEGY.size();
            final Map<Integer, BigDecimal> average = new HashMap<>(strategySize);
            for (Iterator<Integer> i = AVERAGE_STRATEGY.keySet().iterator(); i.hasNext(); ) {
                Integer averageVal = i.next();
                average.put(averageVal, new BigDecimal("0"));
                if (prices.size() >= averageVal) {
                    prices.stream().limit(averageVal).forEach(e -> average.put(averageVal, average.get(averageVal).add(new BigDecimal(e.getUnitPrice()))));
                    average.put(averageVal, average.get(averageVal).divide(BigDecimal.valueOf(averageVal), 4, ROUND_HALF_DOWN));
                }
            }

            // 拉取比较金额 优先使用现价，拉不到就使用前一交易日价格
            BigDecimal baseIncrement = new BigDecimal(increment);
            BigDecimal finalAdjustIncrement = new BigDecimal(increment);
            Double basePrice = null;
            try {
                basePrice = fundPullService.pullNowFundPrice(fundCode);
            } catch (Exception e) {
                basePrice = Double.parseDouble(prices.get(0).getUnitPrice());
            }

            // 优先使用更大购买金额
            for (Iterator<Integer> i = average.keySet().iterator(); i.hasNext(); ) {
                Integer averageVal = i.next();
                BigDecimal linePrice = average.get(averageVal);
                if (basePrice >= linePrice.doubleValue()) {
                    continue;
                }
                Double multiplyVal = AVERAGE_STRATEGY.get(averageVal);
                BigDecimal newAdjustIncrement = baseIncrement.multiply(BigDecimal.valueOf(multiplyVal));
                if (newAdjustIncrement.compareTo(finalAdjustIncrement) > 0) {
                    finalAdjustIncrement = newAdjustIncrement;
                }
            }
            targetPrice = lastTargetPrice + finalAdjustIncrement.intValue();
        } else {
            targetPrice = lastTargetPrice + increment;
        }

        int maxPrice = FundGroupExtraUtils.getMaxPrice(fundGroup.getExtra());
        if (maxPrice >= 0 && targetPrice > maxPrice) {
            targetPrice = maxPrice;
        }
        return targetPrice;
    }
}
