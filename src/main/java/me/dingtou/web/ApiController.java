package me.dingtou.web;

import me.dingtou.domain.UserFundGroup;
import me.dingtou.domain.dto.GroupBuyInfo;
import me.dingtou.exception.BizException;
import me.dingtou.service.FundGroupService;
import me.dingtou.service.FundTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author qiyan
 * @date 2017/6/16
 */
@Controller
public class ApiController {

    @Autowired
    private FundGroupService fundGroupService;

    @Autowired
    private FundTradeService fundTradeService;

    @RequestMapping(value = "/api/dingtou", method = RequestMethod.GET)
    public
    @ResponseBody
    List<GroupBuyInfo> apiDingTou(@RequestParam(value = "userId", required = false, defaultValue = "1") String userId)
        throws Exception {

        Long userIdLong = Long.valueOf(userId);
        List<UserFundGroup> fundGroupList = fundGroupService.queryFundGroupByUserId(userIdLong);
        if (null == fundGroupList) {
            return Collections.emptyList();
        }
        List<GroupBuyInfo> allCalculateFundOrders = new ArrayList<GroupBuyInfo>();
        fundGroupList.forEach(fundGroup -> {

            try {
                GroupBuyInfo groupBuyInfo = fundTradeService.calculateFundOrder(userIdLong, fundGroup.getFundGroupId());
                allCalculateFundOrders.add(groupBuyInfo);
            } catch (BizException e) {
                e.printStackTrace();
            }
        });
        return allCalculateFundOrders;
    }

}
