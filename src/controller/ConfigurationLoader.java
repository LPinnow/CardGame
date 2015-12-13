package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.IdiotGameConfiguration;
import model.card.GameCardRank;
import view.CardPileView;

public class ConfigurationLoader implements IConfigurationLoader {

	protected IdiotGameConfiguration gameRules = new IdiotGameConfiguration();
	protected ArrayList<CardPileView> piles = new ArrayList<CardPileView>();
	private int defaultWidth;
	private int defaultHeight;

	/**
	 * Path to the Card Configuration json file.
	 */
	private String cardConfigFile;

	private String gameBoardConfigFile;

	private String playerConfigFile;

	private String ruleConfigFile;

	private String themeConfigFile;
	
	private String stylesheet, backFace, frontFace, defaultBackground;

	/**
	 * Loads card rules from input file path
	 * 
	 * @param cardConfigFile
	 */
	public ConfigurationLoader(String cardConfigFile,
			String gameBoardConfigFile, String playerConfigFile,
			String ruleConfigFile, String themeConfigFile) {
		this.cardConfigFile = cardConfigFile;
		this.gameBoardConfigFile = gameBoardConfigFile;
		this.playerConfigFile = playerConfigFile;
		this.ruleConfigFile = ruleConfigFile;
		this.themeConfigFile = themeConfigFile;

	}

	public IdiotGameConfiguration loadRules() {
		GameCardRank reverse = null;
		GameCardRank restart = null;
		GameCardRank burn = null;

		Gson gson = new Gson();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(ruleConfigFile),
					Charset.forName("UTF-8")));

			JsonObject jo = gson.fromJson(br, JsonObject.class);

			for (Map.Entry<String, JsonElement> elem : jo.entrySet()) {
				if (elem.getKey().equalsIgnoreCase("restart")) {
					restart = parseCardRankString(elem.getValue().getAsString());
				} else if (elem.getKey().equalsIgnoreCase("burn")) {
					burn = parseCardRankString(elem.getValue().getAsString());
				} else if (elem.getKey().equalsIgnoreCase("reverse")) {
					reverse = parseCardRankString(elem.getValue().getAsString());
				} else {
					// TODO Throw error if file contains a key not listed?
				}

				// TODO Check if all rule cards have been set, otherwise set to
				// default values
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		gameRules = new IdiotGameConfiguration(restart, burn, reverse);

		return gameRules;

	}

	public IdiotGameConfiguration getGameRules() {
		return gameRules;
	}

	@Override
	public boolean loadCards() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<CardPileView> loadGameBoardLayout() {
		Gson gson = new Gson();

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(gameBoardConfigFile),
					Charset.forName("UTF-8")));
			JsonObject jo = gson.fromJson(br, JsonObject.class);

			for (Map.Entry<String, JsonElement> elem : jo.entrySet()) {
				
				if(!elem.getValue().isJsonObject()) {
					if (elem.getKey().equalsIgnoreCase("defaultWidth")) {
						defaultWidth = elem.getValue().getAsInt();
					} else if (elem.getKey().equalsIgnoreCase("defaultHeight")) {
						defaultHeight = elem.getValue().getAsInt();
					}					
				} else {

					CardPileView view = new CardPileView(
							elem.getValue().getAsJsonObject()
									.get("cardGapVertical").getAsDouble(),
							elem.getValue().getAsJsonObject()
									.get("cardGapHorizontal").getAsDouble(),
							elem.getValue().getAsJsonObject().get("initialX")
									.getAsDouble(),
							elem.getValue().getAsJsonObject().get("initialY")
									.getAsDouble(),
							elem.getValue().getAsJsonObject().get("id")
									.getAsString());
					piles.add(view);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return piles;
	}

	@Override
	public boolean loadPlayers() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadTheme() {
		Gson gson = new Gson();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(themeConfigFile),
					Charset.forName("UTF-8")));

			JsonObject jo = gson.fromJson(br, JsonObject.class);

			for (Map.Entry<String, JsonElement> elem : jo.entrySet()) {
				if (elem.getKey().equalsIgnoreCase("stylesheet")) {
					stylesheet = elem.getValue().getAsString();
				} else if (elem.getKey().equalsIgnoreCase("backFace")) {
					backFace = elem.getValue().getAsString();
				} else if (elem.getKey().equalsIgnoreCase("frontFace")) {
					frontFace = elem.getValue().getAsString();
				} else if (elem.getKey().equalsIgnoreCase("defaultBackground")) {
					defaultBackground = elem.getValue().getAsString();
				} 

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
		
	}

	private GameCardRank parseCardRankString(String cardRank) {

		switch (cardRank) {

		case "A":
			return GameCardRank.Ace;
		case "2":
			return GameCardRank.Two;
		case "3":
			return GameCardRank.Three;
		case "4":
			return GameCardRank.Four;
		case "5":
			return GameCardRank.Five;
		case "6":
			return GameCardRank.Six;
		case "7":
			return GameCardRank.Seven;
		case "8":
			return GameCardRank.Eight;
		case "9":
			return GameCardRank.Nine;
		case "10":
			return GameCardRank.Ten;
		case "J":
			return GameCardRank.Jack;
		case "Q":
			return GameCardRank.Queen;
		case "K":
			return GameCardRank.King;
		}

		return null;
	}

	public int getLayoutWidth() {
		return defaultWidth;
	}

	public int getLayoutHeight() {
		return defaultHeight;
	}
	
	public String getStylesheet() {
		return stylesheet;
	}

	public String getFrontFace() {
		return frontFace;
	}
	
	public String getBackFace() {
		return backFace;
	}
	
	public String getDefaultBackground() {
		return defaultBackground;
	}


}
