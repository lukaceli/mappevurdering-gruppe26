package model.player;

import java.util.ArrayList;
import java.util.List;
import model.stock.Share;

/**
 * Represents a player's portfolio of share positions.
 */
public class Portfolio {

  private final List<Share> shares;

  /**
   * Constructs an empty {@code Portfolio}.
   */
  public Portfolio() {
    this.shares = new ArrayList<>();
  }

  /**
   * Adds a share to the portfolio.
   *
   * @param share the share to add
   * @throws NullPointerException if {@code share} is null
   */
  public void addShare(Share share) {
    if (share == null) {
      throw new NullPointerException("Share cannot be null");
    }
    shares.add(share);
  }

  /**
   * Removes a share from the portfolio.
   *
   * @param share the share to remove
   * @throws IllegalArgumentException if the share is not in the portfolio
   */
  public void removeShare(Share share) {
    if (!shares.contains(share)) {
      throw new IllegalArgumentException("Share does not exist in portfolio");
    }
    shares.remove(share);
  }

  /**
   * Returns all shares in the portfolio.
   *
   * @return a list of all {@link Share} objects
   */
  public List<Share> getShares() {
    return shares;
  }
}