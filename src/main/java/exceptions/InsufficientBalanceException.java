package exceptions;

/**
 * Thrown when a player attempts to withdraw more money than their current balance allows.
 */
public class InsufficientBalanceException extends RuntimeException {

  /**
   * Constructs an InsufficientBalanceException with the given message.
   *
   * @param message the detail message
   */
  public InsufficientBalanceException(String message) {
    super(message);
  }
}
