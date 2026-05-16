package execeptions;

public class InvalidPlayerName extends RuntimeException {
  public InvalidPlayerName(String message) {
    super(message);
  }
}
