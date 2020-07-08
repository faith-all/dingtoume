package me.dingtou.web;

import me.dingtou.task.SyncFundPriceTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by qiyan on 2017/6/16.
 */
@Controller
@RequestMapping("/sync.jsp")
public class SyncController {

    @Autowired
    private SyncFundPriceTask syncFundPriceTask;


    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    String doGet() throws Exception {

        try {
            syncFundPriceTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "任务执行完毕";
    }


}
