package model.appState;

public enum Difficulty {
  EASY, NORMAL, HARD;

  private static Difficulty current;

  public static void setDifficulty(Difficulty difficulty) {
    current = difficulty;
  }

  public static Difficulty getDifficulty() {
    return current;
  }
}