package test.validators;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import model.move.MoveResult;
import model.move.TakePileMove;
import controller.validators.MoveValidator;
import controller.validators.ValidationResult;
import model.*;
import model.card.Card;
import model.card.CardDeck;
import model.card.GameCard;
import model.card.GameCardRank;
import model.card.GameCardSuit;

public class MoveValidatorTests {

	IdiotGameState state = new IdiotGameState();
	IdiotGameConfiguration defaultConfig;
	
	Player player = new Player(1, "Gary", true, true);
	Card playerOneHandCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Seven);
	Card playerOneNonHandCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Four);
	Card playerOneFaceUpTableCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Two);
	Card playerOneFaceDownTableCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Ace);
	Card playerOneNonTableCard = new GameCard(false, GameCardSuit.Hearts, GameCardRank.Four);
	
	@Before
    public void setUp() throws Exception{
		
		defaultConfig = new IdiotGameConfiguration(GameCardRank.Two, GameCardRank.Ten, GameCardRank.Five);
		
		state = new IdiotGameState(2);
		state.discardedCards = new ArrayList<Card>();
		
		CardDeck deck = CardDeck.createGameCardDeck();
		state.drawCards = deck.getCards();
		state.pile = new ArrayList<Card>();
		state.discardedCards = new ArrayList<Card>();
		
		state.PlayerPlaces.get(0).tableCards1.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Ace));
		state.PlayerPlaces.get(0).tableCards1.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Two));
		state.PlayerPlaces.get(0).tableCards2.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Three));
		state.PlayerPlaces.get(0).tableCards2.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Four));
		state.PlayerPlaces.get(0).tableCards3.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Five));
		state.PlayerPlaces.get(0).tableCards3.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Six));
		state.PlayerPlaces.get(0).hand.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Seven));
		state.PlayerPlaces.get(0).hand.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Eight));
		state.PlayerPlaces.get(0).hand.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.Nine));
		state.PlayerPlaces.get(1).tableCards1.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Ace));
		state.PlayerPlaces.get(1).tableCards1.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Two));
		state.PlayerPlaces.get(1).tableCards2.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Three));
		state.PlayerPlaces.get(1).tableCards2.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Four));
		state.PlayerPlaces.get(1).tableCards3.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Five));
		state.PlayerPlaces.get(1).tableCards3.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Six));
		state.PlayerPlaces.get(1).hand.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Seven));
		state.PlayerPlaces.get(1).hand.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Eight));
		state.PlayerPlaces.get(1).hand.add(new GameCard(false, GameCardSuit.Hearts, GameCardRank.Nine));
		
		state.CurrentGamePhase = IdiotGameState.GamePhases.CardSwapping;
    }
	
	@Test
	public void IsValidMove_TakePileMoveRequestOnEmptyPile_ReturnsInvalidMove() {

		MoveValidator classUnderTest = new MoveValidator();
		
		classUnderTest.setState(state);
		classUnderTest.setConfig(defaultConfig);
		
		ValidationResult result = classUnderTest.IsValidMove(new TakePileMove("",""));
		
		assertFalse(result.Success);
		assertEquals("Cannot take pile because it is empty", result.ErrorMessage);
	}
	
	@Test
	public void IsValidMove_TakePileMoveRequestOnPileWithOneCard_ReturnsValidMove() {

		MoveValidator classUnderTest = new MoveValidator();
		
		classUnderTest.setState(state);
		classUnderTest.setConfig(defaultConfig);
		
		state.pile.add(new GameCard(false, GameCardSuit.Clubs, GameCardRank.King));
		
		ValidationResult result = classUnderTest.IsValidMove(new TakePileMove("",""));
		
		assertTrue(result.Success);
	}
	
	@Test
	public void IsValidMove_TakePileMoveRequestOnPileWithManyCards_ReturnsValidMove() {

		MoveValidator classUnderTest = new MoveValidator();
		
		classUnderTest.setState(state);
		classUnderTest.setConfig(defaultConfig);
		
		state.pile.add(new GameCard(false, GameCardSuit.Diamonds, GameCardRank.King));
		state.pile.add(new GameCard(false, GameCardSuit.Diamonds, GameCardRank.Queen));
		state.pile.add(new GameCard(false, GameCardSuit.Diamonds, GameCardRank.Jack));
		state.pile.add(new GameCard(false, GameCardSuit.Diamonds, GameCardRank.Ten));
		state.pile.add(new GameCard(false, GameCardSuit.Diamonds, GameCardRank.Nine));
		state.pile.add(new GameCard(false, GameCardSuit.Diamonds, GameCardRank.Eight));
		state.pile.add(new GameCard(false, GameCardSuit.Diamonds, GameCardRank.Seven));
		
		ValidationResult result = classUnderTest.IsValidMove(new TakePileMove("",""));
		
		assertTrue(result.Success);
	}

}

