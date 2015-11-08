package controller;

import java.util.ArrayList;

import view.CardPileView;
import model.IdiotGameConfiguration;

public interface IConfigurationLoader {
	
	/**
	 * Loads all necessary configuration files for a chosen game.
	 * @return 
	 */
	boolean loadCards();
	ArrayList<CardPileView> loadGameBoardLayout();
	boolean loadPlayers();
	IdiotGameConfiguration loadRules();
	boolean loadTheme();
	
}
