package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controller.CardGame;
import controller.IIdiotGameEngine;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import model.IdiotGameState;
import model.IdiotGameState.GamePhases;
import model.card.Card;
import model.move.MoveResult;
import model.move.PlayMultipleCardsMove;
import model.move.PlayOneCardMove;
import model.move.PlayTopOfDeck;
import model.move.TakePileMove;

/**
 * This class serves as the controller for the application.
 * 
 * This class has code based upon the following project: Zoltan Dalmadi,
 * "JCardGamesFX", 2015, GitHub repository,
 * github.com/ZoltanDalmadi/JCardGamesFX.
 */
public class InputManager {

	/**
	 * Helper inner class for determining the position of the mouse.
	 */
	private final MousePos mousePos = new MousePos();

	/**
	 * Dragged cards will be put into this list.
	 */
	private Card draggedCard;

	/**
	 * Same for the view of the cards.
	 */
	private CardView draggedCardView;

	/**
	 * Reference to the game status bar.
	 */
	private StatusBar statusBar;

	/**
	 * Boolean signifying a just dragged event.
	 */
	private boolean justDragged;

	/**
	 * The {@link IIdiotGameEngine} object to manipulate.
	 */
	private IIdiotGameEngine game;

	/**
	 * The {@link GameBoard} object to manipulate.
	 */
	private GameBoard gameBoard;

	/**
	 * This event handler is attached to cards that are still on the stock. When
	 * the user clicks on a card, it will be flipped and put on the waste.
	 */
	EventHandler<MouseEvent> onMouseClickedHandler = e -> {

		// Ignore resulting click event generated after drag
		if (justDragged) {
			justDragged = false;
			e.consume();
			return;
		}

		// Ignore clicks after game is over.
		if (game.getCurrentGameState().CurrentGamePhase() == GamePhases.GameCompleted) {
			draggedCard = null;
			draggedCardView = null;
			e.consume();
			return;
		}

		/** get current cardView. */
		CardView cardView = (CardView) e.getSource();

		/** get current card. */
		Card card = cardView.asGameCard();

		int currentPlayer = game.getCurrentGameState()
				.CurrentPlayerTurn();

		// handle right click actions
		if (e.getButton() == MouseButton.SECONDARY) {
			if (cardView.getContainingPile().equals(
					gameBoard.getPlayerHandView(currentPlayer))) {
				gameBoard.addToSelection(cardView);
			}

		} else if (e.getButton() == MouseButton.PRIMARY) { // handle left click
															// actions

			// determine move type
			if (cardView.getContainingPile().equals(gameBoard.getDeckView())) {
				MoveResult playDeckResult = game.submitMove(game
						.getCurrentGameState()
						.CurrentPlayerTurn(), new PlayTopOfDeck(card.getId(),
								game
										.getCurrentGameState().GetPile().toString()));
				if (playDeckResult.success) {
					gameBoard.setMessageLabelText("");
					cardView.setToFaceUp();
					slideToPile(cardView, gameBoard.getDeckView(),
							gameBoard.getWasteView(), true);

					gameBoard.updateGameBoard();
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());
					statusBar.setActivePlayerText("Player " + game.getCurrentGameState().CurrentPlayerTurn());

				} else {
					gameBoard
							.setMessageLabelText("Card cannot be placed on deck");
				}
			} else if (cardView.getContainingPile().equals(
					gameBoard.getWasteView())) {
				MoveResult playWasteResult = game.submitMove(game
						.getCurrentGameState()
						.CurrentPlayerTurn(), new TakePileMove(card.getId(),
								" "));
				if (playWasteResult.success) {
					gameBoard.setMessageLabelText("");
					slideToPile(cardView, gameBoard.getWasteView(),
							gameBoard.getPlayerHandView(currentPlayer), true);
					gameBoard.updateGameBoard();
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());
					statusBar.setActivePlayerText("Player " + game.getCurrentGameState().CurrentPlayerTurn());
				} else {
					gameBoard.setMessageLabelText("Cannot pick up cards");
				}
			}
		}

		e.consume();
	};

	/**
	 * This event handler is attached to all the cards that are not on the
	 * stock. Stores the position where the user clicked.
	 */
	EventHandler<MouseEvent> onMousePressedHandler = e -> {
		// Store mouse click position
		mousePos.x = e.getSceneX();
		mousePos.y = e.getSceneY();
		e.consume();
	};

	/**
	 * This event handler is attached to all the cards that are not on the
	 * stock. Handles the card movements, applies a drop shadow effect, and
	 * others.
	 */
	EventHandler<MouseEvent> onMouseDraggedHandler = e -> {

		// Ignore drag events generated by other buttons
		if (e.getButton() != MouseButton.PRIMARY) {
			e.consume();
			return;
		}

		// Ignore drag events after game is over.
		if (game.getCurrentGameState().CurrentGamePhase() == GamePhases.GameCompleted) {
			draggedCard = null;
			draggedCardView = null;
			e.consume();
			return;
		}

		// x component
		double offsetX = e.getSceneX() - mousePos.x;
		// y component
		double offsetY = e.getSceneY() - mousePos.y;

		// Get current cardView.
		CardView cardView = (CardView) e.getSource();

		// Get current card.
		Card card = game.getCurrentGameState().GetFullDeck()
				.getById(cardView.getShortID());

		draggedCardView = cardView;
		draggedCard = card;

		// Handles dragging coordinates
		draggedCardView.toFront();
		draggedCardView.setTranslateX(offsetX);
		draggedCardView.setTranslateY(offsetY);

		// Determine if more than one card is selected and if one of those cards
		// is the dragged card
		if (gameBoard.getSelected().size() > 1
				&& gameBoard.getSelected().contains(cardView)) {

			int count = 0;
			List<CardView> tempList = gameBoard.getSelected();
			for (int i = 0; i < tempList.size(); i++) {

				tempList.get(i).getDropShadow().setRadius(20);
				tempList.get(i).getDropShadow().setOffsetX(10);
				tempList.get(i).getDropShadow().setOffsetY(10);

				tempList.get(i).setTranslateX((draggedCardView.getLayoutX()
						- tempList.get(i).getLayoutX()) + offsetX
						+ (tempList.get(i).getContainingPile()
								.getCardGapHorizontal() * count));
				tempList.get(i).setTranslateY((draggedCardView.getLayoutY()
						- tempList.get(i).getLayoutY()) + offsetY
						+ (tempList.get(i).getContainingPile()
								.getCardGapVertical() * count));
				count++;
				tempList.get(i).toFront();

			}
		} else {
			gameBoard.resetSelection(true);
			// Setup drop shadow
			cardView.getDropShadow().setRadius(20);
			cardView.getDropShadow().setOffsetX(10);
			cardView.getDropShadow().setOffsetY(10);
		}

		e.consume();
	};

	/**
	 * This event handler is attached to all the cards that are not on the
	 * stock. Decides if the move is valid, and acts appropriately.
	 */
	EventHandler<MouseEvent> onMouseReleasedHandler = e -> {
		// If no cards are dragged, return immediately
		if (draggedCard == null && draggedCardView == null) {
			e.consume();
			return;
		}

		if (game.getCurrentGameState().CurrentGamePhase() == GamePhases.GameCompleted) {
			draggedCard = null;
			draggedCardView = null;
			e.consume();
			return;
		}

		// Get current card view
		CardView cardView = (CardView) e.getSource();
		// Get current card
		Card card = game.getCurrentGameState().GetFullDeck()
				.getById(cardView.getShortID());

		// List of card/cardviews to use if multiple cards are selected to be
		// played
		List<Card> cardsToPlay = new ArrayList<Card>();
		List<CardView> draggedViewList = new ArrayList<CardView>();

		if (gameBoard.getSelected().size() > 1
				&& gameBoard.getSelected().contains(cardView)) {
			for (CardView selectedCard : gameBoard.getSelected()) {
				draggedViewList.add(selectedCard);
				cardsToPlay.add(selectedCard.asGameCard());
			}

		}

		// Get the pile that contained the actual card
		// Get containing pile view
		CardPileView activePileView = cardView.getContainingPile();

		// Get cards respective pile
		List<Card> activePile = game.getPileById(activePileView.getShortID());

		for (Card c : activePile) {
			if (c.getId().equals(cardView.getShortID())) {
				card = c;
			}
		}

		// Check if card(s) are intersecting with nearest pile
		if (cardsToPlay.size() > 0) {
			if (checkPile(cardsToPlay, cardView, activePile, activePileView,
					gameBoard.getNearestPile(draggedCardView))) {
				draggedCard = null;
				draggedCardView = null;
				e.consume();
				return;
			}
		} else if (checkPile(card, cardView, activePile, activePileView, gameBoard.getNearestPile(draggedCardView))) {
			draggedCard = null;
			draggedCardView = null;
			e.consume();
			return;
		}

		// if not intersecting with any valid pile, slide them back
		if (draggedViewList.size() > 0) {
			slideSelection(draggedViewList, draggedCardView.getContainingPile());
			gameBoard.clearSelection();
		} else {
			slideBack(draggedCardView);
			gameBoard.clearSelection();
		}

		// Reorder CardViews
		for (CardView cv : activePileView) {
			cv.toFront();
		}

		// Throw away dragged cards info
		draggedCard = null;
		draggedCardView = null;
		e.consume();
	};

	/**
	 * Constructs a {@link InputManager} object for the given {@link CardGame}
	 * and {@link GameBoard} objects.
	 *
	 * @param game
	 *            The {@link CardGame object}.
	 * @param gameBoard
	 *            The {@link GameBoard} object.
	 */
	public InputManager(IIdiotGameEngine game, GameBoard gameBoard, StatusBar statusBar) {
		this.game = game;
		this.gameBoard = gameBoard;
		this.statusBar = statusBar;
	}

	/**
	 * Applies the appropriate event handlers for cards not on the stock.
	 *
	 * @param card
	 *            The {@link CardView} to apply event listeners for.
	 */
	public void makeDraggable(CardView card) {
		card.setOnMousePressed(onMousePressedHandler);
		card.setOnMouseDragged(onMouseDraggedHandler);
		card.setOnMouseReleased(onMouseReleasedHandler);
	}

	public void removeDraggable(CardView card) {
		card.setOnMousePressed(null);
		card.setOnMouseDragged(null);
		card.setOnMouseReleased(null);
	}

	/**
	 * Applies the appropriate event handlers for cards on the stock.
	 *
	 * @param card
	 *            The {@link CardView} to apply event listeners for.
	 */
	public void makeClickable(CardView card) {
		card.setOnMousePressed(null);
		card.setOnMouseReleased(null);
		card.setOnMouseClicked(onMouseClickedHandler);
	}

	/**
	 * Applies the appropriate event handlers for cards on the stock.
	 *
	 * @param card
	 *            The {@link CardView} to apply event listeners for.
	 */
	public void removeClickable(CardView card) {
		card.setOnMousePressed(null);
		card.setOnMouseDragged(null);
		card.setOnMouseReleased(null);
		card.setOnMouseClicked(null);
	}

	/**
	 * Check a pile for intersection with multiple cards to be played
	 * 
	 * @param cards Cards to play
	 * @param cardView Drag card view
	 * @param activePile Pile of corresponding game card
	 * @param activePileView Pile of corresponding game card view
	 * @param pileView  Destination pile for event
	 * @return result
	 */
	private boolean checkPile(List<Card> cards, CardView cardView,
			List<Card> activePile,
			CardPileView activePileView, CardPileView pileView) {

		boolean result = false;

		// skip checking the same pile
		if (pileView.equals(activePileView)) {
			result = false;
			return result;
		}

		if (isOverPile(cardView, pileView)) {
			// Determine how to process action based on current game state.
			if (game.getCurrentGameState().CurrentGamePhase()
					.equals(IdiotGameState.GamePhases.GamePlay) && pileView.getShortID().contains("waste")) {
				int currentPlayer = game.getCurrentGameState()
						.CurrentPlayerTurn();
				MoveResult playCards = game.submitMove(currentPlayer,
						new PlayMultipleCardsMove(cards.toString(), game
								.getCurrentGameState().GetPile().toString(),
								cards));
				if (playCards.success) {
					gameBoard.removeOffsetSelection();
					for (CardView selectedCardView : gameBoard.getSelected()) {
						slideToPile(selectedCardView, activePileView, pileView,
								false);
					}
					restack(activePileView);
					gameBoard.clearSelection();
					gameBoard.updateGameBoard();
					result = true;
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());
					statusBar.setActivePlayerText("Player " + game.getCurrentGameState().CurrentPlayerTurn());

					justDragged = true;
				} else {
					result = false;
					gameBoard
							.setMessageLabelText("Invalid PlayMultipleCardMove");
				}

			}
		}
		return result;

	}

	/**
	 * Check a single pile for intersection
	 * 
	 * @param card Card to play
	 * @param cardView Corresponding card view
	 * @param activePile Active game pile that the card belongs to
	 * @param activePileView Active game pile view that the card view belongs to
	 * @param pileView Destination pile for game event
	 * @return result
	 */
	private boolean checkPile(
			Card card, CardView cardView, List<Card> activePile,
			CardPileView activePileView, CardPileView pileView) {

		boolean result = false;

		// Place back in pile if over own home pile
		if (pileView.equals(activePileView)) {
			placeInPile(cardView);
			result = true;
			return result;
		}

		if (isOverPile(cardView, pileView)) {
			if (game.getCurrentGameState().CurrentGamePhase()
					.equals(IdiotGameState.GamePhases.CardSwapping)
					&& pileView.getShortID().contains("Foundation")) {
				MoveResult swapResult = game.requestHandToTableCardSwap(game
						.getCurrentGameState().CurrentPlayerTurn(), cardView
								.asGameCard(),
						pileView.getCards().get(pileView.getCards().size() - 1)
								.asGameCard());
				if (swapResult.success) {
					gameBoard.updateCurrentState(game.getCurrentGameState());
					CardView tablePileTopCardView = pileView.getTopCardView();

					slideToPile(cardView, activePileView, pileView, true);
					slideToPile(tablePileTopCardView, pileView, activePileView,
							true);
					makeDraggable(tablePileTopCardView);
					makeClickable(tablePileTopCardView);
					removeDraggable(cardView);
					removeClickable(cardView);

					gameBoard.setMessageLabelText("");
					result = true;
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());

				} else {
					result = false;
					gameBoard.setMessageLabelText(swapResult.message);
				}
			} else if (game.getCurrentGameState().CurrentGamePhase()
					.equals(IdiotGameState.GamePhases.GamePlay) && pileView.getShortID().contains("waste")) {
				int currentPlayer = game.getCurrentGameState()
						.CurrentPlayerTurn();
				MoveResult playCard = game.submitMove(currentPlayer,
						new PlayOneCardMove(card.getId(), game
								.getCurrentGameState().GetPile().toString(),
								card));
				if (playCard.success) {
					slideToPile(cardView, activePileView, pileView, true);
					gameBoard.updateGameBoard();
					result = true;
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());
					justDragged = true;
				} else {
					result = false;
					gameBoard.setMessageLabelText(playCard.getMessage());
				}

				if (playCard.isGameEnded()) {
					gameBoard.setMessageLabelText(playCard.getMessage());
				}
			}
		}

		statusBar.setActivePlayerText("Player " + game.getCurrentGameState().CurrentPlayerTurn());

		return result;
	}

	/**
	 * Checks if a cardView is over a pile.
	 *
	 * @param cardView
	 *            The cardView to check.
	 * @param pileView
	 *            The pile to check.
	 * @return true if the card is over the pile, false otherwise.
	 */
	private boolean isOverPile(CardView cardView, CardPileView pileView) {
		if (pileView.isEmpty()) {
			return cardView.getBoundsInParent().intersects(
					pileView.getBoundsInParent());
		} else {
			return cardView.getBoundsInParent().intersects(
					pileView.getTopCardView().getBoundsInParent());
		}
	}

	/**
	 * Slide back card to its original position if the move was not valid.
	 *
	 * @param card
	 *            The card view to slide back.
	 */
	private void slideBack(CardView card) {
		double sourceX = card.getLayoutX() + card.getTranslateX();
		double sourceY = card.getLayoutY() + card.getTranslateY();

		moveToEnd(card, card.getContainingPile());

		double targetX = card.getLayoutX();
		double targetY = card.getLayoutY();

		animateCardMovement(card, sourceX, sourceY,
				targetX, targetY, Duration.millis(400), e -> {
					card.getDropShadow().setRadius(2);
					card.getDropShadow().setOffsetX(0);
					card.getDropShadow().setOffsetY(0);
				});
	}

	/**
	 * Slide card from deck (Deal)
	 * 
	 * @param cardToSlide CardView to move
	 * @param ms Millisecond duration for animation
	 */
	public void slideFromDeck(CardView cardToSlide, int ms) {
		if (cardToSlide == null)
			return;

		double targetX, targetY, sourceX, sourceY;

		targetX = cardToSlide.getLayoutX();
		targetY = cardToSlide.getLayoutY();

		sourceX = gameBoard.getDeckView().getLayoutX();
		sourceY = gameBoard.getDeckView().getLayoutY();

		animateCardMovement(cardToSlide, sourceX, sourceY, targetX,
				targetY,
				Duration.millis(ms),
				e -> {
					cardToSlide.getDropShadow().setRadius(2);
					cardToSlide.getDropShadow().setOffsetX(0);
					cardToSlide.getDropShadow().setOffsetY(0);
				});
	}

	/**
	 * Moves a card to the end of a pile
	 * @param view CardView to move
	 * @param pile Pile to move cardview to the end of
	 */
	public void moveToEnd(CardView view, CardPileView pile) {
		pile.getCards().remove(view);
		pile.getCards().add(view);
		restack(pile);
	}

	/**
	 * Dynamically places a card view in a pile based on location of mouse release event
	 * @param card CardView to place in pile
	 */
	public void placeInPile(CardView card) {
		CardPileView pile = card.getContainingPile();
		List<CardView> cards = pile.getCards();
		int gapDivisor = 1;
		if (cards.size() > 10) {
			gapDivisor = 2;
		}
		double cardGapHorizontal = pile.getCardGapHorizontal() / gapDivisor;

		double cardX = card.getLayoutX() + card.getTranslateX();
		double stackSpanWidth = cardGapHorizontal * (cards.size() - 1);
		double placementRatio = (cardX - pile.getLayoutX()) / stackSpanWidth;

		int newIndex = (int) Math.rint(placementRatio * (cards.size() - 1));
		if (newIndex < 0) {
			newIndex = 0;
		} else if (newIndex > cards.size() - 1) {
			newIndex = cards.size() - 1;
		}

		cards.remove(card);
		cards.add(newIndex, card);

		slideInPile(card);
	}

	/**
	 * Slides a card view in a pile to its location (if not already at location)
	 * @param view CardView to slide
	 */
	public void slideInPile(CardView view) {
		double targetX, targetY, sourceX, sourceY;
		sourceX = view.getLayoutX() + view.getTranslateX();
		sourceY = view.getLayoutY() + view.getTranslateY();

		restack(view.getContainingPile());

		targetX = view.getLayoutX();
		targetY = view.getLayoutY();
		if (sourceX != targetX && sourceY != targetY) {
			animateCardMovement(view, sourceX, sourceY, targetX,
					targetY,
					Duration.millis(400),
					e -> {
						view.getDropShadow().setRadius(2);
						view.getDropShadow().setOffsetX(0);
						view.getDropShadow().setOffsetY(0);

					});
		} else {
			view.getDropShadow().setRadius(2);
			view.getDropShadow().setOffsetX(0);
			view.getDropShadow().setOffsetY(0);
		}
	}

	/**
	 * Slides a selection of card views to a designated pile
	 * @param cards Selection of card views
	 * @param pile Pile to slide selection to
	 */
	public void slideSelection(List<CardView> cards, CardPileView pile) {

		double[] sourceX = new double[cards.size()];
		double[] sourceY = new double[cards.size()];
		double[] targetX = new double[cards.size()];
		double[] targetY = new double[cards.size()];

		for (int i = 0; i < cards.size(); i++) {
			pile.getCards().remove(cards.get(i));
			pile.getCards().add(cards.get(i));
			sourceX[i] = cards.get(i).getLayoutX() + cards.get(i).getTranslateX();
			sourceY[i] = cards.get(i).getLayoutY() + cards.get(i).getTranslateY();

		}

		restack(pile);

		for (int i = 0; i < cards.size(); i++) {
			targetX[i] = cards.get(i).getLayoutX();
			targetY[i] = cards.get(i).getLayoutY();
		}

		for (int i = 0; i < cards.size(); i++) {
			final int tempIndex = i;

			// Only move items that need to be moved
			if (sourceX[tempIndex] == targetX[tempIndex] && sourceY[tempIndex] == targetY[tempIndex]) {
				continue;
			}
			animateCardMovement(cards.get(tempIndex), sourceX[tempIndex], sourceY[tempIndex], targetX[tempIndex],
					targetY[tempIndex],
					Duration.millis(400),
					e -> {
						cards.get(tempIndex).getDropShadow().setRadius(2);
						cards.get(tempIndex).getDropShadow().setOffsetX(0);
						cards.get(tempIndex).getDropShadow().setOffsetY(0);
						restack(pile);

					});
		}

	}

	/**
	 * Sort piles based on corresponding card data value
	 * @param pile Pile to sort
	 */
	public void slideSortPile(CardPileView pile) {
		List<CardView> cards = pile.getCards();

		double[] sourceX = new double[cards.size()];
		double[] sourceY = new double[cards.size()];
		double[] targetX = new double[cards.size()];
		double[] targetY = new double[cards.size()];

		for (int i = 0; i < cards.size(); i++) {
			sourceX[i] = cards.get(i).getLayoutX() + cards.get(i).getTranslateX();
			sourceY[i] = cards.get(i).getLayoutY() + cards.get(i).getTranslateY();
		}

		restack(pile);

		for (int i = 0; i < cards.size(); i++) {
			targetX[i] = cards.get(i).getLayoutX();
			targetY[i] = cards.get(i).getLayoutY();
		}

		for (int i = 0; i < cards.size(); i++) {
			final int tempIndex = i;

			// Only move items that need to be moved
			if (sourceX[tempIndex] == targetX[tempIndex] && sourceY[tempIndex] == targetY[tempIndex]) {
				continue;
			}
			animateCardMovement(cards.get(tempIndex), sourceX[tempIndex], sourceY[tempIndex], targetX[tempIndex],
					targetY[tempIndex],
					Duration.millis(400),
					e -> {
						cards.get(tempIndex).getDropShadow().setRadius(2);
						cards.get(tempIndex).getDropShadow().setOffsetX(0);
						cards.get(tempIndex).getDropShadow().setOffsetY(0);
						restack(pile);

					});
		}
	}

	/**
	 * Sorts designated pile view (Insertion sort)
	 * @param pile Pile to sort
	 */
	public void sortPile(CardPileView pile) {
		List<CardView> cards = pile.getCards();
		for (int i = 1; i < cards.size(); i++) {
			int j = i;
			while (j > 0 && cards.get(j - 1).asGameCard().getRank().ordinal() > cards.get(j).asGameCard().getRank()
					.ordinal()) {
				Collections.swap(cards, j, j - 1);
				j = j - 1;
			}
		}

		slideSortPile(pile);
	}

	/**
	 * Restack cards in a pile based on their locations
	 * @param pile Pile to restack
	 */
	public void restack(CardPileView pile) {
		double cardGapHorizontal = pile.getCardGapHorizontal();
		double cardGapVertical = pile.getCardGapVertical();
		List<CardView> cards = pile.getCards();
		if (cards.size() > 10) {
			cardGapHorizontal /= 2;
			cardGapVertical /= 2;
		}
		for (int i = 0; i < cards.size(); i++) {
			CardView tempView = cards.get(i);
			tempView.setTranslateX(0);
			tempView.setTranslateY(0);
			tempView.setLayoutX(pile.getLayoutX() + (i
					* cardGapHorizontal));
			tempView.setLayoutY(pile.getLayoutY() + (i * cardGapVertical));
			tempView.toFront();
		}
		if (cards.size() > 10) {
			cardGapHorizontal *= 2;
			cardGapVertical *= 2;
		}
	}

	/**
	 * Slides a card to specified location
	 * @param cardToSlide Card to slide
	 * @param newX X coordinate to slide to
	 * @param newY Y coordinate to slide to 
	 */
	public void slideToPosition(CardView cardToSlide, double newX, double newY) {
		if (cardToSlide == null)
			return;
		double targetX, targetY, sourceX, sourceY;

		targetX = newX;
		targetY = newY;

		sourceX = cardToSlide.getLayoutX() + cardToSlide.getTranslateX();
		sourceY = cardToSlide.getLayoutY() + cardToSlide.getTranslateY();

		cardToSlide.setLayoutX(newX);
		cardToSlide.setLayoutY(newY);

		animateCardMovement(cardToSlide, sourceX, sourceY, targetX,
				targetY,
				Duration.millis(400),
				e -> {
					cardToSlide.getDropShadow().setRadius(2);
					cardToSlide.getDropShadow().setOffsetX(0);
					cardToSlide.getDropShadow().setOffsetY(0);
				});

	}

	/**
	 * Slides the list of dragged cards from the source pile to the destination
	 * pile.
	 *
	 * @param cardsToSlide
	 *            The list of dragged cards.
	 * @param sourcePile
	 *            The source pile.
	 * @param destPile
	 *            The destination pile.
	 */
	public void slideToPile(CardView cardToSlide, CardPileView sourcePile,
			CardPileView destPile, boolean toRestack) {

		if (cardToSlide == null)
			return;

		double targetX;
		double targetY;

		CardView currentCardView = cardToSlide;
		double sourceX = currentCardView.getLayoutX() + currentCardView.getTranslateX();
		double sourceY = currentCardView.getLayoutY() + currentCardView.getTranslateY();
		if (toRestack) {
			sourcePile.moveCardViewToPile(currentCardView, destPile, true);
			restack(sourcePile);
		} else {
			sourcePile.moveCardViewToPile(currentCardView, destPile, false);
		}

		restack(destPile);
		targetX = cardToSlide.getLayoutX();
		targetY = cardToSlide.getLayoutY();

		animateCardMovement(currentCardView, sourceX, sourceY, targetX,
				targetY,
				Duration.millis(300),
				e -> {
					currentCardView.getDropShadow().setRadius(2);
					currentCardView.getDropShadow().setOffsetX(0);
					currentCardView.getDropShadow().setOffsetY(0);

				});
	}
	
	/**
	 * Fade out and remove a card view
	 * @param view card view to remove
	 */
	public void fadeOutAndRemove(CardView view) {
		final FadeTransition fadeout = new FadeTransition(new Duration(400));
		fadeout.setNode(view);
		fadeout.setToValue(0.0);
		fadeout.setOnFinished(e -> {
			gameBoard.getChildren().remove(view);
		});

		Timeline timeline = new Timeline(

				new KeyFrame(
						new Duration(400),
						e -> {
							fadeout.play();
						}));
		timeline.play();
	}

	/**
	 * Animates card movements.
	 *
	 * @param card
	 *            The card view to animate.
	 * @param sourceX
	 *            Source X coordinate of the card view.
	 * @param sourceY
	 *            Source Y coordinate of the card view.
	 * @param targetX
	 *            Destination X coordinate of the card view.
	 * @param targetY
	 *            Destination Y coordinate of the card view.
	 * @param duration
	 *            The duration of the animation.
	 * @param doAfter
	 *            The action to perform after the animation has been completed.
	 */
	private void animateCardMovement(
			CardView card, double sourceX, double sourceY,
			double targetX, double targetY, Duration duration,
			EventHandler<ActionEvent> doAfter) {

		Path path = new Path();
		path.getElements().add(new MoveToAbs(card, sourceX, sourceY));
		path.getElements().add(new LineToAbs(card, targetX, targetY));

		PathTransition pathTransition = new PathTransition(duration, path, card);
		pathTransition.setInterpolator(Interpolator.EASE_OUT);
		pathTransition.setOnFinished(doAfter);

		Timeline blurReset = new Timeline();
		KeyValue bx = new KeyValue(card.getDropShadow().offsetXProperty(), 0,
				Interpolator.EASE_OUT);
		KeyValue by = new KeyValue(card.getDropShadow().offsetYProperty(), 0,
				Interpolator.EASE_OUT);
		KeyValue br = new KeyValue(card.getDropShadow().radiusProperty(), 2,
				Interpolator.EASE_OUT);
		KeyFrame bKeyFrame = new KeyFrame(duration, bx, by, br);
		blurReset.getKeyFrames().add(bKeyFrame);

		ParallelTransition pt = new ParallelTransition(card, pathTransition,
				blurReset);
		pt.play();
	}

	/**
	 * Helper class for calculating card positions.
	 */
	private static class MoveToAbs extends MoveTo {
		/**
		 * Creates a new instance.
		 *
		 * @param node
		 *            The {@link Node} object to calculate position for.
		 * @param x
		 *            The x coordinate.
		 * @param y
		 *            The y coordinate.
		 */
		public MoveToAbs(Node node, double x, double y) {
			super(
					x - node.getLayoutX() + node.getLayoutBounds().getWidth()
							/ 2,
					y - node.getLayoutY() + node.getLayoutBounds().getHeight()
							/ 2);
		}
	}

	/**
	 * Helper class for calculating card positions.
	 */
	private static class LineToAbs extends LineTo {
		/**
		 * Creates a new instance.
		 *
		 * @param node
		 *            The {@link Node} object to calculate position for.
		 * @param x
		 *            The x coordinate.
		 * @param y
		 *            The y coordinate.
		 */
		public LineToAbs(Node node, double x, double y) {
			super(
					x - node.getLayoutX() + node.getLayoutBounds().getWidth()
							/ 2,
					y - node.getLayoutY() + node.getLayoutBounds().getHeight()
							/ 2);
		}
	}

	/**
	 * Helper class for determining mouse position.
	 */
	private static class MousePos {
		/**
		 * x and y coordinate.
		 */
		double x, y;
	}

}