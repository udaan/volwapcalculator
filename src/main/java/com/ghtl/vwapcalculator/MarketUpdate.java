package com.ghtl.vwapcalculator;

import com.ghtl.vwapcalculator.domain.Market;

/**
 * Contract for MarketUpdate
 */
public interface MarketUpdate {
    Market getMarket();
    TwoWayPrice getTwoWayPrice();
}