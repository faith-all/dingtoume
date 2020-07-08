package me.dingtou.service.impl;

import me.dingtou.domain.FundDailyPrice;
import me.dingtou.domain.FundInfo;
import me.dingtou.exception.BizException;
import me.dingtou.service.FundPullService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiyan on 2017/6/18.
 */
@Service
public class FundPullServiceImpl implements FundPullService {

    private static Cache<String, Double> fundPriceCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).maximumSize(500).build();

    @Override
    public FundInfo pullFundInfo(String fundCode) throws BizException {
        try {
            StringBuffer content = getUrlContent(String.format("https://fundmobapi.eastmoney.com/FundMApi/FundVarietieValuationDetail.ashx?FCODE=%s&deviceid=h5&plat=Wap&product=EFund&version=1.2", fundCode));
            JSONObject fundData = JSON.parseObject(content.toString());
            JSONObject expansion = fundData.getJSONObject("Expansion");
            if (null != expansion) {
                FundInfo fundInfo = new FundInfo();
                fundInfo.setFundCode(fundCode);
                fundInfo.setFundName(expansion.getString("SHORTNAME"));
                fundInfo.setBuyRate(expansion.getString("SOURCERATE"));
                return fundInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FundDailyPrice> pullFundPrice(String fundCode, boolean isFullMode) throws BizException {
        //

        try {
            List<FundDailyPrice> prices = new ArrayList<FundDailyPrice>();
            StringBuffer content = getUrlContent(String.format("https://fundmobapi.eastmoney.com/FundMApi/FundNetDiagram.ashx?deviceid=h5&version=1.2&product=EFund&plat=Wap&FCODE=%s&pageIndex=1&pageSize=%s&_=%s", fundCode, isFullMode ? 500 : 5, System.currentTimeMillis()));
            JSONObject fundData = JSON.parseObject(content.toString());
            JSONArray datas = fundData.getJSONArray("Datas");
            if (null != datas) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                datas.stream().forEach(data -> {
                    JSONObject jsonData = (JSONObject) data;
                    FundDailyPrice price = new FundDailyPrice();
                    price.setFundCode(fundCode);
                    try {
                        price.setPriceDate(sdf.parse(jsonData.getString("FSRQ")));
                    } catch (ParseException e) {
                        throw new RuntimeException("参数异常");
                    }
                    price.setUnitPrice(jsonData.getString("DWJZ"));
                    price.setTotalPrice(jsonData.getString("LJJZ"));
                    price.setIncrease(jsonData.getString("JZZZL"));
                    price.setMemo(jsonData.getString("Remarks"));
                    prices.add(price);
                });
                return prices;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double pullNowFundPrice(String fundCode) throws BizException {

        //https://fundmobapi.eastmoney.com/FundMApi/FundVarietieValuationDetail.ashx?FCODE=000961&deviceid=h5&plat=Wap&product=EFund&version=1.2
        try {
            return fundPriceCache.get(fundCode, new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    String url = String.format("https://fundmobapi.eastmoney.com/FundMApi/FundBasicInformation.ashx?FCODE=%s&deviceid=h5&plat=Wap&product=EFund&version=1.2", fundCode);
                    StringBuffer content = getUrlContent(url);
                    JSONObject fundData = JSON.parseObject(content.toString());
                    JSONObject data = fundData.getJSONObject("Datas");
                    double price = data.getDoubleValue("DWJZ");
                    Date fsrq = data.getDate("FSRQ");
                    if (DateUtils.isSameDay(fsrq, new Date())) {
                        return price;
                    }


                    url = String.format("https://fundmobapi.eastmoney.com/FundMApi/FundVarietieValuationDetail.ashx?FCODE=%s&deviceid=h5&plat=Wap&product=EFund&version=1.2", fundCode);
                    content = getUrlContent(url);
                    fundData = JSON.parseObject(content.toString());
                    JSONObject expansion = fundData.getJSONObject("Expansion");
                    double gz = expansion.getDoubleValue("GZ");
                    Date gztime = DateUtils.parseDate(expansion.getString("GZTIME"), "yyyy-MM-dd HH:mm");

                    if (DateUtils.isSameDay(fsrq, gztime)) {
                        return price;
                    }

                    fsrq = DateUtils.addHours(fsrq, 15);
                    fsrq = DateUtils.addSeconds(fsrq, 1);
                    if (fsrq.before(gztime)) {
                        return gz;
                    }

                    return price;
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private StringBuffer getUrlContent(String url) throws IOException {
        URL urlObj = new URL(url);
        InputStream inputStream = urlObj.openConnection().getInputStream();
        StringBuffer content = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return content;
    }

    public static void main(String[] args) throws BizException {
        System.out.println(new FundPullServiceImpl().pullNowFundPrice("000961"));
    }
}
