package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import model.IdiotGameState;
import model.IdiotGameStateFacade;
import model.card.Card;

/**
 * This class represents the area where the game is taking place.
 * 
 * This class has code based upon the following project: Zoltan Dalmadi,
 * "JCardGamesFX", 2015, GitHub repository,
 * github.com/ZoltanDalmadi/JCardGamesFX.
 */
public class GameBoard extends Pane {

	/**
	 * The list of {@link CardView} objects that are on the playing area.
	 */
	List<CardView> cardViewList = new ArrayList<>();

	private List<List<CardPileView>> playerHands;

	private Map<Integer, List<CardPileView>> foundationPiles;

	/**
	 * The {@link CardPileView} object that serves as the view for the stock.
	 */
	private CardPileView deckView;

	/**
	 * The {@link CardPileView} object that serves as the view for the pile.
	 */
	private CardPileView wasteView;

	/**
	 * Represents string of player 1's name
	 */
	private Label p1_name;

	/**
	 * Represents string of player 2's name
	 */
	private Label p2_name;

	private Label messageLabel;

	/**
	 * Ready button for player 1
	 */
	private Button p1_ready;

	/**
	 * Ready button for player 2
	 */
	private Button p2_ready;

	private List<CardPileView> allPileViews;

	private List<CardView> selection;

	private InputManager mouseUtility;

	private IdiotGameStateFacade currentGameState;

	/**
	 * Constructs a new {@link DeCoupGameBoard} object.
	 */
	public GameBoard(ArrayList<CardPileView> piles) {
		playerHands = new ArrayList<List<CardPileView>>();
		foundationPiles = new HashMap<Integer, List<CardPileView>>();
		int TOTAL_NUM_OF_PLAYERS = 2;
		// feed game rules # of players in
		playerHands.add(0, new ArrayList<CardPileView>());
		for (int i = 1; i < TOTAL_NUM_OF_PLAYERS + 1; i++) {
			foundationPiles.put(i, FXCollections.observableArrayList());
			playerHands.add(i, new ArrayList<CardPileView>());
		}
		this.p1_ready = new Button("Player 1 Ready?");
		this.p1_ready.setId("default-btn");
		this.p2_ready = new Button("Player 2 Ready?");
		this.p2_ready.setId("default-btn");

		this.messageLabel = new Label("");
		this.messageLabel.setTextFill(Color.RED);

		allPileViews = new ArrayList<CardPileView>();
		selection = new ArrayList<CardView>();

		initGameArea(piles);
	}

	public void addToSelection(CardView cardView) {
		if (cardView == null) {
			return;
		}

		if (selection.contains(cardView)) {
			mouseUtility.slideToPosition(cardView,
					cardView.getLayoutX(), cardView.getLayoutY() + 20);
			selection.remove(cardView);
			return;
		}

		if (selection.size() > 0) {
			boolean isDifferentCard = false;
			for (CardView selectedCardView : selection) {
				if (selectedCardView.asGameCard().getRank() != cardView
						.asGameCard().getRank()) {
					isDifferentCard = true;
					mouseUtility.slideToPosition(selectedCardView,
							selectedCardView.getLayoutX(),
							selectedCardView.getLayoutY() + 20);
				}
			}

			if (isDifferentCard) {
				selection.clear();
			}
		}
		selection.add(cardView);
		mouseUtility.slideToPosition(cardView, cardView.getLayoutX(),
				cardView.getLayoutY() - 20);

	}

	public List<CardView> getSelected() {
		return selection;
	}

	public void resetSelection(boolean toDrag) {
		for (CardView selectedCardView : selection) {
			if (toDrag) {
				selectedCardView
						.setLayoutY((selectedCardView.getLayoutY() + 20));
			} else {
				mouseUtility.slideToPosition(selectedCardView,
						selectedCardView.getLayoutX(),
						selectedCardView.getLayoutY() + 20);
			}
		}
		selection.clear();
	}

	public void removeOffsetSelection() {
		for (CardView selectedCardView : selection) {
			selectedCardView.setLayoutY((selectedCardView.getLayoutY() + 20));
		}

	}

	public void clearSelection() {
		selection.clear();
	}

	/**
	 * Constructs a new {@link DeCoupGameBoard} object, with the given image as
	 * the background.
	 *
	 * @param tableauBackground
	 *            The {@link Image} object for the background.
	 */
	public GameBoard(ArrayList<CardPileView> piles, Image tableBackground) {
		this(piles);
		setTableBackground(tableBackground);
	}

	/**
	 * Initializes the game area. Calls methods to build deck, waste, hand, and
	 * card pile views.
	 */
	private void initGameArea(ArrayList<CardPileView> piles) {
		this.p1_name = new Label("Player 1");
		this.p2_name = new Label("Player 2");

		placeLabel(p1_name, 525, 600, 18);
		placeLabel(p2_name, 525, 75, 18);

		for (CardPileView pileView : piles) {
			if (pileView.getShortID().toLowerCase().contains("hand")) {
				playerHands.get(
						Integer.parseInt(pileView.getShortID().toLowerCase()
								.substring(1, 2))).add(pileView);
				buildPile(pileView);
			} else if (pileView.getShortID().toLowerCase()
					.contains("foundation")) {
				foundationPiles.get(
						Integer.parseInt(pileView.getShortID().toLowerCase()
								.substring(1, 2))).add(pileView);
				buildPile(pileView);
			} else if (pileView.getShortID().toLowerCase().contains("deck")) {
				this.deckView = pileView;
				buildPile(deckView);
			} else if (pileView.getShortID().toLowerCase().contains("waste")) {
				this.wasteView = pileView;
				buildPile(wasteView);
			}

			allPileViews.add(pileView);
		}

		placeReadyButton(p1_ready, 1000, 625);
		placeReadyButton(p2_ready, 1000, 75);
		p2_ready.setVisible(false);

		placeLabel(messageLabel, 50, 500, 14);
	}

	private void buildPile(CardPileView cardPileView) {
		BackgroundFill backgroundFill = new BackgroundFill(
				Color.gray(0.0, 0.2), null, null);

		Background background = new Background(backgroundFill);

		GaussianBlur gaussianBlur = new GaussianBlur(10);

		cardPileView.setPrefSize(130, 180);
		cardPileView.setBackground(background);
		cardPileView.setLayoutX(cardPileView.getInitialX());
		cardPileView.setLayoutY(cardPileView.getInitialY());
		cardPileView.setEffect(gaussianBlur);
		getChildren().add(cardPileView);
	}

	private void placeLabel(Label label, int x, int y, int fontSize) {
		label.setTranslateX(x);
		label.setTranslateY(y);
		label.setStyle("-fx-font-size: " + fontSize
				+ "pt; -fx-font-weight: bold;");
		getChildren().add(label);
	}

	/**
	 * Place ready button
	 * 
	 * @param button
	 * @param x
	 * @param y
	 */
	private void placeReadyButton(Button button, int x, int y) {
		button.setTranslateX(x);
		button.setTranslateY(y);
		getChildren().add(button);
	}

	public CardPileView getPlayerHandView(int playerNumber) {
		return playerHands.get(playerNumber).get(0);
	}

	public List<CardPileView> getFoundationPileViews(int playerNumber) {
		return foundationPiles.get(playerNumber);
	}

	/**
	 * Returns the {@link CardPileView} object that serves as the view of the
	 * draw cards pile.
	 *
	 * @return The {@link CardPileView} object.
	 */
	public CardPileView getDeckView() {
		return deckView;
	}

	/**
	 * Returns the {@link CardPileView} object that serves as the view of the
	 * waste.
	 *
	 * @return The {@link CardPileView} object.
	 */
	public CardPileView getWasteView() {
		return wasteView;
	}

	/**
	 * Sets the background image for the table.
	 *
	 * @param tableBackground
	 *            The {@link Image} object to set.
	 */
	public void setTableBackground(Image tableBackground) {
		setBackground(new Background(new BackgroundImage(tableBackground,
				BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
	}

	/**
	 * Updates the card images of each card on the game area. Mainly used when
	 * switching card themes.
	 *
	 * @param cardTheme
	 *            The current card theme to update from.
	 */
	public void updateCardViews(CardTheme cardTheme) {
		cardViewList
				.forEach(cardView -> {
					cardView.setFrontFace(cardTheme.getFrontFace(cardView
							.getShortID()));
					cardView.setBackFace(cardTheme.getBackFace());
					/* TODO: solve update issue for theme files */

				});
	}

	CardView getCardViewById(String id) {
		for (CardView cardView : cardViewList) {
			if (cardView.getShortID().equals(id)) {
				return cardView;
			}
		}
		return null;
	}

	public CardPileView getNearestPile(CardView cardView) {
		double cardWidth = cardView.getFitWidth();
		double cardHeight = cardView.getFitHeight();

		double viewX = cardView.getLayoutX() + cardView.getTranslateX()
				+ (cardWidth / 2);
		double viewY = cardView.getLayoutY() + cardView.getTranslateY()
				+ (cardHeight / 2);
		double diffX, diffY;
		double distance = 0, prevDistance = 0;
		int closestPileByIndex = 0;

		for (int i = 0; i < allPileViews.size(); i++) {
			CardPileView currentPile = allPileViews.get(i);

			// Don't check pile against itself
			if (currentPile == cardView.getContainingPile()) {
				continue;
			}
			double cardPileWidth = (currentPile.getCards().size() * cardWidth)
					+ (currentPile.getCards().size() - 2 * currentPile
							.getCardGapHorizontal());
			double cardPileHeight = (currentPile.getCards().size() * cardHeight)
					+ (currentPile.getCards().size() - 2 * currentPile
							.getCardGapVertical());

			diffX = viewX - (currentPile.getLayoutX() + cardPileWidth / 2);
			diffY = viewY - (currentPile.getLayoutY() + cardPileHeight / 2);

			distance = Math.hypot(diffX, diffY);
			if (i == 0 || distance < prevDistance) {
				prevDistance = distance;
				closestPileByIndex = i;
			}
		}
		return allPileViews.get(closestPileByIndex);
	}

	public Button getP1_ReadyButton() {
		return p1_ready;
	}

	public Button getP2_ReadyButton() {
		return p2_ready;
	}

	public void setButtonVisibility(Button b, boolean visible) {
		b.setVisible(visible);
	}

	public void setInputManager(InputManager mouseUtility) {
		this.mouseUtility = mouseUtility;
	}

	public void updateCurrentState(IdiotGameStateFacade currentGameState) {
		this.currentGameState = currentGameState;
	}

	public void setMessageLabelText(String text) {
		messageLabel.setText(text);
	}

	/**
	 * Draws the deck of cards
	 * 
	 * @param drawCards
	 */
	public void drawDeck() {
		Iterator<Card> deckIterator = currentGameState.GetFullDeck().iterator();

		deckIterator.forEachRemaining(card -> {
			getDeckView().addCardView(CardViewFactory.createCardView(card));
			cardViewList.add(getDeckView().getTopCardView());
			getChildren().add(getDeckView().getTopCardView());
			mouseUtility.makeClickable(getDeckView().getTopCardView());
		});
		deckView.getTopCardView().setMouseTransparent(false);
	}

	/**
	 * Called at the start of the game to draw initialized board state
	 * 
	 * @param playerPlace
	 */
	public void setupGameBoard() {
		for (int i = 1; i < playerHands.size(); i++) {
			CardPileView foundationPileView_1 = foundationPiles.get(i).get(0);
			CardPileView foundationPileView_2 = foundationPiles.get(i).get(1);
			CardPileView foundationPileView_3 = foundationPiles.get(i).get(2);
			CardPileView handPileView = playerHands.get(i).get(0);

			createPile(foundationPileView_1, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards1(), 300*i);
			createPile(foundationPileView_2, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards2(), 600*i);
			createPile(foundationPileView_3, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards3(), 900*i);
			createPile(handPileView, currentGameState.getPlayerPlaces()
					.get(i - 1).getHand().getCards(), 1200*i);

			setupFoundation(foundationPileView_1, handPileView.getCards()
					.size());
			setupFoundation(foundationPileView_2, handPileView.getCards()
					.size());
			setupFoundation(foundationPileView_3, handPileView.getCards()
					.size());

			setupHand(handPileView);

		}		
		setupWaste(getWasteView());
	}

	private void removePileViewCards(CardPileView pile) {
		for (CardView cardView : pile) {
			mouseUtility.fadeOutAndRemove(cardView);
		}
		pile.clearContents();
	}

	private void createPile(CardPileView pile, List<Card> cards, int dealDelay) {
		for (Card card : cards) {
			pile.addCardView(CardViewFactory
					.createCardView(card));
			getChildren().add(pile.getTopCardView());
			cardViewList.add(pile.getTopCardView());
			mouseUtility.slideFromDeck(pile.getTopCardView(), dealDelay);
			
		}

	}

	private void setupFoundation(CardPileView foundationPileView, int handSize) {
		for (CardView cardView : foundationPileView.getCards()) {
			if (handSize == 0) {
				mouseUtility.makeDraggable(cardView);
			}
			if (foundationPileView.getCards().size() == 1) {
				/*TODO Only set face up when the top card of all foundation piles has been played (potentially reference game engine face up status */
				mouseUtility.makeDraggable(cardView);
				cardView.setToFaceUp();
			}
			
			cardView.setMouseTransparent(false);
		}
	}

	private void setupHand(CardPileView handPileView) {
		for (CardView cardView : handPileView.getCards()) {
			mouseUtility.makeDraggable(cardView);
			mouseUtility.makeClickable(cardView);
			cardView.setToFaceUp();
			cardView.setMouseTransparent(false);
		}
	}

	private void setupWaste(CardPileView wastePileView) {
		for (CardView cardView : wastePileView.getCards()) {
			mouseUtility.removeDraggable(cardView);
			mouseUtility.makeClickable(cardView);
			cardView.setToFaceUp();
			cardView.setMouseTransparent(false);
		}
	}

	private void checkForAdditions(CardPileView cardPileView, List<Card> cards) {
		List<CardView> cardViews = cardPileView.getCards();

		for (Card card : cards) {
			CardView tempView = getCardViewById(card.getId());
			if(tempView == null) {
				System.out.println("Could not find " + card.getId());
			}
			if (!cardViews.contains(tempView) && tempView != null) {
				mouseUtility.slideToPile(tempView,
						tempView.getContainingPile(), cardPileView, false);
			}
		}
	}

	private void checkForWasteChanges(CardPileView wastePileView,
			List<Card> cards) {
		List<CardView> cardViews = wastePileView.getCards();

		if (!cardViews.isEmpty() && cards.isEmpty()) {
			removePileViewCards(wastePileView);
		}

	}

	public void updateGameBoard() {
		
		CardPileView foundationPileView_1;
		CardPileView foundationPileView_2;
		CardPileView foundationPileView_3;
		CardPileView handPileView;

		for (int i = 1; i < playerHands.size(); i++) {
			foundationPileView_1 = foundationPiles.get(i).get(0);
			foundationPileView_2 = foundationPiles.get(i).get(1);
			foundationPileView_3 = foundationPiles.get(i).get(2);
			handPileView = playerHands.get(i).get(0);

			checkForAdditions(foundationPileView_1, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards1());
			checkForAdditions(foundationPileView_2, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards2());
			checkForAdditions(foundationPileView_3, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards3());
			checkForAdditions(handPileView, currentGameState.getPlayerPlaces()
					.get(i - 1).getHand().getCards());			

			setupFoundation(foundationPileView_1, handPileView.getCards()
					.size());
			setupFoundation(foundationPileView_2, handPileView.getCards()
					.size());
			setupFoundation(foundationPileView_3, handPileView.getCards()
					.size());

			setupHand(handPileView);

		}
		checkForAdditions(getWasteView(), currentGameState.GetPile().getCards());
		checkForWasteChanges(getWasteView(), currentGameState.GetPile()
				.getCards());
		
		setupWaste(getWasteView());	
		
	}

	/**
	 * Deactivates current player's cards and activates the other player's
	 * cards. Changes drag event status.
	 */
	public void setActivePlayer(int activePlayerNumber) {
		resetSelection(false);
		if (activePlayerNumber == 1) {
			for (CardView cardView : playerHands.get(1).get(0)) {
				mouseUtility.makeDraggable(cardView);
				cardView.setToFaceUp();
			}

			for (CardView cardView : playerHands.get(2).get(0)) {
				mouseUtility.removeDraggable(cardView);
				cardView.setToFaceDown();
			}

		} else if (activePlayerNumber == 2) {
			for (CardView cardView : playerHands.get(2).get(0)) {
				mouseUtility.makeDraggable(cardView);
				cardView.setToFaceUp();
			}

			for (CardView cardView : playerHands.get(1).get(0)) {
				mouseUtility.removeDraggable(cardView);
				cardView.setToFaceDown();
			}
		}
	}

	public void changePlayerTurn(int currentPlayer) {
		if (currentPlayer == 1) {
			setActivePlayer(2);
		} else {
			setActivePlayer(1);
		}

	}
}
