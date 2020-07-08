package me.dingtou.service;

import me.dingtou.domain.FundOrder;
import me.dingtou.domain.TradeCalendar;
import me.dingtou.domain.dto.FundBuyInfo;
import me.dingtou.domain.dto.GroupBuyInfo;
import me.dingtou.exception.BizException;

import java.util.Date;
import java.util.List;

/**
 * Created by qiyan on 2017/6/18.
 */
public interface FundTradeService {

    /**
     * 查询待处理订单
     *
     * @return
     * @throws BizException
     */
    List<FundOrder> queryAllWaitProcessFundOrder() throws BizException;

    /**
     * 处理订单
     *
     * @param orderId
     * @return
     * @throws BizException
     */
    FundOrder processFundOrder(long orderId) throws BizException;

    /**
     * 根据用户和基金编码查找订单
     *
     * @param userId 用户ID
     * @param fundCode 基金编码
     * @return
     * @throws BizException
     */
    List<FundOrder> getUserFundOrderByFundCode(long userId, String fundCode) throws BizException;

    /**
     * 计算基金购买信息
     *
     * @param userId
     * @param fundGroupId
     * @return
     * @throws BizException
     */
    GroupBuyInfo calculateFundOrder(long userId, long fundGroupId) throws BizException;

    /**
     * 创建订单
     *
     * @param userId
     * @param createTime
     * @param buyInfos
     * @return
     * @throws BizException
     */
    List<FundOrder> createFundOrders(long userId, Date createTime, List<FundBuyInfo> buyInfos) throws BizException;

    /**
     * 查询交易信息
     *
     * @param fundGroupId 基金组合ID
     * @param tradeDate 交易日期
     * @return
     * @throws BizException
     */
    TradeCalendar queryTradeCalendar(long fundGroupId, Date tradeDate) throws BizException;

    TradeCalendar queryPreTradeCalendar(long fundGroupId, Date tradeDate);
}
