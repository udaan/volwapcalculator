package com.ghtl.vwapcalculator.impl

import com.ghtl.vwapcalculator.MarketUpdateLoader
import com.ghtl.vwapcalculator.TwoWayPrice
import com.ghtl.vwapcalculator.domain.Instrument
import com.ghtl.vwapcalculator.domain.InstrumentTwoWayPrice
import com.ghtl.vwapcalculator.domain.Market
import com.ghtl.vwapcalculator.domain.State

import static com.ghtl.vwapcalculator.domain.Instrument.*
import static com.ghtl.vwapcalculator.domain.Market.*

/**
 * Dummy historic market price data
 */
public class MockMarketUpdateLoader implements MarketUpdateLoader {
    @Override
    public Map<Instrument, Map<Market, TwoWayPrice>> loadMarketUpdatesPerInstrument() {
        Map<Instrument, Map<Market, TwoWayPrice>> pricesPerInstrument = new EnumMap<>(Instrument.class);

        def price1 = new InstrumentTwoWayPrice(Instrument10, State.FIRM, 0.8, 1000, 1.5, 2000)
        def price2 = new InstrumentTwoWayPrice(Instrument10, State.FIRM, 0.2, 3000, 0.5, 3000)

        Map<Market, TwoWayPrice> pricerPerMarket = new EnumMap<>(Market.class)

        pricerPerMarket.put(Market14, price1)
        pricerPerMarket.put(Market15, price2)

        pricesPerInstrument.put(Instrument10, pricerPerMarket)
        return pricesPerInstrument;
    }
}
