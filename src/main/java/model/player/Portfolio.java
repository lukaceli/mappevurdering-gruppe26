package model.player;

import java.util.ArrayList;
import java.util.List;
import model.stock.Share;

public class Portfolio {

  private List<Share> shares = new ArrayList<>();

  public Portfolio() {}

  public void addShare(Share share) {
    shares.add(share);
  }

  public void removeShare(Share share) {
    shares.remove(share);
  }

  public List<Share> getShares() {
    return shares;
  }

}
