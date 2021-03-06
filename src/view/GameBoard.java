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

	/**
	 * List of playerhands per player(index)
	 */
	private List<List<CardPileView>> playerHands;

	/**
	 * Map of foundation pileview lists per player(key)
	 */
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
	
	/**
	 * Sort button for player 1
	 */
	private Button p1_sort;
	
	/**
	 * Sort button for player 2
	 */
	private Button p2_sort;

	/**
	 * All card pile views
	 */
	private List<CardPileView> allPileViews;

	/**
	 * List of cards currently selected
	 */
	private List<CardView> selection;

	/**
	 * Reset input manager
	 */
	private InputManager inputManager;

	/**
	 * Current game state represented by model data
	 */
	private IdiotGameStateFacade currentGameState;

	/**
	 * Constructs a new {@link DeCoupGameBoard} object.
	 */
	public GameBoard(ArrayList<CardPileView> piles) {
		int TOTAL_NUM_OF_PLAYERS = 2;
		
		playerHands = new ArrayList<List<CardPileView>>();
		foundationPiles = new HashMap<Integer, List<CardPileView>>();
		
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
		
		this.p1_sort = new Button("Sort Cards");
		this.p1_sort.setId("default-btn");
		
		this.p2_sort = new Button("Sort Cards");
		this.p2_sort.setId("default-btn");
		

		this.messageLabel = new Label("");
		this.messageLabel.setTextFill(Color.RED);

		allPileViews = new ArrayList<CardPileView>();
		selection = new ArrayList<CardView>();

		initGameArea(piles);
	}

	/**
	 * Add card view to selection
	 * @param cardView Card view to add to selection
	 */
	public void addToSelection(CardView cardView) {
		if (cardView == null) {
			return;
		}

		if (selection.contains(cardView)) {
			inputManager.slideToPosition(cardView,
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
					inputManager.slideToPosition(selectedCardView,
							selectedCardView.getLayoutX(),
							selectedCardView.getLayoutY() + 20);
				}
			}

			if (isDifferentCard) {
				selection.clear();
			}
		}
		selection.add(cardView);
		inputManager.slideToPosition(cardView, cardView.getLayoutX(),
				cardView.getLayoutY() - 20);

	}

	/**
	 * @return Selected cards
	 */
	public List<CardView> getSelected() {
		return selection;
	}

	/**
	 * Reset card selection y-offset and clear selection cards
	 * @param toDrag Selection being removed for drag event
	 */
	public void resetSelection(boolean toDrag) {
		for (CardView selectedCardView : selection) {
			if (toDrag) {
				selectedCardView
						.setLayoutY((selectedCardView.getLayoutY() + 20));
			} else {
				inputManager.slideToPosition(selectedCardView,
						selectedCardView.getLayoutX(),
						selectedCardView.getLayoutY() + 20);
			}
		}
		selection.clear();
	}

	/**
	 * Remove y offset for a selected card.
	 */
	public void removeOffsetSelection() {
		for (CardView selectedCardView : selection) {
			selectedCardView.setLayoutY((selectedCardView.getLayoutY() + 20));
		}

	}

	/**
	 * Clear selected cards
	 */
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
								.substring(1, 2)))
						.add(pileView);
				buildPile(pileView);
			} else if (pileView.getShortID().toLowerCase()
					.contains("foundation")) {
				foundationPiles.get(
						Integer.parseInt(pileView.getShortID().toLowerCase()
								.substring(1, 2)))
						.add(pileView);
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

		placeButton(p1_ready, 1000, 625);
		placeButton(p2_ready, 1000, 75);
		placeButton(p1_sort, 525, 650);
		
		placeButton(p2_sort, 525, 125);
		
		p1_sort.setOnMouseClicked(e-> {
			inputManager.sortPile(playerHands.get(1).get(0));
			e.consume();
		});
		
		p2_sort.setOnMouseClicked(e-> {
			inputManager.sortPile(playerHands.get(2).get(0));
			e.consume();
		});
		
		p2_ready.setVisible(false);
		p2_sort.setVisible(false);

		placeLabel(messageLabel, 50, 500, 14);
	}

	/**
	 * Build card pile view
	 * @param cardPileView CardPileView to build
	 */
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
	private void placeButton(Button button, int x, int y) {
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

	/**
	 * @param id id of Card View
	 * @return Card view represented by this card view
	 */
	CardView getCardViewById(String id) {
		for (CardView cardView : cardViewList) {
			if (cardView.getShortID().equals(id)) {
				return cardView;
			}
		}
		return null;
	}

	/**
	 * Gets nearest pile view to current card view (hypotenuse distance calculation)
	 * @param cardView Card view to check against pile view locations
	 * @return Nearest pile view
	 */
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

	/**
	 * @return P1 Ready Button
	 */
	public Button getP1_ReadyButton() {
		return p1_ready;
	}

	/**
	 * @return P2 Ready Button
	 */
	public Button getP2_ReadyButton() {
		return p2_ready;
	}

	/**
	 * Set button's visibility
	 * @param b Button
	 * @param visible visibility status
	 */
	public void setButtonVisibility(Button b, boolean visible) {
		b.setVisible(visible);
	}

	/**
	 * Set input manager
	 * @param inputManager Input manager for processing input
	 */
	public void setInputManager(InputManager inputManager) {
		this.inputManager = inputManager;
	}

	/**
	 * Manually push current game state
	 * @param currentGameState Current game state
	 */
	public void updateCurrentState(IdiotGameStateFacade currentGameState) {
		this.currentGameState = currentGameState;
	}

	/**
	 * Set message label text
	 * @param text Text for message label
	 */
	public void setMessageLabelText(String text) {
		messageLabel.setText(text);
	}

	/**
	 * Draws the deck of cards
	 * @param drawCards
	 */
	public void drawDeck() {
		Iterator<Card> deckIterator = currentGameState.GetFullDeck().iterator();

		deckIterator.forEachRemaining(card -> {
			getDeckView().addCardView(CardViewFactory.createCardView(card));
			inputManager.restack(getDeckView());
			cardViewList.add(getDeckView().getTopCardView());
			getChildren().add(getDeckView().getTopCardView());
			inputManager.makeClickable(getDeckView().getTopCardView());
		});
		deckView.getTopCardView().setMouseTransparent(false);
	}

	/**
	 * Called at the start of the game to draw initialized board state
	 */
	public void setupGameBoard() {
		for (int i = 1; i < playerHands.size(); i++) {
			CardPileView foundationPileView_1 = foundationPiles.get(i).get(0);
			CardPileView foundationPileView_2 = foundationPiles.get(i).get(1);
			CardPileView foundationPileView_3 = foundationPiles.get(i).get(2);
			CardPileView handPileView = playerHands.get(i).get(0);

			createPile(foundationPileView_1, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards1(), 300 * i);
			createPile(foundationPileView_2, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards2(), 600 * i);
			createPile(foundationPileView_3, currentGameState
					.getPlayerPlaces().get(i - 1).getAllTableCards3(), 900 * i);
			createPile(handPileView, currentGameState.getPlayerPlaces()
					.get(i - 1).getHand().getCards(), 1200 * i);

			setupFoundation(foundationPileView_1, handPileView.getCards()
					.size(), false);
			setupFoundation(foundationPileView_2, handPileView.getCards()
					.size(), false);
			setupFoundation(foundationPileView_3, handPileView.getCards()
					.size(), false);

			setupHand(handPileView);

		}
		setupWaste(getWasteView());
	}

	/**
	 * Remove pile view (fades out cards)
	 * @param pile Pile to remove
	 */
	private void removePileViewCards(CardPileView pile) {
		for (CardView cardView : pile) {
			inputManager.fadeOutAndRemove(cardView);
		}
		pile.clearContents();
	}

	/**
	 * Creates a new pile
	 * @param pile Pile view
	 * @param cards Model cards that the pile view represents
	 * @param dealDelay Delay in deal (animation duration)
	 */
	private void createPile(CardPileView pile, List<Card> cards, int dealDelay) {
		for (Card card : cards) {
			pile.addCardView(CardViewFactory
					.createCardView(card));
			inputManager.restack(pile);
			getChildren().add(pile.getTopCardView());
			cardViewList.add(pile.getTopCardView());
			inputManager.slideFromDeck(pile.getTopCardView(), dealDelay);

		}

	}

	/**
	 * Set up foundation pile
	 * @param foundationPileView Foundation Pile View
	 * @param handSize Size of hand for corresponding player's hand
	 * @param allFoundationCardsVisible Specifies whether or not to show foundation cards
	 */
	private void setupFoundation(CardPileView foundationPileView, int handSize, boolean allFoundationCardsVisible) {
		int maxIndex = foundationPileView.getCards().size() - 1;
		for (CardView cardView : foundationPileView.getCards()) {
			if (handSize == 0 && foundationPileView.getCards().size() > 1) {
				inputManager.makeDraggable(foundationPileView.getCards().get(maxIndex));
			} else {
				inputManager.removeDraggable(cardView);
			}
			if (allFoundationCardsVisible && handSize == 0) {
				cardView.setToFaceUp();
				inputManager.makeDraggable(cardView);
			}
			cardView.setMouseTransparent(false);
		}

	}

	/**
	 * Set up hand pile view
	 * @param handPileView Pile view for hand
	 */
	private void setupHand(CardPileView handPileView) {
		for (CardView cardView : handPileView.getCards()) {
			inputManager.makeDraggable(cardView);
			inputManager.makeClickable(cardView);
			cardView.setToFaceUp();
			cardView.setMouseTransparent(false);
		}
	}

	/**
	 * Set up waste pile view
	 * @param wastePileView Waste pile view
	 */
	private void setupWaste(CardPileView wastePileView) {
		for (CardView cardView : wastePileView.getCards()) {
			inputManager.removeDraggable(cardView);
			inputManager.makeClickable(cardView);
			cardView.setToFaceUp();
			cardView.setMouseTransparent(false);
		}
	}

	/**
	 * @param cardPileView Pile view
	 * @param cards Corresponding model data cards in pile
	 */
	private void checkForAdditions(CardPileView cardPileView, List<Card> cards) {
		List<CardView> cardViews = cardPileView.getCards();

		for (Card card : cards) {
			CardView tempView = getCardViewById(card.getId());
			if (tempView == null) {
				System.out.println("Could not find " + card.getId());
			}
			if (!cardViews.contains(tempView) && tempView != null) {
				inputManager.slideToPile(tempView,
						tempView.getContainingPile(), cardPileView, false);
			}
		}
	}

	/**
	 * @param wastePileView Waste pile view
	 * @param cards Model view representation of Waste pile view
	 */
	private void checkForWasteChanges(CardPileView wastePileView,
			List<Card> cards) {
		List<CardView> cardViews = wastePileView.getCards();

		if (!cardViews.isEmpty() && cards.isEmpty()) {
			removePileViewCards(wastePileView);
		}

	}

	/**
	 * Update game board to reflect any updates to model data.
	 */
	public void updateGameBoard() {

		CardPileView foundationPileView_1;
		CardPileView foundationPileView_2;
		CardPileView foundationPileView_3;
		CardPileView handPileView;

		boolean allTableCardsVisible;

		for (int i = 1; i < playerHands.size(); i++) {
			allTableCardsVisible = false;
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

			if (foundationPileView_1.getCards().size() <= 1 && foundationPileView_2.getCards().size() <= 1
					&& foundationPileView_3.getCards().size() <= 1) {
				allTableCardsVisible = true;
			}

			setupFoundation(foundationPileView_1, handPileView.getCards()
					.size(), allTableCardsVisible);
			setupFoundation(foundationPileView_2, handPileView.getCards()
					.size(), allTableCardsVisible);
			setupFoundation(foundationPileView_3, handPileView.getCards()
					.size(), allTableCardsVisible);

			setupHand(handPileView);

		}
		checkForAdditions(getWasteView(), currentGameState.GetPile().getCards());
		checkForWasteChanges(getWasteView(), currentGameState.GetPile()
				.getCards());

		setupWaste(getWasteView());

	}

	/**
	 * Deactivates current player's cards and activates the other player's
	 * cards. 
	 */
	public void setActivePlayer(int activePlayerNumber) {
		resetSelection(false);
		if (activePlayerNumber == 1) {
			p1_sort.setVisible(true);
			p2_sort.setVisible(false);
			for (CardView cardView : playerHands.get(1).get(0)) {
				inputManager.makeDraggable(cardView);
				cardView.setToFaceUp();
			}

			for (CardView cardView : playerHands.get(2).get(0)) {
				inputManager.removeDraggable(cardView);
				cardView.setToFaceDown();
			}

			boolean setFoundationCardsUp = false;
			int count = 0;
			for (CardPileView pile : foundationPiles.get(1)) {
				if (pile.getCards().size() <= 1) {
					count++;
				}

				if (count == foundationPiles.get(1).size()) {
					setFoundationCardsUp = true;
				}
			}
			for (CardPileView pile : foundationPiles.get(1)) {
				if (!pile.getCards().isEmpty()) {
					if (setFoundationCardsUp) {
						pile.getCards().get(pile.getCards().size() - 1).setToFaceUp();
					}
				}
			}

			for (CardPileView pile : foundationPiles.get(2)) {
				if (!pile.getCards().isEmpty()) {
					if (pile.getCards().size() != 1) {
						pile.getCards().get(pile.getCards().size() - 1).setToFaceUp();
					} else {
						pile.getCards().get(pile.getCards().size() - 1).setToFaceDown();
					}
				}
			}

		} else if (activePlayerNumber == 2) {
			p1_sort.setVisible(false);
			p2_sort.setVisible(true);
			for (CardView cardView : playerHands.get(2).get(0)) {
				inputManager.makeDraggable(cardView);
				cardView.setToFaceUp();
			}

			for (CardView cardView : playerHands.get(1).get(0)) {
				inputManager.removeDraggable(cardView);
				cardView.setToFaceDown();
			}

			boolean setFoundationCardsUp = false;
			int count = 0;
			for (CardPileView pile : foundationPiles.get(2)) {
				if (pile.getCards().size() <= 1) {
					count++;
				}

				if (count == foundationPiles.get(1).size()) {
					setFoundationCardsUp = true;
				}
			}

			for (CardPileView pile : foundationPiles.get(1)) {
				if (!pile.getCards().isEmpty()) {
					if (pile.getCards().size() != 1) {
						pile.getCards().get(pile.getCards().size() - 1).setToFaceUp();
					} else {
						pile.getCards().get(pile.getCards().size() - 1).setToFaceDown();
					}
				}
			}

			for (CardPileView pile : foundationPiles.get(2)) {
				if (!pile.getCards().isEmpty()) {
					if (setFoundationCardsUp) {
						pile.getCards().get(pile.getCards().size() - 1).setToFaceUp();
					}
				}
			}
		}
	}

	/**
	 * Changes player turn based on current player info submitted
	 * @param currentPlayer Current players turn
	 */
	public void changePlayerTurn(int currentPlayer) {
		if (currentPlayer == 1) {
			setActivePlayer(2);
		} else {
			setActivePlayer(1);
		}
	}
}
