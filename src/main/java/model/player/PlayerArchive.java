package model.player;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerArchive implements PlayerSubject {
  private final ArrayList<Player> players = new ArrayList<>();
  private final ArrayList<PlayerObserver> observers = new ArrayList<>();

  public void addPlayer(Player player) throws IOException {
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
  public void notifyPlayerObservers() throws IOException {
    for (PlayerObserver o : observers) {
      o.gameStart();
    }
  }
}
