package windows;

import model.player.Player;

import java.io.File;
import java.math.BigDecimal;

public class StartController {
  private Player player;

  public String createPlayer(String name, String capital) {
    try {
      if (name.isEmpty() || capital.isEmpty()) {
        throw new IllegalArgumentException("Player name and capital must be filled");
      }
      BigDecimal capitalBigDecimal = new BigDecimal(capital);
      player = new Player(name, capitalBigDecimal);
      return null;
    } catch (IllegalArgumentException ex) {
      return ex.getMessage();
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void loadFile(File file) {

  }
}
