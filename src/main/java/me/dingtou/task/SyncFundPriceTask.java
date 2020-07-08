package me.dingtou.task;

import me.dingtou.domain.FundInfo;
import me.dingtou.exception.BizException;
import me.dingtou.service.FundBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by qiyan on 2017/6/18.
 */
@Component
public class SyncFundPriceTask implements Callable {

    @Autowired
    private FundBaseService fundBaseService;

    @Override
    public Object call() throws Exception {
        //1.查询所有需要同步的基金
        List<FundInfo> fundInfo = fundBaseService.queryAllFundInfo();
        if (null == fundInfo) {
            return Boolean.TRUE;
        }

        fundInfo.stream().forEach(fund -> {
            try {
                //2.进行同步
                fundBaseService.syncFundPrice(fund.getFundCode(), false);
            } catch (BizException e) {
                e.printStackTrace();
            }
        });

        return Boolean.TRUE;
    }
}
