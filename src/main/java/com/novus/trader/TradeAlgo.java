package com.novus.trader;

import com.novus.tradesim.*;
import java.util.*;

/**
 * jven's trading algorithm.
 *
 * @author Justin Venezuela (jven@mit.edu)
 */
public class TradeAlgo extends ManagedAlgo {

  private final Map<String, PriceHistory> hists;
  private final Map<String, TransactionLog> logs;
  
  public TradeAlgo(Map<String, List<Double>> data) {
    // we ignore the training data
    hists = new HashMap<String, PriceHistory>();
    logs = new HashMap<String, TransactionLog>();
    for (String secName : data.keySet()) {
      hists.put(secName, new PriceHistory(secName));
      logs.put(secName, new TransactionLog(secName));
    }
  }

  public String getName() {
    return "jven";
  }

  public Map<String, TradeRequest> onMarketUpdate(TraderState state) {
    // my monies
    Double balance = state.balance();
    // amount I have
    Map<String, Integer> positions = state.positions();
    // current price
    Map<String, Double> securities = state.securities();
    
    // report prices and transactions
    for (String secName : securities.keySet()) {
      if (hists.containsKey(secName)) {
        hists.get(secName).reportPrice(state.securities().get(secName));
      }
      if (logs.containsKey(secName)) {
        logs.get(secName).reportMarketUpdate(state);
      }
    }
    
    // make trade requests
    Map<String, TradeRequest> ans = new HashMap<String, TradeRequest>();
    for (String secName : hists.keySet()) {
      if (logs.containsKey(secName) && positions.containsKey(secName)
          && securities.containsKey(secName)) {
        PriceHistory hist = hists.get(secName);
        double price = securities.get(secName);
        // split balance evenly amongst various securities
        int amtIHave = positions.get(secName);
        int amtCanBuy = (int)(balance / (hists.size() * price));
        double myVal = hist.getValuation();
        double bidP = Math.min(myVal, (price + myVal) / 2);
        double askP = myVal + 0.01;
        int amtCanSell = logs.get(secName).getAmountCanSellAtPrice(askP);
        TradeRequest req = makeTradeRequest(amtCanBuy, bidP, amtCanSell, askP);
        ans.put(secName, req);
        logs.get(secName).reportRequest(amtIHave, amtCanBuy, bidP, amtCanSell, askP);
      }
    }
    return ans;
  }

  public TradeRequest makeTradeRequest(int bidQuantity, double bidPrice, int askQuantity,
      double askPrice) {
    return new TradeRequest(bidQuantity, bidPrice, askQuantity, askPrice);
  }
}
