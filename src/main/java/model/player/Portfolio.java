package model.player;

import java.util.ArrayList;
import model.stock.Share;

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
