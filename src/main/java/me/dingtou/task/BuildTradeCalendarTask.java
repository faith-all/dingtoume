package me.dingtou.task;

import me.dingtou.domain.UserFundGroup;
import me.dingtou.exception.BizException;
import me.dingtou.service.FundGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by qiyan on 2017/6/18.
 */
@Component
public class BuildTradeCalendarTask implements Callable {

    @Autowired
    private FundGroupService fundGroupService;

    @Override
    public Object call() throws Exception {
        //1.查询所有基金分组
        List<UserFundGroup> allGroups = fundGroupService.queryAllUserFundGroup();
        if (null == allGroups) {
            return Boolean.TRUE;
        }

        allGroups.stream().forEach(group -> {
            try {
                //2.生成最近的交易计划
                fundGroupService.buildFundGroupTradeCalendar(group.getFundGroupId(), new Date());
            } catch (BizException e) {
                e.printStackTrace();
            }
        });

        
        return Boolean.TRUE;
    }
}
