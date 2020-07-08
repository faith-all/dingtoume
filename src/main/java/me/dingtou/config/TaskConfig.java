package me.dingtou.config;

import me.dingtou.task.BuildTradeCalendarTask;
import me.dingtou.task.SyncFundPriceTask;
import me.dingtou.task.TradeOrderProcessTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by qiyan on 2017/6/18.
 */

@Component
@Configurable
@EnableScheduling
public class TaskConfig {

    @Autowired
    private SyncFundPriceTask syncFundPriceTask;
    
    @Autowired
    private BuildTradeCalendarTask buildTradeCalendarTask;
    
    @Autowired
    private TradeOrderProcessTask tradeOrderProcessTask;


    @Scheduled(cron = "* 0/30 * * * *")
    public void syncFundPrice() {
        try {
            //同步基金价格
            syncFundPriceTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Scheduled(cron = "* 0/10 * * * *")
    public void buildTradeCalendar() {
        try {
            //生成交易日历
            buildTradeCalendarTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Scheduled(cron = "* 0/6 * * * *")
    public void tradeOrderProcess() {
        try {
            //交易处理日历
            tradeOrderProcessTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
