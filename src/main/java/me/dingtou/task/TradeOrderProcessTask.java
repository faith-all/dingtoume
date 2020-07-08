package me.dingtou.task;

import me.dingtou.domain.FundOrder;
import me.dingtou.exception.BizException;
import me.dingtou.service.FundTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by qiyan on 2017/6/18.
 */
@Component
public class TradeOrderProcessTask implements Callable {

    @Autowired
    private FundTradeService fundTradeService;

    @Override
    public Object call() throws Exception {
        //1.查询所有待处理订单
        List<FundOrder> allOrders = fundTradeService.queryAllWaitProcessFundOrder();
        if (null == allOrders) {
            return Boolean.TRUE;
        }

        allOrders.stream().forEach(order -> {
            try {
                //2.更新订单
                fundTradeService.processFundOrder(order.getOrderId());
            } catch (BizException e) {
                e.printStackTrace();
            }
        });

        
        return Boolean.TRUE;
    }
}
