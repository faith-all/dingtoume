package me.dingtou.service.impl;

import me.dingtou.constant.GroupTypeEnum;
import me.dingtou.domain.*;
import me.dingtou.domain.dto.FundBuyInfo;
import me.dingtou.domain.dto.GroupBuyInfo;
import me.dingtou.exception.BizException;
import me.dingtou.mapper.*;
import me.dingtou.service.FundPullService;
import me.dingtou.service.FundTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by qiyan on 2017/6/18.
 */
@Service
public class FundTradeServiceImpl implements FundTradeService {

    @Autowired
    private FundInfoMapper fundInfoMapper;

    @Autowired
    private FundOrderMapper fundOrderMapper;

    @Autowired
    private FundDailyPriceMapper fundDailyPriceMapper;

    @Autowired
    private UserFundGroupMapper userFundGroupMapper;

    @Autowired
    private FundGroupDetailMapper fundGroupDetailMapper;

    @Autowired
    private TradeCalendarMapper tradeCalendarMapper;

    @Autowired
    private FundPullService fundPullService;

    @Override
    public List<FundOrder> queryAllWaitProcessFundOrder() throws BizException {
        FundOrderExample query = new FundOrderExample();
        query.createCriteria().andStatusEqualTo(0);
        return fundOrderMapper.selectByExample(query);
    }

    @Override
    public FundOrder processFundOrder(long orderId) throws BizException {
        FundOrder order = fundOrderMapper.selectByPrimaryKey(orderId);
        if (null == order) {
            return null;
        }

        Date buyTime = order.getBuyTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(buyTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (hour >= 15) {
            calendar.add(Calendar.DATE, 1);
        }

        FundDailyPriceExample queryPrice = new FundDailyPriceExample();
        queryPrice.createCriteria().andFundCodeEqualTo(order.getFundCode()).andPriceDateGreaterThanOrEqualTo(
                calendar.getTime());
        queryPrice.setOrderByClause("price_date asc");
        List<FundDailyPrice> priceList = fundDailyPriceMapper.selectByExample(queryPrice);
        if (null == priceList || priceList.isEmpty()) {
            return null;
        }
        FundDailyPrice fundPrice = priceList.get(0);

        if ("buy".equals(order.getOrderType())) {
            FundInfo fundInfo = fundInfoMapper.selectByPrimaryKey(order.getFundCode());
            Integer price = order.getTradePrice();
            String buyRate = fundInfo.getBuyRate();
            double rate = getBuyRate(buyRate);
            //份额计算四舍五入
            int tradeFee = BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(rate)).setScale(0,
                    BigDecimal.ROUND_HALF_DOWN).intValue();
            order.setTradeFee(tradeFee);
            //减去手续费后的金额
            int buyFundPrice = price - tradeFee;
            double share = BigDecimal.valueOf(buyFundPrice).divide(
                    new BigDecimal(fundPrice.getUnitPrice()).multiply(BigDecimal.valueOf(100)), 2, fundInfo.getRoundingMode())
                    .doubleValue();
            order.setTradeShare(share);
            order.setTradeDate(fundPrice.getPriceDate());
        } else if ("sell".equals(order.getOrderType())) {
            //FIXME 卖出费率待计算
            order.setTradeFee(null);
            order.setTradePrice(BigDecimal.valueOf(order.getTradeShare())
                    .multiply(new BigDecimal(fundPrice.getUnitPrice()))
                    .multiply(BigDecimal.valueOf(100))
                    .intValue());
            order.setTradeDate(fundPrice.getPriceDate());
        }
        //TODO 分红待处理
        order.setStatus(1);
        fundOrderMapper.updateByPrimaryKey(order);
        return order;
    }

    @Override
    public List<FundOrder> getUserFundOrderByFundCode(long userId, String fundCode) throws BizException {
        FundOrderExample query = new FundOrderExample();
        query.createCriteria().andUserIdEqualTo(userId).andFundCodeEqualTo(fundCode);
        return fundOrderMapper.selectByExample(query);
    }

    @Override
    public GroupBuyInfo calculateFundOrder(long userId, long fundGroupId) {
        GroupBuyInfo groupBuyInfo = new GroupBuyInfo();
        List<FundBuyInfo> buyInfoList = new ArrayList<FundBuyInfo>();
        groupBuyInfo.setFundBuyInfoList(buyInfoList);
        FundGroupDetailExample query = new FundGroupDetailExample();
        query.createCriteria().andFundGroupIdEqualTo((int) fundGroupId);
        List<FundGroupDetail> fundGroupDetailList = fundGroupDetailMapper.selectByExample(query);
        UserFundGroup userFundGroup = userFundGroupMapper.selectByPrimaryKey((int) fundGroupId);
        if (null == userFundGroup || null == fundGroupDetailList) {
            return groupBuyInfo;
        }
        groupBuyInfo.setUserFundGroup(userFundGroup);

        Date tradeDate = new Date();
        TradeCalendar tradeCalendar = queryTradeCalendar(fundGroupId, tradeDate);
        if (null == tradeCalendar) {
            return groupBuyInfo;
        }
        groupBuyInfo.setTradeCalendar(tradeCalendar);


        TradeCalendar preTradeCalendar = queryPreTradeCalendar(fundGroupId, tradeDate);


        //总计购买金额
        Integer targetPrice = tradeCalendar.getTargetPrice();

        double needBuyPrice = 0;
        BigDecimal hundred = BigDecimal.valueOf(100);
        if (GroupTypeEnum.VALUE.getType().equals(userFundGroup.getGroupType())) {
            needBuyPrice = BigDecimal.valueOf(targetPrice).add(BigDecimal.valueOf(userFundGroup.getPriceOffset()))
                    .divide(hundred).doubleValue();
        } else if (GroupTypeEnum.PRICE.getType().equals(userFundGroup.getGroupType())) {
            needBuyPrice = BigDecimal.valueOf(userFundGroup.getIncrement()).divide(hundred).doubleValue();
        }

        fundGroupDetailList.forEach(fundGroupDetail -> {
            try {

                FundBuyInfo buyInfo = new FundBuyInfo();
                buyInfo.setFundCode(fundGroupDetail.getFundCode());
                double groupTargetValue = BigDecimal.valueOf(targetPrice).divide(hundred).doubleValue();
                buyInfo.setGroupTargetValue(groupTargetValue);
                if (null != preTradeCalendar) {
                    double preGroupTargetValue = BigDecimal.valueOf(preTradeCalendar.getTargetPrice())
                            .divide(hundred)
                            .doubleValue();
                    buyInfo.setPreGroupTargetValue(preGroupTargetValue);
                }
                List<FundOrder> fundOrders = getUserFundOrderByFundCode(userId, fundGroupDetail.getFundCode());
                if (null == fundOrders) {
                    return;
                }

                fundOrders.forEach(order -> {
                    BigDecimal tradePrice = null;
                    if (null != order.getTradePrice()) {
                        tradePrice = BigDecimal.valueOf(order.getTradePrice()).divide(hundred);
                    } else {
                        tradePrice = BigDecimal.ZERO;
                    }
                    if (Integer.valueOf(1).equals(order.getStatus())) {
                        //统计持有份额
                        buyInfo.setHoldShare(BigDecimal.valueOf(buyInfo.getHoldShare())
                                .add(BigDecimal.valueOf(order.getTradeShare())).doubleValue());

                        //成本计算
                        buyInfo.setCostPrice(
                                BigDecimal.valueOf(buyInfo.getCostPrice())
                                        .add(tradePrice)
                                        .doubleValue());
                    } else if (Integer.valueOf(0).equals(order.getStatus())) {
                        //未确认金额
                        buyInfo.setUnconfirmedPrice(
                                BigDecimal.valueOf(buyInfo.getUnconfirmedPrice())
                                        .add(tradePrice)
                                        .doubleValue());
                    }
                });

                //根据购买比例计算购买金额
                double orderRate = BigDecimal.valueOf(fundGroupDetail.getOrderRate()).divide(hundred).doubleValue();
                buyInfo.setOrderRate(orderRate);

                double nowUnitPrice = fundPullService.pullNowFundPrice(fundGroupDetail.getFundCode());
                buyInfo.setNowUnitPrice(nowUnitPrice);

                buyInfoList.add(buyInfo);
            } catch (BizException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        for (FundBuyInfo buyInfo : buyInfoList) {
            //NowUnitPrice * HoldShare
            buyInfo.setHoldPrice(BigDecimal.valueOf(buyInfo.getNowUnitPrice()).multiply(
                    BigDecimal.valueOf(buyInfo.getHoldShare())).setScale(2, ROUND_HALF_DOWN).doubleValue());
            if (GroupTypeEnum.VALUE.getType().equals(userFundGroup.getGroupType())) {
                needBuyPrice = BigDecimal.valueOf(needBuyPrice).subtract(BigDecimal.valueOf(buyInfo.getHoldPrice()))
                        .intValue();
            }
        }

        //组合成本
        double groupCostPrice = 0;
        //组合持有价值
        double groupHoldPrice = 0;

        for (FundBuyInfo buyInfo : buyInfoList) {
            //计算涨幅
            if (buyInfo.getCostPrice() > 0) {
                buyInfo.setAmountOfIncrease(BigDecimal.valueOf(buyInfo.getHoldPrice())
                        .subtract(BigDecimal.valueOf(buyInfo.getCostPrice()))
                        .divide(BigDecimal.valueOf(buyInfo.getCostPrice()), 4, ROUND_HALF_DOWN)
                        .multiply(hundred)
                        .doubleValue());
            } else {
                buyInfo.setAmountOfIncrease(0);
            }

            //累积组合内所有基金成本以及持有金额
            groupCostPrice = BigDecimal.valueOf(groupCostPrice).add(BigDecimal.valueOf(buyInfo.getCostPrice()))
                    .doubleValue();
            groupHoldPrice = BigDecimal.valueOf(groupHoldPrice).add(BigDecimal.valueOf(buyInfo.getHoldPrice()))
                    .doubleValue();

            //不需要买卖直接不计算买卖金额 continue
            if (buyInfo.getOrderRate() == 0) {
                continue;
            }
            double buyPrice = BigDecimal.valueOf(needBuyPrice).multiply(BigDecimal.valueOf(buyInfo.getOrderRate()))
                    .setScale(2, ROUND_HALF_DOWN).doubleValue();

            //Setp1:取整
            buyPrice = Math.ceil(buyPrice);

            //Step2:多买30快 涨跌buffer
            if (GroupTypeEnum.VALUE.getType().equals(userFundGroup.getGroupType())) {
                buyPrice += 30;
            }

            //Step3:节省手续费
            //计算步骤：正常计算手续费->根据手续费取整反算购买金额->加上四舍五入极限值
            FundInfo fundInfo = fundInfoMapper.selectByPrimaryKey(buyInfo.getFundCode());
            double buyFeeRate = getBuyRate(fundInfo.getBuyRate());
            //正常计算手续费
            double buyFee = BigDecimal.valueOf(buyPrice).multiply(new BigDecimal(buyFeeRate)).setScale(2,
                    ROUND_HALF_DOWN).doubleValue();
            //最佳手续费
            double freeBuyFee = 0;
            if (GroupTypeEnum.VALUE.getType().equals(userFundGroup.getGroupType())) {
                freeBuyFee = 0.004;
            }
            BigDecimal bestBuyFee = BigDecimal.valueOf(buyFee).add(BigDecimal.valueOf(freeBuyFee));
            //根据最佳手续费除以费率计算出最佳购买金额
            double suggestBuyPrice = bestBuyFee.divide(BigDecimal.valueOf(buyFeeRate), 0, ROUND_FLOOR).doubleValue();
            buyInfo.setBuyPrice(suggestBuyPrice);
            buyInfo.setBuyFee(BigDecimal.valueOf(buyInfo.getBuyPrice()).multiply(BigDecimal.valueOf(buyFeeRate))
                    .doubleValue());
            buyInfo.setBuyShare(BigDecimal.valueOf(buyInfo.getBuyPrice())
                    .divide(BigDecimal.valueOf(buyInfo.getNowUnitPrice()), 0, ROUND_HALF_DOWN).doubleValue());
        }

        groupBuyInfo.setGroupCostPrice(groupCostPrice);
        groupBuyInfo.setGroupHoldPrice(groupHoldPrice);

        if (groupCostPrice > 0) {
            groupBuyInfo.setAmountOfIncrease(BigDecimal.valueOf(groupHoldPrice)
                    .subtract(BigDecimal.valueOf(groupCostPrice))
                    .divide(BigDecimal.valueOf(groupCostPrice), 4, ROUND_HALF_DOWN)
                    .multiply(hundred)
                    .doubleValue());
        } else {
            groupBuyInfo.setAmountOfIncrease(0);
        }
        return groupBuyInfo;
    }

    @Override
    public List<FundOrder> createFundOrders(long userId, Date createTime, List<FundBuyInfo> buyInfos)
            throws BizException {
        List<FundOrder> orders = new ArrayList<FundOrder>();
        if (null == buyInfos || buyInfos.isEmpty()) {
            return orders;
        }
        buyInfos.stream().filter(fundBuyInfo -> fundBuyInfo.getBuyShare() != 0 || fundBuyInfo.getBuyPrice() != 0)
                .forEach(fundBuyInfo -> {
                    FundOrder order = new FundOrder();
                    order.setStatus(0);
                    order.setFundCode(fundBuyInfo.getFundCode());
                    order.setBuyTime(createTime);
                    order.setUserId(userId);
                    if (fundBuyInfo.getBuyPrice() > 0) {
                        order.setOrderType("buy");
                        order.setTradePrice((int) (fundBuyInfo.getBuyPrice() * 100));
                    } else if (fundBuyInfo.getBuyShare() < 0) {
                        order.setOrderType("sell");
                        order.setTradeShare(fundBuyInfo.getBuyShare());
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    order.setOuterOrderId(order.getOrderType() + "-" + order.getFundCode() + "-" + sdf.format(createTime));
                    FundOrderExample query = new FundOrderExample();
                    query.createCriteria().andUserIdEqualTo(userId).andFundCodeEqualTo(order.getFundCode())
                            .andOuterOrderIdEqualTo(order.getOuterOrderId());

                    long count = fundOrderMapper.countByExample(query);
                    if (count <= 0) {
                        fundOrderMapper.insertSelective(order);
                        orders.add(order);
                    }
                });
        return orders;
    }

    @Override
    public TradeCalendar queryTradeCalendar(long fundGroupId, Date tradeDate) {
        TradeCalendarExample query = new TradeCalendarExample();
        query.createCriteria().andFundGroupIdEqualTo((int) fundGroupId).andTradeDateGreaterThanOrEqualTo(tradeDate);
        query.setOrderByClause("trade_date asc");
        List<TradeCalendar> tradeCalendarList = tradeCalendarMapper.selectByExample(query);
        if (null != tradeCalendarList && !tradeCalendarList.isEmpty()) {
            return tradeCalendarList.get(0);
        }
        //没有交易计划返回null
        return null;
    }

    @Override
    public TradeCalendar queryPreTradeCalendar(long fundGroupId, Date tradeDate) {

        TradeCalendarExample query = new TradeCalendarExample();
        query.createCriteria().andFundGroupIdEqualTo((int) fundGroupId).andTradeDateLessThan(tradeDate);
        query.setOrderByClause("trade_date desc");
        List<TradeCalendar> tradeCalendarList = tradeCalendarMapper.selectByExample(query);
        if (null != tradeCalendarList && !tradeCalendarList.isEmpty()) {
            return tradeCalendarList.get(0);
        }
        //没有交易计划返回null
        return null;
    }

    private double getBuyRate(String buyRate) {
        //费率默认1折
        return new BigDecimal(buyRate.replace("%", "")).divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(10))
                .doubleValue();
    }

}
