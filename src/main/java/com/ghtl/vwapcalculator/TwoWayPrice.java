package com.ghtl.vwapcalculator;

import com.ghtl.vwapcalculator.domain.Instrument;
import com.ghtl.vwapcalculator.domain.State;

/**
 * Contract for TwoWayPrice
 */
public interface TwoWayPrice {
    Instrument getInstrument();

    State getState();

    double getBidPrice();
    double getBidAmount();

    double getOfferAmount();
    double getOfferPrice();
}
