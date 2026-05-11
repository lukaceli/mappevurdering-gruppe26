package model.exchange;

import java.util.ArrayList;

public class ExchangeList {
  private ArrayList<Exchange> exchanges;
  public ExchangeList() {
    exchanges = new ArrayList<>();

  }

  public void addExchange(Exchange exchange) {
    exchanges.add(exchange);
  }

  public ArrayList<Exchange> getExchanges() {
    return exchanges;
  }
}
