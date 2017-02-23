package com.ghtl.vwapcalculator;

/**
 * Calculator responsible for calculating VWAP for every MarketUpdate tick.
 */
public interface Calculator {
    TwoWayPrice applyMarketUpdate(final MarketUpdate twoWayMarketPrice);
}
