package me.dingtou.service;

import me.dingtou.domain.FundInfo;
import me.dingtou.domain.FundDailyPrice;
import me.dingtou.exception.BizException;

import java.util.List;

/**
 * Created by qiyan on 2017/6/18.
 */
public interface FundBaseService {

    /**
     * 添加基金信息
     *
     * @param fundCode 基金编码
     */
    void addFundInfo(String fundCode) throws BizException;

    /**
     * 同步基金价格
     *
     * @param fundCode   基金编码
     * @param isFullMode 是否使用全量模式
     */
    void syncFundPrice(String fundCode, boolean isFullMode) throws BizException;

    /**
     * 查询基金信息
     *
     * @param fundCode 基金编码
     * @return
     */
    FundInfo queryFundInfo(String fundCode) throws BizException;

    /**
     * 查询所有基金列表
     *
     * @return
     */
    List<FundInfo> queryAllFundInfo() throws BizException;

    /**
     * 查询历史基金价格信息
     *
     * @param fundCode 基金编码
     * @return
     */
    List<FundDailyPrice> queryFundPrice(String fundCode) throws BizException;


}
