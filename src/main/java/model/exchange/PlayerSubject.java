package model.exchange;

public interface PlayerSubject {
  void addPlayerObserver(PlayerObserver o);
  void removePlayerObserver(PlayerObserver o);
  void notifyPlayerObservers();
}
