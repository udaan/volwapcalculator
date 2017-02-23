package com.ghtl.vwapcalculator;

import com.ghtl.vwapcalculator.domain.Instrument;
import com.ghtl.vwapcalculator.domain.Market;

import java.util.Map;

/**
 * Contract for MarketUpdateLoader from history
 */
public interface MarketUpdateLoader {
    Map<Instrument, Map<Market, TwoWayPrice>> loadMarketUpdatesPerInstrument();
}
