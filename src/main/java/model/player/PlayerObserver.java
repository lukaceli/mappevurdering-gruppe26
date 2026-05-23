package model.player;

/**
 * Observer interface for receiving notifications when a new game starts.
 */
public interface PlayerObserver {

  /**
   * Called when a new game has started.
   */
  void gameStart();
}