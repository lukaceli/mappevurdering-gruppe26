package exceptions;

/**
 * Thrown when a player name does not meet the required format or length constraints.
 */
public class InvalidPlayerName extends RuntimeException {

  /**
   * Constructs an InvalidPlayerName with the given message.
   *
   * @param message the detail message
   */
  public InvalidPlayerName(String message) {
    super(message);
  }
}
