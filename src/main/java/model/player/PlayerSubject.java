package model.player;

/**
 * Subject interface for managing and notifying player observers.
 */
public interface PlayerSubject {

  /**
   * Registers an observer to be notified when a new game starts.
   *
   * @param observer the observer to add
   */
  void addPlayerObserver(PlayerObserver observer);

  /**
   * Notifies all registered observers that a new game has started.
   */
  void notifyPlayerObservers();
}