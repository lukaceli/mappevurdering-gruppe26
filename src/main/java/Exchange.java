import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Exchange {

  private String name;
  private int week;
  private HashMap<String, Stock> stockMap = new HashMap<>();
  private Random rand = new Random();
  public Exchange(String name, ArrayList<Stock> stocks) {
    this.name = name;
    this.week = 1;
  }
}
