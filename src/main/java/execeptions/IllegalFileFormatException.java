package execeptions;

public class IllegalFileFormatException extends RuntimeException {
  public IllegalFileFormatException(String message) {
    super(message);
  }
}
