package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.IdiotGameConfiguration;
import model.card.GameCardRank;

public class RuleConfigurationLoader implements IRuleConfigurationLoader {
	
	protected IdiotGameConfiguration gameRules = new IdiotGameConfiguration();
	
	/**
	 * Path to the Card Configuration json file.
	*/
	private String cardConfigFile;
	
	/**
	 * Loads card rules from input file path
	 * @param cardConfigFile
	 */
	public RuleConfigurationLoader(String cardConfigFile){
		this.cardConfigFile = cardConfigFile;
	}
	

	@Override
	public IdiotGameConfiguration loadRules() {
		GameCardRank reverse = null;
		GameCardRank restart = null;
		GameCardRank burn = null;
		
		Gson gson = new Gson();
		
		try{
		    BufferedReader br
		        = new BufferedReader(new InputStreamReader(
		        getClass().getResourceAsStream(cardConfigFile), Charset.forName("UTF-8")));
	
		    JsonObject jo = gson.fromJson(br, JsonObject.class);
		    
		    for (Map.Entry<String, JsonElement> elem : jo.entrySet()) {
		    	if(elem.getKey().equalsIgnoreCase("restart")){
		    		restart = parseCardRankString(elem.getValue().getAsString());
		    	} else if (elem.getKey().equalsIgnoreCase("burn")){
		    		burn = parseCardRankString(elem.getValue().getAsString());
		    	} else if (elem.getKey().equalsIgnoreCase("reverse")){
		    		reverse = parseCardRankString(elem.getValue().getAsString());
		    	} else {
		    		//TODO Throw error if file contains a key not listed?
		    	}
		    	
		    	//TODO Check if all rule cards have been set, otherwise set to default values
		    }
		} catch (Exception e){
			e.printStackTrace();
		}
		
		gameRules = new IdiotGameConfiguration(restart, burn, reverse);
		
		return gameRules;		
	}
	
	private GameCardRank parseCardRankString(String cardRank) {
		
		switch(cardRank){
		
		case "A" : return GameCardRank.Ace;
		case "2" : return GameCardRank.Two;
		case "3" : return GameCardRank.Three;
		case "4" : return GameCardRank.Four;
		case "5" : return GameCardRank.Five;
		case "6" : return GameCardRank.Six;
		case "7" : return GameCardRank.Seven;
		case "8" : return GameCardRank.Eight;
		case "9" : return GameCardRank.Nine;
		case "10" : return GameCardRank.Ten;
		case "J" : return GameCardRank.Jack;
		case "Q" : return GameCardRank.Queen;
		case "K" : return GameCardRank.King;
		}
		
		return null;
	}
}
