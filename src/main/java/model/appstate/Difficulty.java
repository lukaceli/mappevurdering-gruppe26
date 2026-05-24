package model.appstate;

/**
 * Represents the difficulty levels available in the game.
 * The current difficulty is stored as a static field and can be
 * get and set globally via {@link #getDifficulty()} and {@link #setDifficulty(Difficulty)}.
 */
public enum Difficulty {
  EASY, NORMAL, HARD;

  private static Difficulty current;

  /**
   * Sets the current global difficulty level.
   *
   * @param difficulty the difficulty to set
   */
  public static void setDifficulty(Difficulty difficulty) {
    current = difficulty;
  }

  /**
   * Returns the current global difficulty level.
   *
   * @return the current {@link Difficulty}
   */
  public static Difficulty getDifficulty() {
    return current;
  }
}