package com.beekeeper.model.agent.implem;

import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public class TestEmitterAgent extends EmitterAgent
{
	private static Stimulus[] stimuli = {Stimulus.StimulusA, Stimulus.StimulusB, Stimulus.StimulusC};

	private Stimulus smell;

	public TestEmitterAgent(StimuliManagerServices stimuliManagerServices) {
		super(stimuliManagerServices);

		this.type = AgentType.TEST_EMITTERAGENT;

		smell = stimuli[(int)(Math.random() * 3)];
		smell = Stimulus.StimulusA;

		this.setEnergy(Math.random() /2 + 0.5);
	}

	@Override
	public void live()
	{		
		emit(smell, 1-this.getEnergy());
		this.addToEnergy(-0.001);

		if(this.getEnergy() == 0)
		{
			this.alive = false;
		}
	}

}
