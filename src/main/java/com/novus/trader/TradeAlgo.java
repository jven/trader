package com.novus.trader;

import com.novus.tradesim.*;
import java.util.*;

/**
 * jven's trading algorithm.
 *
 * @author Justin Venezuela (jven@mit.edu)
 */
public class TradeAlgo extends ManagedAlgo {
	
	/** The training data for your algorithm. Maps a security id to a list of prices. */
  private final Map<String, List<Double>> trainingData;

  /** The required constructor. */
  public TradeAlgo(Map<String, List<Double>> data) {
    trainingData = data;
  }

  /** The name of your algorithm. */
  public String getName() {
    return "jven";
  }

  /**
   * Your algorithm implementation resides here. This method is called by the provided
   * client when a market event occurs. Such an event results in a new state notification
   * from the server, which includes your current balance, positions, and the current
   * prices of the market securities.
   */
  public Map<String, TradeRequest> onMarketUpdate(TraderState state) {
    Double balance = state.balance();
    Map<String, Integer> positions = state.positions();
    Map<String, Double> securities = state.securities();

    return null;
  }

  /**
   * Create a trade request.
   *
   * @param bidQuantity   The number of shares you wish to buy.
   * @param bidPrice      The price per share you are willing to pay.
   * @param askQuantity   The number of shares you wish to sell.
   * @param askPrice      The price per share you require.
   */
  public TradeRequest makeTradeRequest(int bidQuantity, double bidPrice, int askQuantity,
      double askPrice) {
    return new TradeRequest(bidQuantity, bidPrice, askQuantity, askPrice);
  }
}
