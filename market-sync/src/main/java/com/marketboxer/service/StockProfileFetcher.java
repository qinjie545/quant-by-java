package com.marketboxer.service;

import com.futu.openapi.pb.QotCommon;
import com.futu.openapi.services.SecurityStaticInfoFacade;
import com.marketboxer.core.model.MarketCodeEnum;
import com.marketboxer.core.model.StockProfile;
import com.marketboxer.data.access.mapper.StockProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinjie  at 2022/9/16
 * e
 **/
@Component
public class StockProfileFetcher {

    @Autowired
    StockProfileMapper stockProfileMapper;

    /**
     * 获取股票基本信息
     * @param marketCode 股票代码 @See {@link MarketCodeEnum}
     * @return 股票基本信息
     */
    public int fetchStockProfile(String marketCode) {

        MarketCodeEnum marketCodeEnum = MarketCodeEnum.fromCode(marketCode);

        SecurityStaticInfoFacade securitySnapshotFacade = new SecurityStaticInfoFacade();
        List<QotCommon.SecurityStaticInfo> staticInfos = securitySnapshotFacade.fetchSecurityStaticInfoOfMarket(convertFromMarketCode(marketCodeEnum));

        List<StockProfile> stockProfiles = new ArrayList<>(100);

        for (QotCommon.SecurityStaticInfo staticInfo : staticInfos) {
            StockProfile stockProfile = new StockProfile();

            String securityCode = staticInfo.getBasic().getSecurity().getCode();
            boolean isDelisting = staticInfo.getBasic().getDelisting();
            String securityName = staticInfo.getBasic().getName();
            long id = staticInfo.getBasic().getId();
            int exchType = staticInfo.getBasic().getExchType();
            String listingTime = staticInfo.getBasic().getListTime();
            int lotSize = staticInfo.getBasic().getLotSize();
            int secType = staticInfo.getBasic().getSecType();

            stockProfile.setId(id);
            stockProfile.setMarketCode(marketCode);
            stockProfile.setExchType(exchType);
            stockProfile.setSecurityName(securityName);
            stockProfile.setMarketCode(marketCode);
            stockProfile.setSecurityCode(securityCode);
            stockProfile.setLotSize(lotSize);
            stockProfile.setListingTime(listingTime);
            stockProfile.setIsDelisting(isDelisting?1:0);
            stockProfile.setSecType(secType);

            stockProfiles.add(stockProfile);
        }
        return storeStockProfile(stockProfiles);
    }

    private int storeStockProfile(List<StockProfile> stockProfiles) {
        return stockProfileMapper.insertMany(stockProfiles);
    }

    private QotCommon.QotMarket convertFromMarketCode(MarketCodeEnum marketCode) {

        switch (marketCode) {
            case CNSH_S:
                return QotCommon.QotMarket.QotMarket_CNSH_Security;
            default:
                //TODO add more securityMarket convert
                return QotCommon.QotMarket.QotMarket_Unknown;
        }
    }
}
