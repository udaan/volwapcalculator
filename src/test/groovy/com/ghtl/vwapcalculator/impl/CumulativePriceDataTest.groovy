package com.ghtl.vwapcalculator.impl

import com.ghtl.vwapcalculator.TwoWayPrice
import com.ghtl.vwapcalculator.domain.InstrumentTwoWayPrice
import com.ghtl.vwapcalculator.domain.Market
import com.ghtl.vwapcalculator.domain.NewMarketUpdate
import com.ghtl.vwapcalculator.domain.State

import static com.ghtl.vwapcalculator.domain.Instrument.*
import org.junit.Test

import static com.ghtl.vwapcalculator.domain.Market.*

/**
 * Test class for CumulativePriceData
 */
class CumulativePriceDataTest {
    @Test
    public void testPriceAdd() {
        CumulativePriceData data = new CumulativePriceData(Instrument0)

        def price = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        data.addData(new NewMarketUpdate(Market0, price))

        assert data.currentBidVolume == 1200
        assert data.currentOfferVolume == 5000
        assert data.totalBidAmount == 1000
        assert data.totalOfferAmount == 2000
    }

    @Test
    public void testRepeatedPriceAddSameMarket() {
        CumulativePriceData data = new CumulativePriceData(Instrument0)

        def price = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        data.addData(new NewMarketUpdate(Market0, price))

        def price2 = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        data.addData(new NewMarketUpdate(Market0, price2))

        assert data.currentBidVolume == 1200
        assert data.currentOfferVolume == 5000
        assert data.totalBidAmount == 1000
        assert data.totalOfferAmount == 2000
    }

    @Test
    public void testRepeatedPriceAddDifferentMarket() {
        CumulativePriceData data = new CumulativePriceData(Instrument0)

        def price = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        data.addData(new NewMarketUpdate(Market0, price))

        def price2 = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        data.addData(new NewMarketUpdate(Market1, price2))

        assert data.currentBidVolume == 2400
        assert data.currentOfferVolume == 10000
        assert data.totalBidAmount == 2000
        assert data.totalOfferAmount == 4000
    }

    @Test
    public void testAddListOfPriceData() {
        CumulativePriceData data = new CumulativePriceData(Instrument0)

        def price1 = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        def price2 = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)

        Map<Market, TwoWayPrice> pricerPerMarket = new EnumMap<>(Market.class)

        pricerPerMarket.put(Market14, price1)
        pricerPerMarket.put(Market15, price2)


        data.addData(pricerPerMarket)


        assert data.currentBidVolume == 2400
        assert data.currentOfferVolume == 10000
        assert data.totalBidAmount == 2000
        assert data.totalOfferAmount == 4000
    }
}
