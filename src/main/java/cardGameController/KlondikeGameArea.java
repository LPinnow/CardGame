package cardGameController;

import javafx.collections.FXCollections;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import cardGameView.CardPileView;
import cardGameView.CardTheme;
import cardGameView.CardView;

/**
 * This class represents the area where the game is taking place.
 */
public class KlondikeGameArea extends Pane {

  /**
   * The list of {@link CardView} objects that are on the playing area.
   */
  List<CardView> cardViewList = new ArrayList<>();

  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * player 1's hand pile.
   */
  private CardPileView p1_handPileView;
  
  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * player 2's hand pile.
   */
  private CardPileView p2_handPileView;
  
  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * the foundation piles.
   */
  private List<CardPileView> p1_foundationPileViews;
  
  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * the foundation piles.
   */
  private List<CardPileView> p2_foundationPileViews;

  /**
   * The {@link CardPileView} object that serves as the view for the stock.
   */
  private CardPileView stockView;

  /**
   * The {@link CardPileView} object that serves as the view for the waste.
   */
  private CardPileView wasteView;

  /**
   * The gap (offset) between the cards.
   */
  private double cardGapVertical = 30;

  /**
   * Constructs a new {@link KlondikeGameArea} object.
   */
  public KlondikeGameArea() {
    this.p1_handPileView = new CardPileView(0, 40, "K");
    this.p2_handPileView = new CardPileView(0, 40, "K"+1);
    this.p1_foundationPileViews = FXCollections.observableArrayList();
    this.p2_foundationPileViews = FXCollections.observableArrayList();
    this.stockView = new CardPileView(1, 0, "S");
    this.wasteView = new CardPileView(1, 0, "W");
    initGameArea();
  }

  /**
   * Constructs a new {@link KlondikeGameArea} object, with the given image
   * as the background.
   *
   * @param tableauBackground The {@link Image} object for the background.
   */
  public KlondikeGameArea(Image tableauBackground) {
    this();
    setTableauBackground(tableauBackground);
  }

  /**
   * Initializes the game area.
   */
  private void initGameArea() {
    buildStock(500, 275);
    buildWaste(650, 275);
    
    //pass in x,y locations for player 1
    buildFoundationPiles(50, 20, p1_foundationPileViews);
    
    //pass in x,y locations for player 2
    buildFoundationPiles(50, 550, p2_foundationPileViews);
    
    buildHandPiles(650, 20, p1_handPileView);
    buildHandPiles(650, 550, p2_handPileView);
  }

  /**
   * Configures the {@link CardPileView} object that serves as the view
   * of the stock.
   */
  private void buildStock(int x, int y) {
    BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    stockView.setPrefSize(130, 180);
    stockView.setBackground(background);
    stockView.setLayoutX(x);
    stockView.setLayoutY(y);
    stockView.setEffect(gaussianBlur);
    getChildren().add(stockView);
  }

  /**
   * Configures the {@link CardPileView} object that serves as the view
   * of the waste.
   */
  private void buildWaste(int x, int y) {
    BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    wasteView.setPrefSize(130, 180);
    wasteView.setBackground(background);
    wasteView.setLayoutX(x);
    wasteView.setLayoutY(y);
    wasteView.setEffect(gaussianBlur);
    getChildren().add(wasteView);
  }

  /**
   * Configures the {@link CardPileView} objects that serves as the view
   * of the foundation piles.
   */
  private void buildFoundationPiles(int x, int y, List<CardPileView> foundationPileViews) {
	 BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    IntStream.range(0, 3).forEach(i -> {
      foundationPileViews.add(new CardPileView(2, 0, "F" + i));
      foundationPileViews.get(i).setPrefSize(130, 180);
      foundationPileViews.get(i).setBackground(background);
      foundationPileViews.get(i).setLayoutX(x + i * 160);
      foundationPileViews.get(i).setLayoutY(y);
      foundationPileViews.get(i).setEffect(gaussianBlur);
      getChildren().add(foundationPileViews.get(i));
    });
  }

  /**
   * Configures the {@link CardPileView} objects that serves as the view
   * of the standard piles.
 * @param p1_handView 
 * @param y 
 * @param x 
   */
  private void buildHandPiles(int x, int y, CardPileView p1_handView) {
    BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    p1_handView.setPrefSize(130, 180);
    p1_handView.setBackground(background);
    p1_handView.setLayoutX(x);
    p1_handView.setLayoutY(y);
    p1_handView.setEffect(gaussianBlur);
    getChildren().add(p1_handView);
  }

  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of player 1's hand.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public CardPileView getP1_HandPileView() {
    return p1_handPileView;
  }

  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of player 1's hand.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public CardPileView getP2_HandPileView() {
    return p2_handPileView;
  }
  
  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of the foundation piles.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public List<CardPileView> getP1_FoundationPileViews() {
	return p1_foundationPileViews;
  }
  
  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of the foundation piles.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public List<CardPileView> getP2_FoundationPileViews() {
    return p2_foundationPileViews;
  }

  /**
   * Returns the {@link CardPileView} object that serves as the view
   * of the stock.
   *
   * @return The {@link CardPileView} object.
   */
  public CardPileView getStockView() {
    return stockView;
  }

  /**
   * Returns the {@link CardPileView} object that serves as the view
   * of the waste.
   *
   * @return The {@link CardPileView} object.
   */
  public CardPileView getWasteView() {
    return wasteView;
  }

  /**
   * Sets the background image for the tableau.
   *
   * @param tableauBackground The {@link Image} object to set.
   */
  public void setTableauBackground(Image tableauBackground) {
    setBackground(new Background(new BackgroundImage(tableauBackground,
        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
        BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
  }

  /**
   * Updates the card images of each card on the game area.
   * Mainly used when switching card themes.
   *
   * @param cardTheme The current card theme to update from.
   */
  public void updateCardViews(CardTheme cardTheme) {
    cardViewList.forEach(cardView -> {
      cardView.setFrontFace(cardTheme.getFrontFace(cardView.getShortID()));
      cardView.setBackFace(cardTheme.getBackFace());
    });
  }
}
