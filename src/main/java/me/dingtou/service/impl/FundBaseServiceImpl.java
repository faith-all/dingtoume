package me.dingtou.service.impl;

import me.dingtou.domain.FundDailyPrice;
import me.dingtou.domain.FundDailyPriceExample;
import me.dingtou.domain.FundInfo;
import me.dingtou.domain.FundInfoExample;
import me.dingtou.exception.BizException;
import me.dingtou.mapper.FundDailyPriceMapper;
import me.dingtou.mapper.FundInfoMapper;
import me.dingtou.service.FundBaseService;
import me.dingtou.service.FundPullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiyan on 2017/6/18.
 */
@Service
public class FundBaseServiceImpl implements FundBaseService {

    @Autowired
    private FundInfoMapper fundInfoMapper;

    @Autowired
    private FundDailyPriceMapper fundDailyPriceMapper;

    @Autowired
    private FundPullService fundPullService;


    @Override
    public void addFundInfo(String fundCode) throws BizException {
        FundInfo fundInfo = fundPullService.pullFundInfo(fundCode);
        if (null != fundInfo) {
            FundInfo dbFundInfo = fundInfoMapper.selectByPrimaryKey(fundCode);
            if (null == dbFundInfo) {
                fundInfoMapper.insert(fundInfo);
            } else {
                FundInfoExample query = new FundInfoExample();
                query.createCriteria().andFundCodeEqualTo(fundCode);
                fundInfoMapper.updateByExample(fundInfo, query);
            }
        } else {
            throw new BizException("数据源拉取失败");
        }

    }

    @Override
    public void syncFundPrice(String fundCode, boolean isFullMode) throws BizException {
        List<FundDailyPrice> fundPriceList = fundPullService.pullFundPrice(fundCode, isFullMode);
        if (null != fundPriceList) {
            fundPriceList.stream().forEach(fundPrice -> {
                FundDailyPriceExample query = new FundDailyPriceExample();
                query.createCriteria().andFundCodeEqualTo(fundCode)
                        .andPriceDateEqualTo(fundPrice.getPriceDate());
                long count = fundDailyPriceMapper.countByExample(query);
                if (count <= 0) {
                    fundDailyPriceMapper.insert(fundPrice);
                }
            });

        }
    }

    @Override
    public FundInfo queryFundInfo(String fundCode) {
        return fundInfoMapper.selectByPrimaryKey(fundCode);
    }

    @Override
    public List<FundInfo> queryAllFundInfo() {
        FundInfoExample query = new FundInfoExample();
        return fundInfoMapper.selectByExample(query);
    }

    @Override
    public List<FundDailyPrice> queryFundPrice(String fundCode) {
        FundDailyPriceExample query = new FundDailyPriceExample();
        query.createCriteria().andFundCodeEqualTo(fundCode);
        return fundDailyPriceMapper.selectByExample(query);
    }
}
