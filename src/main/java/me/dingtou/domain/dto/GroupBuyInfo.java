package me.dingtou.domain.dto;

import me.dingtou.domain.TradeCalendar;
import me.dingtou.domain.UserFundGroup;
import lombok.Data;

import java.util.List;

/**
 * Created by qiyan on 2017/7/6.
 */
@Data
public class GroupBuyInfo {

    /**
     * 组合成本
     */
    private double groupCostPrice;

    /**
     * 组合持有价值
     */
    private double groupHoldPrice;

    /**
     * 涨幅
     */
    private double amountOfIncrease;
    
    private UserFundGroup userFundGroup;

    private TradeCalendar tradeCalendar;

    private List<FundBuyInfo> fundBuyInfoList;

}
