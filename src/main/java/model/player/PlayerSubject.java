package model.player;

import java.io.IOException;

public interface PlayerSubject {
  void addPlayerObserver(PlayerObserver o);
  void removePlayerObserver(PlayerObserver o);
  void notifyPlayerObservers() throws IOException;
}
