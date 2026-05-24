package exceptions;

/**
 * Thrown when a buy or sell transaction cannot be completed.
 */
public class TransactionFailedException extends RuntimeException {

  /**
   * Constructs a TransactionFailedException with the given message.
   *
   * @param message the detail message
   */
  public TransactionFailedException(String message) {
    super(message);
  }
}
