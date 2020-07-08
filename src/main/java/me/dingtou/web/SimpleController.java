package me.dingtou.web;

import me.dingtou.domain.UserFundGroup;
import me.dingtou.domain.dto.FundBuyInfo;
import me.dingtou.domain.dto.GroupBuyInfo;
import me.dingtou.exception.BizException;
import me.dingtou.service.FundGroupService;
import me.dingtou.service.FundTradeService;

import me.dingtou.task.BuildTradeCalendarTask;
import me.dingtou.task.SyncFundPriceTask;
import me.dingtou.task.TradeOrderProcessTask;
import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiyan
 * @date 2017/6/16
 */
@Controller
public class SimpleController {

    @Autowired
    private FundGroupService fundGroupService;

    @Autowired
    private FundTradeService fundTradeService;

    @Autowired
    private SyncFundPriceTask syncFundPriceTask;

    @Autowired
    private BuildTradeCalendarTask buildTradeCalendarTask;

    @Autowired
    private TradeOrderProcessTask tradeOrderProcessTask;

    @RequestMapping("/dingtou.jsp")
    public String doDingTou(@RequestParam(value = "userId", required = false, defaultValue = "1") String userId,
                            ModelMap modelMap) throws Exception {

        try {
            syncFundPriceTask.call();
            buildTradeCalendarTask.call();
            tradeOrderProcessTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Long userIdLong = Long.valueOf(userId);
        List<UserFundGroup> fundGroupList = fundGroupService.queryFundGroupByUserId(userIdLong);
        if (null == fundGroupList) {
            return "";
        }
        List<FundBuyInfo> allFundOrder = new ArrayList<FundBuyInfo>();
        fundGroupList.forEach(fundGroup -> {

            try {
                GroupBuyInfo calculateFundOrders = fundTradeService.calculateFundOrder(userIdLong,
                    fundGroup.getFundGroupId());
                allFundOrder.addAll(calculateFundOrders.getFundBuyInfoList());
            } catch (BizException e) {
                e.printStackTrace();
            }
        });

        List<FundBuyInfo> filterAllFundOrder = allFundOrder.stream()
            .filter(e -> e.getBuyPrice() != 0)
            .collect(Collectors.toList());
        List<Map<String, Object>> buyList = new ArrayList<Map<String, Object>>();
        filterAllFundOrder.forEach(fundBuyInfo -> buyList.add(fundBuyInfo.buildBuyParam()));
        String buyParam = new BASE64Encoder().encode(JSON.toJSONString(buyList).getBytes("utf-8"));
        modelMap.put("allFundOrder", filterAllFundOrder);
        modelMap.put("buyParam", buyParam);
        modelMap.put("timestamp", System.currentTimeMillis());
        return "dingtou";
    }

}
