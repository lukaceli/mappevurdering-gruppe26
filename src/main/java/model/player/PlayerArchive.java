package model.player;

import java.util.ArrayList;

/**
 * Maintains a list of players and notifies observers when a new player is added.
 * This class was meant for a further expansion of the application but,
 * we didn't have time so we left it as it is.
 */
public class PlayerArchive implements PlayerSubject {

  private final ArrayList<Player> players = new ArrayList<>();
  private final ArrayList<PlayerObserver> observers = new ArrayList<>();

  /**
   * Adds a player to the archive and notifies all registered observers.
   *
   * @param player the player to add
   */
  public void addPlayer(Player player) {
    players.add(player);
    notifyPlayerObservers();
  }

  @Override
  public void addPlayerObserver(PlayerObserver observer) {
    observers.add(observer);
  }

  @Override
  public void notifyPlayerObservers() {
    for (PlayerObserver observer : observers) {
      observer.gameStart();
    }
  }
}