package me.dingtou.service;

import me.dingtou.domain.FundGroupDetail;
import me.dingtou.domain.UserFundGroup;
import me.dingtou.exception.BizException;

import java.util.Date;
import java.util.List;

/**
 * Created by qiyan on 2017/6/18.
 */
public interface FundGroupService {

    /**
     * 查询所有基金组合
     */
    List<UserFundGroup> queryAllUserFundGroup() throws BizException;

    /**
     * 查询用户基金组合
     *
     * @param userId 用户信息
     * @return
     * @throws BizException
     */
    List<UserFundGroup> queryFundGroupByUserId(long userId) throws BizException;

    /**
     * 查询基金组合信息
     *
     * @param fundGroupId 基金组合ID
     * @return
     */
    UserFundGroup queryFundGroup(long fundGroupId) throws BizException;

    /**
     * 查询组合中的基金明细
     *
     * @param fundGroupId 基金组合ID
     * @return
     * @throws BizException
     */
    List<FundGroupDetail> queryFundGroupDetails(long fundGroupId) throws BizException;


    /**
     * 生成基金组合交易日历
     *
     * @param fundGroupId 基金组合ID
     * @param nearbyDate  附近的日期
     */
    void buildFundGroupTradeCalendar(long fundGroupId, Date nearbyDate) throws BizException;

}
