// Copyright 2012. All rights reserved.

package com.novus.trader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Price history for a particular security.
 *
 * @author Justin Venezuela (jven@mit.edu)
 */
public class PriceHistory {

  private final String secName;
  private final List<Double> pastPrices;
  
  private final int SCOPE = 7;
  
  public PriceHistory(String secName) {
    this.secName = secName;
    pastPrices = new ArrayList<Double>();
  }
  
  public void reportPrice(double price) {
    pastPrices.add(price);
  }
  
  public double getValuation() {
    int n = pastPrices.size();
    if (pastPrices.size() < SCOPE) {
      return pastPrices.get(n - 1);
    }
    List<Double> lastFewPrices = new ArrayList<Double>();
    for (int idx = n - SCOPE; idx < n; idx++) {
      lastFewPrices.add(pastPrices.get(idx));
    }
    Collections.sort(lastFewPrices);
    return 0.5 * (lastFewPrices.get(SCOPE / 2) + lastFewPrices.get((SCOPE / 2) + 1));
  }
}
