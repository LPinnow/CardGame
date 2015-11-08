package controller;

import model.IdiotGameConfiguration;

public interface IRuleConfigurationLoader {
	
	/**
	 * Loads the rules from a configuration file
	 * @return 
	 */
	IdiotGameConfiguration loadRules();
	
}
