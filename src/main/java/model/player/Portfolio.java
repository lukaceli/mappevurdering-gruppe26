package model.player;

import java.util.ArrayList;
import java.util.List;
import model.stock.Share;

public class Portfolio {

  private List<Share> shares = new ArrayList<>();

  public Portfolio() {}

  public void addShare(Share share) {
    if (share == null) {
      throw new NullPointerException("share cannot be null ");
    }
    shares.add(share);
  }

  public void removeShare(Share share) {
    if (shares.contains(share)) {
      shares.remove(share);
    }
    else  {
      throw new NullPointerException("share does not exist");
    }
  }

  public List<Share> getShares() {
    return shares;
  }

}
