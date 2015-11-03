package test;

import controller.*;
import controller.validators.TableSwapValidationResult;
import model.IdiotGameState;
import model.MoveResult;
import model.Player;
import model.card.Card;
import static org.junit.Assert.*;

import org.junit.Test;

import test.mocks.*;

public class IdiotGameEngineTests {

	@Test
	public void initializeNewGame_Invoked_ResetsStateProperly() {

		int numberOfPlayers = 2; 
		GameBoard board = null;
		
		IdiotGameEngine classUnderTest = new IdiotGameEngine(new MockTableSwapValidator(null));
		
		classUnderTest.initializeNewGame(board, numberOfPlayers);
		
		assertEquals(34, classUnderTest.getCurrentGameState().GetDeck().size());
		assertEquals(0, classUnderTest.getCurrentGameState().GetPile().size());
		assertEquals(3, classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getHand().size());
		assertEquals(2, classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getTableCards1().size());
		assertEquals(2, classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getTableCards2().size());
		assertEquals(2, classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getTableCards3().size());
		assertEquals(3, classUnderTest.getCurrentGameState().getPlayerPlaces().get(1).getHand().size());
		assertEquals(2, classUnderTest.getCurrentGameState().getPlayerPlaces().get(1).getTableCards1().size());
		assertEquals(2, classUnderTest.getCurrentGameState().getPlayerPlaces().get(1).getTableCards2().size());
		assertEquals(2, classUnderTest.getCurrentGameState().getPlayerPlaces().get(1).getTableCards3().size());
		assertEquals(IdiotGameState.GamePhases.CardSwapping, classUnderTest.getCurrentGameState().CurrentGamePhase());
	}

	@Test
	public void requestHandToTableCardSwap_IncorrectGameState_ReturnsError() {
		int numberOfPlayers = 2; 
		GameBoard board = null;
		IdiotGameEngine classUnderTest = new IdiotGameEngine(new MockTableSwapValidator(null));
		classUnderTest.initializeNewGame(null, numberOfPlayers);
		classUnderTest.beginPlay();
		
		MoveResult result = classUnderTest.requestHandToTableCardSwap(new Player(1, "Gary", false, false), null, null);
		
		assertFalse(result.success);
		assertEquals("Swapping table cards is not valid in this state of the game.", result.message);
	}
	
	@Test
	public void requestHandToTableCardSwap_ValidationFails_ReturnsNotSuccessfulAndValidatorErrorMessage() {
		
		String validatorErrorMessage =  "error message";
		
		TableSwapValidationResult validatorResultToReturn = new TableSwapValidationResult() {{ Success = false; ErrorMessage = validatorErrorMessage; }};
		
		int numberOfPlayers = 2; 
		GameBoard board = null;
		IdiotGameEngine classUnderTest = new IdiotGameEngine(new MockTableSwapValidator(validatorResultToReturn));
		classUnderTest.initializeNewGame(board, numberOfPlayers);
		
		MoveResult result = classUnderTest.requestHandToTableCardSwap(new Player(1, "Gary", false, false), null, null);
		
		assertFalse(result.success);
		assertEquals(validatorErrorMessage, result.message);
	}
	
	@Test
	public void requestHandToTableCardSwap_ValidationPasses_ReturnsSuccessAndSwapsHandCardWithTableCard() {

		int targetTableStackNumber = 1;
		
		TableSwapValidationResult validatorResultToReturn = new TableSwapValidationResult() {{ Success = true; targetTableStack = targetTableStackNumber; }};
		
		int numberOfPlayers = 2; 
		GameBoard board = null;
		IdiotGameEngine classUnderTest = new IdiotGameEngine(new MockTableSwapValidator(validatorResultToReturn));
		classUnderTest.initializeNewGame(board, numberOfPlayers);
		
		Card validTableSwapCardForPlayer1 = classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getTableCards1().getTopCard();
		Card validHandCardForPlayer1 = classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getHand().getCards().get(0);
		
		MoveResult result = classUnderTest.requestHandToTableCardSwap(new Player(1, "Gary", false, false), validHandCardForPlayer1, validTableSwapCardForPlayer1);
		
		assertTrue(result.success);
		assertEquals(validHandCardForPlayer1, classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getTableCards1().getTopCard());
		assertEquals(validTableSwapCardForPlayer1, classUnderTest.getCurrentGameState().getPlayerPlaces().get(0).getHand().getCards().get(2));
	}

	@Test
	public void beginPlay_Invoked_SetsGamePhaseToGamePlayAndSetsTurnToFirstPlayer() {
		IdiotGameEngine classUnderTest = new IdiotGameEngine(new MockTableSwapValidator(null));
		classUnderTest.initializeNewGame(null, 2);
		classUnderTest.beginPlay();
		
		assertEquals(IdiotGameState.GamePhases.GamePlay, classUnderTest.getCurrentGameState().CurrentGamePhase());
		assertEquals(1, classUnderTest.getCurrentGameState().CurrentPlayerTurn());
	}

	@Test
	public void testSubmitMove() {
		fail("Not yet implemented");
	}

}
