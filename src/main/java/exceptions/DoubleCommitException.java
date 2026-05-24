package exceptions;

/**
 * Thrown when a transaction is committed more than once.
 */
public class DoubleCommitException extends RuntimeException {

  /**
   * Constructs a DoubleCommitException with the given message.
   *
   * @param message the detail message
   */
  public DoubleCommitException(String message) {
    super(message);
  }
}
