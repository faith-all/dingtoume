package me.dingtou.service;

import me.dingtou.domain.FundInfo;
import me.dingtou.domain.FundDailyPrice;
import me.dingtou.exception.BizException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by qiyan on 2017/6/18.
 */
public interface FundPullService {

    /**
     * 拉取基金信息
     *
     * @param fundCode 基金编码
     * @return
     */
    FundInfo pullFundInfo(String fundCode) throws BizException;

    /**
     * 拉取基金价格
     *
     * @param fundCode   基金编码
     * @param isFullMode 是否全量模式
     * @return
     */
    List<FundDailyPrice> pullFundPrice(String fundCode, boolean isFullMode) throws BizException;

    /**
     * 拉取实时基金价格
     * 
     * @param fundCode
     * @return
     */
    double pullNowFundPrice(String fundCode) throws ExecutionException, BizException;


}
