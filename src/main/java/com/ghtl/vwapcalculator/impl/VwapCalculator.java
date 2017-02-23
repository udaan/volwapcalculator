package com.ghtl.vwapcalculator.impl;

import com.ghtl.vwapcalculator.Calculator;
import com.ghtl.vwapcalculator.MarketUpdate;
import com.ghtl.vwapcalculator.MarketUpdateLoader;
import com.ghtl.vwapcalculator.TwoWayPrice;
import com.ghtl.vwapcalculator.domain.Instrument;
import com.ghtl.vwapcalculator.domain.InstrumentTwoWayPrice;
import com.ghtl.vwapcalculator.domain.Market;
import com.ghtl.vwapcalculator.domain.State;

import java.util.EnumMap;
import java.util.Map;

/**
 * Class responsible for VWAP Calculation.
 *
 * VWAP is calculated per Instrument.
 *
 * For calculating VWAP once MarketUpdate is received, following are taken into consideration:
 * Instrument of the MarketUpdate price and
 * Latest price for a Market for that instrument (means we need to store prices per Market per Instrument)
 *
 * It also keeps running the cumulative calculation of volume.
 */
public class VwapCalculator implements Calculator {

    private final Map<Instrument, CumulativePriceData> pricesPerInstrument = new EnumMap<>(Instrument.class);
    private static final double ROUND_PLACES = 100.0;


    public VwapCalculator(MarketUpdateLoader marketUpdateLoader) {
        init(marketUpdateLoader);
    }

    /*
        Early initialization of pricesPerInstrument for Instrument codes.
        Also can be used to load the collection with today's data if service restarts intraday.
     */
    private void init(MarketUpdateLoader marketUpdateLoader) {
        for (Instrument instrument : Instrument.values()) {
            pricesPerInstrument.put(instrument, new CumulativePriceData(instrument));
        }

        Map<Instrument, Map<Market, TwoWayPrice>> historicPrices = marketUpdateLoader.loadMarketUpdatesPerInstrument();
        if (historicPrices != null) {
            for (Map.Entry<Instrument, Map<Market, TwoWayPrice>> entry : historicPrices.entrySet()) {
                CumulativePriceData data = pricesPerInstrument.get(entry.getKey());
                data.addData(entry.getValue());
            }
        }
    }

    @Override
    public TwoWayPrice applyMarketUpdate(MarketUpdate currentMarketUpdate) {
        if (validMarketUpdate(currentMarketUpdate)) {
            //Valid Firm side price is received.

            CumulativePriceData cumulativePriceData = getUpdatedPricesPerMarket(currentMarketUpdate);

            return calculateVWapPrice(cumulativePriceData);
        }

        return null;
    }

    private boolean validMarketUpdate(MarketUpdate currentMarketUpdate) {
        return currentMarketUpdate != null &&
                currentMarketUpdate.getTwoWayPrice() != null &&
                currentMarketUpdate.getTwoWayPrice().getState() != State.INDICATIVE;
    }

    private CumulativePriceData getUpdatedPricesPerMarket(MarketUpdate currentMarketUpdate) {
        CumulativePriceData cumulativePriceData = pricesPerInstrument.get(currentMarketUpdate.getTwoWayPrice().getInstrument());
        //Add new price or update existing price
        cumulativePriceData.addData(currentMarketUpdate);
        return cumulativePriceData;
    }

    private TwoWayPrice calculateVWapPrice(CumulativePriceData cumulativePriceData) {

        double vwapBid = Math.round((cumulativePriceData.getCurrentBidVolume() / cumulativePriceData.getTotalBidAmount()) * ROUND_PLACES) / ROUND_PLACES;
        double vwapOffer = Math.round((cumulativePriceData.getCurrentOfferVolume() / cumulativePriceData.getTotalOfferAmount()) * ROUND_PLACES) / ROUND_PLACES;
        return new InstrumentTwoWayPrice(cumulativePriceData.getInstrument(), State.FIRM, vwapBid,
                                        cumulativePriceData.getTotalBidAmount(), vwapOffer, cumulativePriceData.getTotalOfferAmount());

    }
}