package test;

import controller.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IdiotGameEngineTests {

	@Test
	public void testInitializeNewGame() {

		IdiotGameEngine classUnderTest = new IdiotGameEngine();
		
		classUnderTest.initializeNewGame(2 /*number of players*/);
		
		//TODO: switch IdiotGameEngine state back to protected and verify the facade state instead 
		
		assertEquals(34, classUnderTest.state.drawCards.size());
		
		System.out.print(classUnderTest.state.toString());
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
