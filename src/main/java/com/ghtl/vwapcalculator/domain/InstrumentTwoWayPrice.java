package com.ghtl.vwapcalculator.domain;

import com.ghtl.vwapcalculator.TwoWayPrice;

import java.io.Serializable;

/**
 * Domain class to hold Vwap Two-Way Price.
 *
 * VWAP is kept Serializable so it can be sent periodically to another service, GUI or over network etc
 */
public final class InstrumentTwoWayPrice implements TwoWayPrice, Serializable {

    private static final long serialVersionUID = -7218614352779347729L;

    private final Instrument instrument;
    private final State state;
    private final double bidPrice;
    private final double offerPrice;
    private final double bidAmount;
    private final double offerAmount;

    public InstrumentTwoWayPrice(Instrument instrument, State state,
                                 double bidPrice, double bidAmount,
                                 double offerPrice, double offerAmount) {
        this.bidAmount = bidAmount;
        this.instrument = instrument;
        this.state = state;
        this.bidPrice = bidPrice;
        this.offerPrice = offerPrice;
        this.offerAmount = offerAmount;
    }

    @Override
    public Instrument getInstrument() {
        return instrument;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public double getBidPrice() {
        return bidPrice;
    }

    @Override
    public double getBidAmount() {
        return bidAmount;
    }

    @Override
    public double getOfferPrice() {
        return offerPrice;
    }

    @Override
    public double getOfferAmount() {
        return offerAmount;
    }
}