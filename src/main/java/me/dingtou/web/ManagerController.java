package me.dingtou.web;

import me.dingtou.domain.dto.FundBuyInfo;
import me.dingtou.service.FundBaseService;
import me.dingtou.service.FundTradeService;
import me.dingtou.task.BuildTradeCalendarTask;
import me.dingtou.task.TradeOrderProcessTask;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import java.util.Date;
import java.util.List;

/**
 * @author qiyan
 * @date 2017/6/18
 */
@Controller
public class ManagerController {
    @Autowired
    private FundBaseService fundBaseService;

    @Autowired
    private FundTradeService fundTradeService;

    @Autowired
    private BuildTradeCalendarTask buildTradeCalendarTask;

    @Autowired
    private TradeOrderProcessTask tradeOrderProcessTask;

    @RequestMapping(value = "/fundmanager.jsp", method = RequestMethod.GET)
    public
    @ResponseBody
    String fundManager(@RequestParam(value = "type", required = true, defaultValue = "add") String type,
        @RequestParam(value = "value", required = true) String value,
        @RequestParam(value = "timestamp", required = false) String timestamp,
        @RequestParam(value = "userId", required = true, defaultValue = "1") String userId) throws Exception {

        if (StringUtils.isBlank(value)) {
            return "param error.";
        }
        switch (type) {
            case "add":
                fundBaseService.addFundInfo(value);
                break;
            case "sync":
                fundBaseService.syncFundPrice(value, true);
                break;
            case "buy":
                String buyParam = new String(new BASE64Decoder().decodeBuffer(value), "utf-8");
                List<FundBuyInfo> buyInfos = JSON.parseArray(buyParam, FundBuyInfo.class);
                fundTradeService.createFundOrders(Long.parseLong(userId),new Date(Long.parseLong(timestamp)), buyInfos);
                break;
            case "calendar":
                buildTradeCalendarTask.call();
                break;
            case "order":
                tradeOrderProcessTask.call();
                break;
            default:
                break;
        }
        return "done.";
    }

}
