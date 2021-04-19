package com.beekeeper.model.agent;

import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public class FakeAgent extends Agent {

	@Override
	public void live() {}

	@Override
	public void registerNewStimuliManagerServices(StimuliManagerServices stimuliManagerServices) {}

	@Override
	public String getStringName()
	{
		return "FakeAgent" + ID;
	}

	@Override
	public void logTurn(MyLogger logger, int turnIndex) {}

}
