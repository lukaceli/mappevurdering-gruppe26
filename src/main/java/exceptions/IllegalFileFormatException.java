package exceptions;

/**
 * Thrown when a file does not conform to the expected format.
 */
public class IllegalFileFormatException extends RuntimeException {

  /**
   * Constructs an IllegalFileFormatException with the given message.
   *
   * @param message the detail message
   */
  public IllegalFileFormatException(String message) {
    super(message);
  }
}
