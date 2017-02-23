package com.ghtl.vwapcalculator.impl

import com.ghtl.vwapcalculator.Calculator
import com.ghtl.vwapcalculator.TwoWayPrice
import com.ghtl.vwapcalculator.domain.InstrumentTwoWayPrice
import com.ghtl.vwapcalculator.domain.NewMarketUpdate
import com.ghtl.vwapcalculator.domain.State
import org.junit.Test

import static com.ghtl.vwapcalculator.domain.Instrument.*
import static com.ghtl.vwapcalculator.domain.Market.*

/**
 * Test class for VwapCalculator component
 */
class VwapCalculatorTest {

    @Test
    public void 'Given null Market update is received Then do not generate VWAP'() {
        Calculator calculator = new VwapCalculator(new MockMarketUpdateLoader())
        assert null == calculator.applyMarketUpdate(null)
    }

    @Test
    public void 'Given Market update is received When Two-Way price is null Then do not generate VWAP'() {
        Calculator calculator = new VwapCalculator(new MockMarketUpdateLoader())
        assert null == calculator.applyMarketUpdate(new NewMarketUpdate(Market0, null))
    }

    @Test
    public void 'Given Market update is received When state is Indicative Then do not generate VWAP'() {
        Calculator calculator = new VwapCalculator(new MockMarketUpdateLoader())
        TwoWayPrice price = new InstrumentTwoWayPrice(Instrument0, State.INDICATIVE, 1.2, 1000, 2.5, 2000)
        assert null == calculator.applyMarketUpdate(new NewMarketUpdate(Market0, price))
    }

    @Test
    public void 'Given Market update is received When state is FIRM Then generate VWAP'() {
        Calculator calculator = new VwapCalculator(new MockMarketUpdateLoader())
        TwoWayPrice price = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        TwoWayPrice vwapPrice = calculator.applyMarketUpdate(new NewMarketUpdate(Market0, price))
        assert vwapPrice != null
        assert vwapPrice.bidPrice == 1.2d
        assert vwapPrice.bidAmount == 1_000
        assert vwapPrice.offerPrice == 2.5d
        assert vwapPrice.offerAmount == 2_000
    }

    @Test
    public void 'Given multiple Market updates When updates are on different Markets of same Instrument Then generate VWAP'() {
        Calculator calculator = new VwapCalculator(new MockMarketUpdateLoader())
        def price = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        def vwapPrice = calculator.applyMarketUpdate(new NewMarketUpdate(Market0, price))
        assert vwapPrice != null
        assert vwapPrice.bidPrice == 1.2d
        assert vwapPrice.bidAmount == 1_000
        assert vwapPrice.offerPrice == 2.5d
        assert vwapPrice.offerAmount == 2_000

        def price2 = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 0.8, 1000, 1.5, 2000)
        def vwapPrice2 = calculator.applyMarketUpdate(new NewMarketUpdate(Market1, price2))
        assert vwapPrice2 != null
        assert vwapPrice2.bidPrice == 1d
        assert vwapPrice2.bidAmount == 2_000
        assert vwapPrice2.offerPrice == 2d
        assert vwapPrice2.offerAmount == 4_000

        def price3 = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.8, 2000, 0.5, 4000)
        def vwapPrice3 = calculator.applyMarketUpdate(new NewMarketUpdate(Market1, price3))
        assert vwapPrice3 != null
        assert vwapPrice3.bidPrice == 1.6d
        assert vwapPrice3.bidAmount == 3_000
        assert vwapPrice3.offerPrice == 1.17d
        assert vwapPrice3.offerAmount == 6_000
    }

    @Test
    public void 'Given multiple Market updates When updates are on different Instruments Then generate VWAP'() {
        Calculator calculator = new VwapCalculator(new MockMarketUpdateLoader())
        def price = new InstrumentTwoWayPrice(Instrument0, State.FIRM, 1.2, 1000, 2.5, 2000)
        def vwapPrice = calculator.applyMarketUpdate(new NewMarketUpdate(Market0, price))
        assert vwapPrice != null
        assert vwapPrice.bidPrice == 1.2d
        assert vwapPrice.bidAmount == 1_000
        assert vwapPrice.offerPrice == 2.5d
        assert vwapPrice.offerAmount == 2_000

        def price2 = new InstrumentTwoWayPrice(Instrument1, State.FIRM, 0.8, 1000, 1.5, 2000)
        def vwapPrice2 = calculator.applyMarketUpdate(new NewMarketUpdate(Market1, price2))
        assert vwapPrice2 != null
        assert vwapPrice2.bidPrice == 0.8d
        assert vwapPrice2.bidAmount == 1_000
        assert vwapPrice2.offerPrice == 1.5d
        assert vwapPrice2.offerAmount == 2_000
    }

    @Test
    public void 'Given multiple Market updates When historical Market updates exist Then generate VWAP'() {
        Calculator calculator = new VwapCalculator(new MockMarketUpdateLoader())
        def price = new InstrumentTwoWayPrice(Instrument10, State.FIRM, 1.2, 1000, 2.5, 2000)
        def vwapPrice = calculator.applyMarketUpdate(new NewMarketUpdate(Market0, price))
        assert vwapPrice != null
        assert vwapPrice.bidPrice == 0.52d
        assert vwapPrice.bidAmount == 5_000
        assert vwapPrice.offerPrice == 1.36d
        assert vwapPrice.offerAmount == 7_000
    }
}
