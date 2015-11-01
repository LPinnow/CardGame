package test;

import controller.*;
import static org.junit.Assert.*;

import org.junit.Test;
import test.mocks.*;

public class IdiotGameEngineTests {

	@Test
	public void testInitializeNewGame() {

		int numberOfPlayers = 2; 
		
		IdiotGameEngine classUnderTest = new IdiotGameEngine(new MockTableSwapValidator(null));
		
		classUnderTest.initializeNewGame(2);
		
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
	}

	@Test
	public void testRequestHandToTableCardSwap() {
		fail("Not yet implemented");
	}

	@Test
	public void testBeginPlay() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubmitMove() {
		fail("Not yet implemented");
	}

}
