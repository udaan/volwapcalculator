package com.ghtl.vwapcalculator.impl;

import com.ghtl.vwapcalculator.MarketUpdate;
import com.ghtl.vwapcalculator.TwoWayPrice;
import com.ghtl.vwapcalculator.domain.Instrument;
import com.ghtl.vwapcalculator.domain.Market;

import java.util.EnumMap;
import java.util.Map;

/**
 * Keeps running calculation of VWap related volume and amount.
 */
public class CumulativePriceData {
    private Instrument instrument;
    private double currentBidVolume;
    private double totalBidAmount;
    private double currentOfferVolume;
    private double totalOfferAmount;
    private Map<Market, TwoWayPrice> pricesPerMarket = new EnumMap<>(Market.class);

    public CumulativePriceData(Instrument instrument) {
        this.instrument = instrument;
    }

    public void addData(MarketUpdate marketUpdate) {
        if(pricesPerMarket.containsKey(marketUpdate.getMarket())) {
            //This price needs to be updated from the cumulative calculation, hence removed
            TwoWayPrice oldPrice = pricesPerMarket.get(marketUpdate.getMarket());
            currentBidVolume -= oldPrice.getBidAmount() * oldPrice.getBidPrice();
            currentOfferVolume -= oldPrice.getOfferAmount() * oldPrice.getOfferPrice();
            totalBidAmount -= oldPrice.getBidAmount();
            totalOfferAmount -= oldPrice.getOfferAmount();
        }

        TwoWayPrice newPrice = marketUpdate.getTwoWayPrice();
        updateCumulativeData(newPrice);
        pricesPerMarket.put(marketUpdate.getMarket(), marketUpdate.getTwoWayPrice());
    }

    private void updateCumulativeData(TwoWayPrice price) {
        currentBidVolume += price.getBidAmount() * price.getBidPrice();
        currentOfferVolume += price.getOfferAmount() * price.getOfferPrice();
        totalBidAmount += price.getBidAmount();
        totalOfferAmount += price.getOfferAmount();
    }

    public void addData(Map<Market, TwoWayPrice> pricesPerMarket) {
        for (Map.Entry<Market, TwoWayPrice> entry : pricesPerMarket.entrySet())
        {
            TwoWayPrice newPrice = entry.getValue();
            updateCumulativeData(newPrice);
        }
    }

    public double getCurrentBidVolume() {
        return currentBidVolume;
    }

    public double getCurrentOfferVolume() {
        return currentOfferVolume;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public double getTotalBidAmount() {
        return totalBidAmount;
    }

    public double getTotalOfferAmount() {
        return totalOfferAmount;
    }
}
