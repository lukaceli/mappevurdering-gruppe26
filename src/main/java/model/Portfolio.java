package model;

import java.util.ArrayList;

public class Portfolio {

  private ArrayList<Share> shares = new ArrayList<>();

  public Portfolio() {}

  public void addShare(Share share) {
    shares.add(share);
  }

  public void removeShare(Share share) {
    shares.remove(share);
  }

  public ArrayList<Share> getShares() {
    return shares;
  }

}
