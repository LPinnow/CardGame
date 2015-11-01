package test.validators;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.validators.TableSwapValidationResult;
import controller.validators.TableSwapValidator;
import model.*;

public class TableSwapValidatorTests {

	IdiotGameState state = new IdiotGameState();
	
	Player player = new Player(1, "Gary", true, true);
	Card playerOneHandCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Seven);
	Card playerOneNonHandCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Four);
	Card playerOneFaceUpTableCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Two);
	Card playerOneFaceDownTableCard = new GameCard(false, GameCardSuit.Clubs, GameCardRank.Ace);
	Card playerOneNonTableCard = new GameCard(false, GameCardSuit.Hearts, GameCardRank.Four);
	
	@Before
    public void setUp() throws Exception{
		
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
	public void IsValidSwap_ToLowAPlayerNumber_FailsValidation() {
		
		TableSwapValidator classUnderTest = new TableSwapValidator(state);
	
		TableSwapValidationResult result = classUnderTest.isValidSwap(0, playerOneHandCard, playerOneFaceUpTableCard);
		
		assertFalse(result.Success);
		assertEquals("Invalid Player Number: 0", result.ErrorMessage);
	}
	
	@Test
	public void IsValidSwap_ToHighAPlayerNumber_FailsValidation() {
		
		TableSwapValidator classUnderTest = new TableSwapValidator(state);
	
		TableSwapValidationResult result = classUnderTest.isValidSwap(3, playerOneHandCard, playerOneFaceUpTableCard);
		
		assertFalse(result.Success);
		assertEquals("Invalid Player Number: 3", result.ErrorMessage);
	}
	
	@Test
	public void IsValidSwap_ValidPlayerNumber_PassesValidation() {
		
		TableSwapValidator classUnderTest = new TableSwapValidator(state);
	
		TableSwapValidationResult result = classUnderTest.isValidSwap(1, playerOneHandCard, playerOneFaceUpTableCard);
		
		assertTrue(result.Success);
	}
	
	@Test
	public void IsValidSwap_TableCardIsOneFaceUpForAndHandCardBelongsToPlayer_PassesValidation() {
		
		TableSwapValidator classUnderTest = new TableSwapValidator(state);
	
		TableSwapValidationResult result = classUnderTest.isValidSwap(1, playerOneHandCard, playerOneFaceUpTableCard);
		
		assertTrue(result.Success);
	}
	
	@Test
	public void IsValidSwap_TableCardIsOneFaceDownForPlayer_FailsValidation() {
		
		TableSwapValidator classUnderTest = new TableSwapValidator(state);
	
		TableSwapValidationResult result = classUnderTest.isValidSwap(1, playerOneHandCard, playerOneFaceDownTableCard);
		
		assertFalse(result.Success);
		assertEquals("Table card requested for swap not among Player 1's face up cards", result.ErrorMessage);
	}
	
	@Test
	public void IsValidSwap_TableCardIsNotOnTableForPlayer_FailsValidation() {
		
		TableSwapValidator classUnderTest = new TableSwapValidator(state);
	
		TableSwapValidationResult result = classUnderTest.isValidSwap(1, playerOneHandCard, playerOneNonTableCard);
		
		assertFalse(result.Success);
		assertEquals("Table card requested for swap not among Player 1's face up cards", result.ErrorMessage);
	}
	
	@Test
	public void IsValidSwap_HandCardDoesNotBelongToPlayer_FailsValidation() {
		
		TableSwapValidator classUnderTest = new TableSwapValidator(state);
	
		TableSwapValidationResult result = classUnderTest.isValidSwap(1, playerOneNonHandCard, playerOneFaceUpTableCard);
		
		assertFalse(result.Success);
		assertEquals("Hand card requested for swap not in Player 1's hand", result.ErrorMessage);
	}

}
