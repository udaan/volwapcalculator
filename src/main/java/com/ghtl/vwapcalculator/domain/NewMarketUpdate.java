package com.ghtl.vwapcalculator.domain;

import com.ghtl.vwapcalculator.MarketUpdate;
import com.ghtl.vwapcalculator.TwoWayPrice;

/**
 * Domain class to hold Current MarketUpdate from a Market
 */
public class NewMarketUpdate implements MarketUpdate {

    private Market market;
    private TwoWayPrice twoWayPrice;

    public NewMarketUpdate(Market market, TwoWayPrice twoWayPrice) {
        this.market = market;
        this.twoWayPrice = twoWayPrice;
    }

    @Override
    public Market getMarket() {
        return market;
    }

    @Override
    public TwoWayPrice getTwoWayPrice() {
        return twoWayPrice;
    }
}
