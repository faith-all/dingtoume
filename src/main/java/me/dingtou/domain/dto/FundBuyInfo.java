package me.dingtou.domain.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiyan on 2017/6/18.
 */
@Data
public class FundBuyInfo {

    /**
     * 基金编码
     */
    private String fundCode;

    /**
     * 组合目标价值
     */
    private double groupTargetValue;

    /**
     * 上一期组合目标价值
     */
    private double preGroupTargetValue;

    /**
     * 当前单位价格(元)
     */
    private double nowUnitPrice;

    /**
     * 当前持有份额
     */
    private double holdShare;

    /**
     * 当前持有价格(元)
     */
    private double holdPrice;

    /**
     * 当前持有成本价(元)
     */
    private double costPrice;

    /**
     * 买入单未确认金额(元)
     */
    private double unconfirmedPrice;

    /**
     * 买入金额(元)
     */
    private double buyPrice;

    /**
     * 买入份额
     */
    private double buyShare;

    /**
     * 购买比例
     */
    private double orderRate;

    /**
     * 购买费用（元）
     */
    private double buyFee;

    /**
     * 涨幅
     */
    private double amountOfIncrease;

    public Map<String, Object> buildBuyParam() {
        Map<String, Object> buyParam = new HashMap<String, Object>();
        buyParam.put("fundCode", this.fundCode);
        buyParam.put("buyPrice", this.buyPrice);
        buyParam.put("buyShare", this.buyShare);
        return buyParam;
    }
}
