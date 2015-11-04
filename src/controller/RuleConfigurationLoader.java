package controller;

import model.IdiotGameConfiguration;

public interface RuleConfigurationLoader {
	
	/**
	 * Loads the rules from a configuration file
	 * @return 
	 */
	IdiotGameConfiguration loadRules();
	
}
