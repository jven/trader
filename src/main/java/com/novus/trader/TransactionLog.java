// Copyright 2012. All rights reserved.

package com.novus.trader;

import com.novus.tradesim.TradeRequest;
import com.novus.tradesim.TraderState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Log of all transactions I've made.
 *
 * @author Justin Venezuela (jven@mit.edu)
 */
public class TransactionLog {
  
  private class Request {
    public int prevQ;
    public int bidQ;
    public double bidP;
    public int askQ;
    public double askP;
    
    private Request(int prevQ, int bidQ, double bidP, int askQ, double askP) {
      this.prevQ = prevQ;
      this.bidQ = bidQ;
      this.bidP = bidP;
      this.askQ = askQ;
      this.askP = askP;
    }
  }
  
  private class Transaction {
    public int amt;
    public double price;
    
    private Transaction(int amt, double price) {
      this.amt = amt;
      this.price = price;
    }
  }
  
  private final String secName;
  private List<Transaction> transactions;
  
  private Request liveRequest;
  
  public TransactionLog(String secName) {
    this.secName = secName;
    transactions = new ArrayList<Transaction>();
  }
  
  public void reportRequest(int prevQ, int bidQ, double bidP, int askQ, double askP) {
    liveRequest = new Request(prevQ, bidQ, bidP, askQ, askP);
  }
  
  public void reportMarketUpdate(TraderState state) {
    if (liveRequest != null) {
      Map<String, Integer> positions = state.positions();
      if (positions.containsKey(secName)) {
        int curQ = positions.get(secName);
        if (curQ > liveRequest.prevQ) {
          handleBuy(curQ - liveRequest.prevQ, liveRequest.bidP);
        } else if (curQ < liveRequest.prevQ) {
          handleSell(liveRequest.prevQ - curQ, liveRequest.askP);
        }
      }
      liveRequest = null;
    }
  }
  
  public int getAmountCanSellAtPrice(double price) {
    int ans = 0;
    for (Transaction t : transactions) {
      if (t.price < price) {
        ans += t.amt;
      }
    }
    return ans;
  }
  
  private void handleBuy(int amt, double price) {
    transactions.add(new Transaction(amt, price));
  }
  
  private void handleSell(int amt, double price) {
    while (amt > 0) {
      for (Transaction t : transactions) {
        if (t.price < price) {
          if (amt <= t.amt) { 
            amt -= t.amt;
            t.amt = 0;
          } else {
            t.amt -= amt;
            amt = 0;
          }
          break;
        }
      }
    }
    // remove transactions with zero amt
    List<Transaction> newTransactions = new ArrayList<Transaction>();
    for (Transaction t : transactions) {
      if (t.amt > 0) {
        newTransactions.add(t);
      }
    }
    transactions = newTransactions;
  }
}
