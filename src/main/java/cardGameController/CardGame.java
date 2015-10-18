package cardGameController;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cardGameModel.Card;
import cardGameModel.CardPile;
import cardGameModel.GameCardDeck;
import cardGameModel.Player;

/**
 * This class represents the game itself, with methods to manipulate the state
 * of the game.
 */
public class CardGame {

  /**
   * Reference to the {@link GameCardDeck} object associated with this game.
   */
  private GameCardDeck deck;

  /**
   * Reference to the {@link CardPile} object representing the stock.
   */
  private CardPile stock;

  /**
   * Reference to the {@link CardPile} object representing the waste.
   */
  private CardPile waste;

  /**
   * The list of {@link CardPile} objects representing the foundation piles.
   */
  private List<CardPile> p1_foundations;
  
  /**
   * The list of {@link CardPile} objects representing the foundation piles.
   */
  private List<CardPile> p2_foundations;

  /**
   * The list of {@link CardPile} objects representing player 1's hand.
   */
  private CardPile p1_handPile;
  
  /**
   * The list of {@link CardPile} objects representing player 2's hand.
   */
  private CardPile p2_handPile;
  
  /**
   * The rules for this game.
   */
  private CardGameRules rules;
  
  /**
   * Player 1
   */
  private Player p1;

  /**
   * Player 2
   */
  private Player p2;
  
  /**
   * Status of the game
   */
  private boolean gameInProgress;
  
  /**
   * Constructs a new {@link CardGame} object.
   */
  public CardGame() {
    // Create deck
    this.deck = GameCardDeck.createGameCardDeck();

    // create stock
    this.stock = new CardPile(CardPile.Type.DECK, "S");

    // create waste
    this.waste = new CardPile(CardPile.Type.WASTE, "W");
    
    // create foundations
    this.p1_foundations = FXCollections.observableArrayList();
    this.p2_foundations = FXCollections.observableArrayList();
    
    for (int i = 0; i < 3; i++)
    	p1_foundations.add(new CardPile(CardPile.Type.DECK, "F" + i));
    
    for (int i = 3; i < 6; i++)
    	p2_foundations.add(new CardPile(CardPile.Type.DECK, "F" + i));

    // create standard piles
    this.p1_handPile = new CardPile(CardPile.Type.HAND, "K");
    
    // create standard piles
    this.p2_handPile = new CardPile(CardPile.Type.HAND, "K" + 1);
    
    //create player 1
    this.p1 = new Player(1, "Player 1", false, true);
    
    //create player 2
    this.p2 = new Player(2, "Player 2", false, false);
    
    //set status of game
    this.gameInProgress = false;

    // load rules
    this.rules = new CardGameRules(p1_handPile, p2_handPile, p1_foundations, p2_foundations, waste, stock, 3, 3);
  }

  /**
   * Returns the deck of cards that is used in the current game.
   *
   * @return The {@link GameCardDeck} object.
   */
  public GameCardDeck getDeck() {
    return deck;
  }

  /**
   * Returns the stock pile.
   *
   * @return The {@link CardPile} object representing the stock.
   */
  public CardPile getStock() {
    return stock;
  }

  /**
   * Returns the waste pile.
   *
   * @return The {@link CardPile} object representing the waste.
   */
  public CardPile getWaste() {
    return waste;
  }

  /**
   * Returns the list of foundation piles.
   *
   * @return The {@link List} of {@link CardPile} objects representing
   * the foundations.
   */
  public List<CardPile> getP1_Foundations() {
    return p1_foundations;
  }

  /**
   * Returns the list of foundation piles.
   *
   * @return The {@link List} of {@link CardPile} objects representing
   * the foundations.
   */
  public List<CardPile> getP2_Foundations() {
    return p2_foundations;
  }
  
  /**
   * Returns player 1's hand.
   *
   * @return The {@link CardPile} object representing player 1's hand.
   */
  public CardPile getP1_HandPile() {
    return p1_handPile;
  }
  
  /**
   * Returns player 1's hand.
   *
   * @return The {@link CardPile} object representing player 1's hand.
   */
  public CardPile getP2_HandPile() {
    return p2_handPile;
  }
  
  /**
   * Returns the {@link CardGameRules} object associated with this
   * {@link CardGame} instance.
   *
   * @return The {@link CardGameRules} object.
   */
  public CardGameRules getRules() {
    return rules;
  }
  
  public Player getPlayer1(){
	  return p1;
  }
  
  public Player getPlayer2(){
	  return p2;
  }
  
  public Player getActivePlayer(){
	  if(p1.isActive()){
		  return p1;
	  } else if(p2.isActive()){
		  return p2;
	  } else {
		  return p1;
	  }
  }
  
  public ObservableValue<? extends String> getActivePlayerName(){
	  SimpleStringProperty selectedName = new SimpleStringProperty();
	  if(p1.isActive()){
		  selectedName.set(p1.getName());
		  return selectedName;
	  } else if(p2.isActive()){
		  selectedName.set(p2.getName());
		  return selectedName;
	  } else {
		  selectedName.set(p1.getName());
		  return selectedName;
	  }
  }
  
  /**
   * Return status of game
   * true: game is in progress
   * false: game is not in progress
   * @return
   */
  public boolean isGameInProgress(){
	  return gameInProgress;
  }
  
  /**
   * true: game is in progress
   * false: game is not in progress
   * @param gameInProgress
   */
  public void setGameInProgress(boolean gameInProgress){
	  this.gameInProgress = gameInProgress;
  }


  /**
   * Starts a new game. Effectively shuffles the deck of cards, and deals them
   * to the standard piles, and puts the rest to the stock.
   */
  public void startNewGame() {
    // shuffle cards
    deck.shuffle();

    // deal to piles
    Iterator<Card> deckIterator = deck.iterator();

    int cardsToHandPiles = getRules().getHandCardNum();
    int cardsToPiles = getRules().getPileCardNum();
    
    //deal to player 1 card piles
    for (CardPile p1_foundationPile : p1_foundations) {
        for (int i = 0; i < cardsToPiles; i++)
        	p1_foundationPile.addCard(deckIterator.next());

        p1_foundationPile.getTopCard().flip();
     }
    
    //deal to player 2 card piles
    for (CardPile p2_foundationPile : p2_foundations) {
    	for (int i = 0; i < cardsToPiles; i++)
        	p2_foundationPile.addCard(deckIterator.next());

        p2_foundationPile.getTopCard().flip();
     }
    
    //deal to player 1 hand
    for (int i = 0; i < cardsToHandPiles; i++)
    	p1_handPile.addCard(deckIterator.next());
    
    for (Card card : p1_handPile)
    	card.flip();
    
    //deal to player 2 hand
    for (int i = 0; i < cardsToHandPiles; i++)
    	p2_handPile.addCard(deckIterator.next());

    for (Card card : p2_handPile)
    	card.flip();

    // put the rest of the card in the stock pile
    deckIterator.forEachRemaining(stock::addCard);
  }

  /**
   * Moves a list of {@link Card} object from a {@link CardPile} to another.
   *
   * @param cardsToMove The {@link List} of {@link Card}'s to move.
   * @param from        The source {@link CardPile} object.
   * @param to          The destination {@link CardPile} object.
   */
  public void moveCards(List<Card> cardsToMove, CardPile from, CardPile to) {
    if (cardsToMove == null)
      return;

    from.moveCardsToPile(cardsToMove, to);
  }
  
  public void moveCard(Card cardsToMove, CardPile from, CardPile to) {
	    if (cardsToMove == null)
	      return;

	    from.moveCardToPile(cardsToMove, to);
	  }

  /**
   * Draws a {@link Card} from the stock and put it on the waste, flipped.
   *
   * @param card The {@link Card} object to draw.
   */
  public void drawFromStock(Card card) {
    stock.moveCardToPile(card, waste);
    card.flip();
  }

  /**
   * Refills all cards from the waste to the stock, in reverse order.
   */
  public void refillStockFromWaste() {
    ListIterator<Card> revIt =
        waste.getCards().listIterator(waste.numOfCards());

    while (revIt.hasPrevious()) {
      Card currentCard = revIt.previous();
      currentCard.flip();
      stock.addCard(currentCard);
      revIt.remove();
    }
  }

  /**
   * Checks if the current game is won by the player.
   *
   * @return true if the game is won, false otherwise.
   */
  public boolean isGameWon() {
    if(p1_foundations.stream().allMatch(pile -> pile.numOfCards() == 0))
    	return true;
    else if (p2_foundations.stream().allMatch(pile -> pile.numOfCards() == 0))
    	return true;
    else
    	return false;
  }

  /**
   * Looks up a {@link CardPile} object by its short identifier.
   *
   * @param id The short identifier to look for.
   * @return The found {@link CardPile} object, or null if no matching pile found.
   */
  public CardPile getPileById(String id) {
    CardPile result;
    
    if (p1_handPile.getId().equals(id))
        return p1_handPile;
    
    if (p2_handPile.getId().equals(id))
        return p2_handPile;

    result = p1_foundations.stream()
        .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);

    if (result != null)
      return result;
    
    result = p2_foundations.stream()
            .filter(pile -> pile.getId().equals(id)).findFirst().orElse(null);

        if (result != null)
          return result;

    if (waste.getId().equals(id))
      return waste;

    if (stock.getId().equals(id))
      return stock;

    return null;
  }

public void switchCards(Card draggedCard, Card topCard, CardPile sourcePile,
		CardPile destPile) {
	sourcePile.moveCardToPile(topCard, destPile);
	destPile.moveCardToPile(draggedCard, sourcePile);
}

}
