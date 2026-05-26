package windows.trade;

import java.math.BigDecimal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Stock;

/**
 * Abstract base class for trade windows, providing shared user interface components
 * and logic for handling asset transactions like buying and selling.
 */
public abstract class TradeWindow {
  protected Label commission;
  protected Label tax;
  protected Label total;
  protected Label amountError;
  protected Label confirmation;
  protected Label amount;
  protected Label balance;
  private final Player player;
  private TextField amountField;
  private Runnable onAfterTrade;

  /**
   * Constructs a {@code TradeWindow} for the given player.
   *
   * @param player the player performing the trades
   */
  protected TradeWindow(Player player) {
    this.player = player;
  }

  /**
   * Sets the callback action to be executed after a successful trade.
   *
   * @param onAfterTrade the callback runnable
   */
  public void setOnAfterTrade(Runnable onAfterTrade) {
    this.onAfterTrade = onAfterTrade;
  }

  /**
   * Notifies the trade listener by executing the registered callback, if present.
   */
  public void notifyAfterTrade() {
    if (onAfterTrade != null) {
      onAfterTrade.run();
    }
  }

  /**
   * Returns the prefix label text used for the header title in the window.
   *
   * @return the action label text
   */
  protected abstract String getActionLabel();

  /**
   * Returns the text field used for entering the share amount.
   *
   * @return the amount {@link TextField}
   */
  protected TextField getAmountField() {
    return amountField;
  }

  /**
   * Returns the inline CSS styling rules for the main action button.
   *
   * @return the CSS style string
   */
  protected abstract String getActionButtonStyle();

  /**
   * Returns the text label to display inside the main action button.
   *
   * @return the action button text
   */
  protected abstract String getActionButtonText();

  /**
   * Handles the event when the amount selection button is clicked.
   *
   * @param text the text currently entered the amount field
   */
  protected abstract void onAmountClicked(String text);

  /**
   * Handles the event when the max quantity button is clicked.
   */
  protected abstract void onMaxBtnClicked();

  /**
   * Handles the event when the main transaction action button is clicked.
   */
  protected abstract void onActionClicked();

  /**
   * Creates and constructs the visual layout for the trade window popup.
   *
   * @param stock    the stock being traded
   * @param parent   the parent pane where the popup will be added
   * @param exchange the exchange managing the stock
   * @return the completed {@link VBox} popup layout
   */
  public VBox create(Stock stock, StackPane parent, Exchange exchange) {
    balance = new Label("");
    balance.getStyleClass().add("trade-balance");
    StackPane.setAlignment(balance, Pos.TOP_RIGHT);
    StackPane.setMargin(balance, new Insets(10));

    VBox mainContent = new VBox(15);
    mainContent.setAlignment(Pos.CENTER);

    String stockName = stock.getName();
    Label title = new Label(getActionLabel() + stockName);
    title.getStyleClass().add("trade-title");

    HBox amountBox = new HBox(20);
    amountBox.setAlignment(Pos.CENTER);

    amountField = new TextField();
    amountField.setPromptText("Enter amount");
    amountField.getStyleClass().add("form-input");

    BigDecimal price = stock.getCurrentPrice();
    Label statsLabel = new Label("Price per stock: " + price);
    statsLabel.getStyleClass().add("trade-stats");

    commission = new Label("Fees");
    tax = new Label("Tax");
    total = new Label("Total");
    commission.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;");
    tax.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;");
    total.getStyleClass().add("trade-total");

    amountError = new Label("");
    amountError.getStyleClass().add("form-error");

    confirmation = new Label("");

    amount = new Label("Amount: ");
    amount.getStyleClass().add("trade-label");

    initController(stock, exchange, player);

    Button amountButton = new Button("Select");
    amountButton.getStyleClass().add("nav-button");
    amountButton.setOnAction(e -> onAmountClicked(amountField.getText()));

    Button maxBtn = new Button("Max");
    maxBtn.getStyleClass().add("nav-button");
    maxBtn.setOnAction(e -> onMaxBtnClicked());

    amountBox.getChildren().addAll(amountField, amountButton, maxBtn);

    Button actionButton = new Button(getActionButtonText());
    actionButton.setStyle(getActionButtonStyle());
    actionButton.setPrefWidth(200);
    actionButton.setOnAction(e -> onActionClicked());

    Button closeBtn = new Button("Close");
    closeBtn.getStyleClass().add("trade-close");
    VBox popupBox = buildPopupBox();
    closeBtn.setOnAction(e -> {
      parent.getChildren().remove(popupBox);
      notifyAfterTrade();
    });

    mainContent.getChildren().addAll(
            title, amountBox, amountError, amount, statsLabel,
            commission, tax, total, actionButton, confirmation, closeBtn
    );

    StackPane contentWrapper = new StackPane();
    contentWrapper.getChildren().addAll(mainContent, balance);
    popupBox.getChildren().add(contentWrapper);
    StackPane.setAlignment(popupBox, Pos.CENTER);

    return popupBox;
  }

  /**
   * Initializes the concrete implementation's controller with the current trade context.
   *
   * @param stock    the stock involved in the transaction
   * @param exchange the exchange handling the transaction
   * @param player   the player performing the trade
   */
  protected abstract void initController(Stock stock, Exchange exchange, Player player);

  private static VBox buildPopupBox() {
    VBox popupBox = new VBox();
    popupBox.setAlignment(Pos.CENTER);
    popupBox.setPadding(new Insets(20));
    popupBox.setMaxSize(800, 500);
    popupBox.getStyleClass().add("trade-popup");

    return popupBox;
  }

  /**
   * Updates the displayed commission price label.
   *
   * @param price the commission price to display
   */
  public void commisionSetPrice(String price) {
    commission.setText("Commission: " + price);
  }

  /**
   * Updates the displayed tax price label.
   *
   * @param price the tax price to display
   */
  public void setTaxPrice(String price) {
    tax.setText("Tax: " + price);
  }

  /**
   * Displays an error message related to the input amount.
   *
   * @param message the error message to display
   */
  public void setAmountErrorMessage(String message) {
    amountError.setText(message);
  }

  /**
   * Displays an error message related to trade confirmation.
   *
   * @param message the confirmation error message to display
   */
  public void setConfirmationErrorMessage(String message) {
    confirmation.getStyleClass().removeAll("confirm-success");
    confirmation.getStyleClass().add("confirm-error");
    confirmation.setText(message);
  }

  /**
   * Displays a success message related to trade confirmation.
   *
   * @param message the confirmation success message to display
   */
  public void setConfirmationSuccessMessage(String message) {
    confirmation.getStyleClass().removeAll("confirm-error");
    confirmation.getStyleClass().add("confirm-success");
    confirmation.setText(message);
  }

  /**
   * Updates the displayed total cost or proceeds label.
   *
   * @param price the total price to display
   */
  public void setTotalPrice(String price) {
    total.setText("Total: " + price);
  }

  /**
   * Updates the displayed trade quantity amount label.
   *
   * @param text the amount text to display
   */
  public void setAmount(String text) {
    amount.setText("Amount: " + text);
  }

  /**
   * Updates the displayed user balance label.
   *
   * @param text the balance text to display
   */
  public void setBalance(String text) {
    balance.setText("Balance: " + text);
  }
}