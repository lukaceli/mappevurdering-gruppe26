package windows;

import model.exchange.PlayerObserver;
import model.exchange.PlayerSubject;
import model.player.Player;

import java.util.ArrayList;

public class PlayerArchive implements PlayerSubject {
  private ArrayList<Player> players = new ArrayList<>();
  private ArrayList<PlayerObserver> observers = new ArrayList<>();

  public void addPlayer(Player player) {
    players.add(player);
    System.out.println(player.getName() + " has been added");
    notifyPlayerObservers();
  }
  public void removePlayer(Player player) {
    players.remove(player);
  }
  public ArrayList<Player> getPlayers() {
    return players;
  }

  @Override
  public void addPlayerObserver(PlayerObserver o) {
    observers.add(o);
  }

  @Override
  public void removePlayerObserver(PlayerObserver o) {
    observers.remove(o);
  }

  @Override
  public void notifyPlayerObservers() {
    for (PlayerObserver o : observers) {
      o.onPlayerChanged();
    }
  }
}
