package test.mocks;

import controller.IRuleConfigurationLoader;
import model.IdiotGameConfiguration;
import model.card.GameCardRank;

public class MockRuleConfigurationLoader implements IRuleConfigurationLoader {

	@Override
	public IdiotGameConfiguration loadRules() {
		return new IdiotGameConfiguration() {{ burnCard = GameCardRank.Ten; restartCard = GameCardRank.Two; reverseCard = GameCardRank.Five; }};
	}
}
