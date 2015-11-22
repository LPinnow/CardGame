package view;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
import controller.CardGame;
import controller.IIdiotGameEngine;

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

		// ignore resulting click event generated after drag
		if (justDragged) {
			justDragged = false;
			e.consume();
			return;
		}
		
		if (game.getCurrentGameState().CurrentGamePhase() == GamePhases.GameCompleted) {
			draggedCard = null;
			draggedCardView = null;
			e.consume();
			return;
		}

		// put card from stock to waste and flip them
		/** get current cardView. */
		CardView cardView = (CardView) e.getSource();

		/** get current card. */
		Card card = cardView.asGameCard();

		int currentPlayer = game.getCurrentGameState()
				.CurrentPlayerTurn();

		if (e.getButton() == MouseButton.SECONDARY) {
			if (cardView.getContainingPile().equals(
					gameBoard.getPlayerHandView(currentPlayer))) {
				gameBoard.addToSelection(cardView);
			}

		} else if (e.getButton() == MouseButton.PRIMARY) {

			if (cardView.getContainingPile().equals(gameBoard.getDeckView())) {
				MoveResult playDeckResult = game.submitMove(game
						.getCurrentGameState()
						.CurrentPlayerTurn(), new PlayTopOfDeck(card.getId(),
						game
								.getCurrentGameState().GetPile().toString()));
				if (playDeckResult.success) {
					gameBoard.setMessageLabelText("");
					slideToPile(cardView, gameBoard.getDeckView(),
							gameBoard.getWasteView(), true);

					gameBoard.updateGameBoard();
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());

				} else {
					gameBoard
							.setMessageLabelText("Card cannot be placed on deck");
				}
			} else if (cardView.getContainingPile().equals(
					gameBoard.getWasteView())) {
				System.out.println("Play Waste Pile Move ");
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
				}
				else {
					gameBoard.setMessageLabelText("Cannot pick up cards");
				}
			}

		}

		e.consume();
	};

	/**
	 * This event handler is attached to the stock itself. It puts all cards on
	 * the waste back on the stock.
	 */
	EventHandler<MouseEvent> stockReverseCardsHandler = e -> {
		// put all cards on waste back to stock and flip them
		// game.refillStockFromWaste();

		/** get view for waste. */
		CardPileView wasteView = gameBoard.getWasteView();

		/** get view for stock. */
		CardPileView stockView = gameBoard.getDeckView();

		/** reverse iterator for list. */
		ListIterator<CardView> revIt = wasteView.getCards().listIterator(
				wasteView.numOfCards());

		while (revIt.hasPrevious()) {
			/** get current card view. */
			CardView currentCardView = revIt.previous();
			currentCardView.setToFaceDown();
			makeClickable(currentCardView);
			stockView.addCardView(currentCardView);
			revIt.remove();
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
		// Calculate difference vector from clicked point
		if (e.getButton() != MouseButton.PRIMARY) {
			return;
		}
		
		if (game.getCurrentGameState().CurrentGamePhase() == GamePhases.GameCompleted) {
			draggedCard = null;
			draggedCardView = null;
			e.consume();
			return;
		}

		/** x component. */
		double offsetX = e.getSceneX() - mousePos.x;
		/** y component. */
		double offsetY = e.getSceneY() - mousePos.y;

		/** get current cardView. */
		CardView cardView = (CardView) e.getSource();

		/** get current card. */
		Card card = game.getCurrentGameState().GetFullDeck()
				.getById(cardView.getShortID());

		draggedCardView = cardView;
		draggedCard = card;
		
		// Handles dragging coordinates
		draggedCardView.toFront();
		draggedCardView.setTranslateX(offsetX);
		draggedCardView.setTranslateY(offsetY);

		if (gameBoard.getSelected().size() > 1
				&& gameBoard.getSelected().contains(cardView)) {

			int count = 0;
			List<CardView> tempList = gameBoard.getSelected();
			for (int i = tempList.size() - 1; i >= 0; i--) {

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
		// if no cards are dragged, return immediately
		if (draggedCard == null && draggedCardView == null) {
			return;
		}
		justDragged = true;

		if (game.getCurrentGameState().CurrentGamePhase() == GamePhases.GameCompleted) {
			draggedCard = null;
			draggedCardView = null;
			e.consume();
			return;
		}
			
		// Get the actual card
		/** get current card view. */
		CardView cardView = (CardView) e.getSource();
		/** get current card. */
		Card card = game.getCurrentGameState().GetFullDeck()
				.getById(cardView.getShortID());

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
		/** get current pile view. */
		CardPileView activePileView = cardView.getContainingPile();
		/** get current pile. */
		List<Card> activePile = game.getPileById(activePileView.getShortID());

		for (Card c : activePile) {
			if (c.getId().equals(cardView.getShortID()))
				card = c;
		}

		System.out.println("CardView ID: " + cardView.getShortID()
				+ " in pile: " + activePileView.getShortID());

		// check if card(s) are intersecting with any of the piles
		if (cardsToPlay.size() > 0) {
			if (checkPile(cardsToPlay, cardView, activePile, activePileView, gameBoard.getNearestPile(draggedCardView))) {
				draggedCard = null;
				draggedCardView = null;
				return;
			}
		} else if (checkPile(card, cardView, activePile, activePileView, gameBoard.getNearestPile(draggedCardView))) {
			draggedCard = null;
			draggedCardView = null;
			return;
		}

		// if not intersecting with any valid pile, slide them back

		if (draggedViewList.size() > 0) {
			for (CardView draggedView : draggedViewList) {
				slideBack(draggedView);
			}
		} else {
			slideBack(draggedCardView);
		}

		// reorder CardViews
		for (CardView cv : activePileView) {
			cv.toFront();
		}

		// throw away dragged cards info
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
	public InputManager(IIdiotGameEngine game, GameBoard gameBoard) {
		this.game = game;
		this.gameBoard = gameBoard;
	}

	/**
	 * Applies the appropriate event handlers for cards not on the stock.
	 *
	 * @param card
	 *            The {@link CardView} to apply event listeners for.
	 */
	public void makeDraggable(CardView card) {
		// card.setOnMouseClicked(null);
		card.setOnMousePressed(onMousePressedHandler);
		card.setOnMouseDragged(onMouseDraggedHandler);
		card.setOnMouseReleased(onMouseReleasedHandler);
	}

	public void removeDraggable(CardView card) {
		// card.setOnMouseClicked(null);
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
		// card.setOnMouseDragged(null);
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

	private boolean checkPile(List<Card> cards, CardView cardView,
			List<Card> activePile,
			CardPileView activePileView, CardPileView pileView) {

		boolean result = false;

		// skip checking the same pile
		if (pileView.equals(activePileView))
			return result;

		if (isOverPile(cardView, pileView)) {

			if (game.getCurrentGameState().CurrentGamePhase()
					.equals(IdiotGameState.GamePhases.GamePlay))
			{
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
								true);
					}
					gameBoard.clearSelection();
					gameBoard.updateGameBoard();
					result = true;
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());
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
	 * @param card
	 * @param cardView
	 * @param activePile
	 * @param activePileView
	 * @param pileView
	 * @return
	 */
	private boolean checkPile(
			Card card, CardView cardView, List<Card> activePile,
			CardPileView activePileView, CardPileView pileView) {

		boolean result = false;

		// skip checking the same pile
		if (pileView.equals(activePileView))
			return result;

		if (isOverPile(cardView, pileView)) {
			System.out.println("Dropped on pile: " + pileView.getShortID());

			if (game.getCurrentGameState().CurrentGamePhase()
					.equals(IdiotGameState.GamePhases.CardSwapping)
					&& pileView.getShortID().contains("Foundation"))
			{
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
					// slideToHand(tablePileTopCardView, pileView,
					// activePileView,
					// draggedCardView.getLayoutX(),
					// draggedCardView.getLayoutY(), draggedCardViewIndex,
					// false);
					makeDraggable(tablePileTopCardView);
					makeClickable(tablePileTopCardView);
					removeDraggable(cardView);
					removeClickable(cardView);

					gameBoard.setMessageLabelText("");
					result = true;
					gameBoard.setActivePlayer(game.getCurrentGameState()
							.CurrentPlayerTurn());
					justDragged = false;

				}
				else {
					result = false;
					gameBoard.setMessageLabelText(swapResult.message);
				}
			}
			else if (game.getCurrentGameState().CurrentGamePhase()
					.equals(IdiotGameState.GamePhases.GamePlay))
			{
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
				} else {
					result = false;
					gameBoard.setMessageLabelText(playCard.getMessage());
				}
				
				if (playCard.isGameEnded()) {
					gameBoard.setMessageLabelText(playCard.getMessage());
				}

			}
		}

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
		if (pileView.isEmpty())
			return cardView.getBoundsInParent().intersects(
					pileView.getBoundsInParent());
		else
			return cardView.getBoundsInParent().intersects(
					pileView.getTopCardView().getBoundsInParent());
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

		double targetX = card.getLayoutX();
		double targetY = card.getLayoutY();

		animateCardMovement(card, sourceX, sourceY,
				targetX, targetY, Duration.millis(150), e -> {
					card.getDropShadow().setRadius(2);
					card.getDropShadow().setOffsetX(0);
					card.getDropShadow().setOffsetY(0);
					gameBoard.resetSelection(false);
			});
	}

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
			CardPileView destPile, boolean replaceAtIndex) {
		if (cardToSlide == null)
			return;

		double targetX;
		double targetY;

		if (destPile.isEmpty()) {
			targetX = destPile.getLayoutX();
			targetY = destPile.getLayoutY();
		} else {
			targetX = destPile.getTopCardView().getLayoutX();
			targetY = destPile.getTopCardView().getLayoutY();
		}

		CardView currentCardView = cardToSlide;
		double sourceX =
				currentCardView.getLayoutX() + currentCardView.getTranslateX();
		double sourceY =
				currentCardView.getLayoutY() + currentCardView.getTranslateY();
		sourcePile.moveCardViewToPile(currentCardView, destPile);

		destPile.restackCards();
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
	private void slideToHand(CardView cardToSlide, CardPileView sourcePile,
			CardPileView destPile, double x, double y, int index,
			boolean replaceAtIndex) {
		if (cardToSlide == null)
			return;

		double sourceX = cardToSlide.getLayoutX() + cardToSlide.getTranslateX();
		double sourceY = cardToSlide.getLayoutY() + cardToSlide.getTranslateY();

		double targetX = x;
		double targetY = y;

		sourcePile.moveCardViewToPile(cardToSlide, destPile);

		animateCardMovement(cardToSlide, sourceX, sourceY,
				targetX, targetY, Duration.millis(150), e -> {

					cardToSlide.getDropShadow().setRadius(2);
					cardToSlide.getDropShadow().setOffsetX(0);
					cardToSlide.getDropShadow().setOffsetY(0);
				});
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

		PathTransition pathTransition =
				new PathTransition(duration, path, card);
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
