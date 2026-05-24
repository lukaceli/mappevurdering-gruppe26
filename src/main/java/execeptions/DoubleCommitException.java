package execeptions;

public class DoubleCommitException extends RuntimeException {
  public DoubleCommitException(String message) {
    super(message);
  }
}
