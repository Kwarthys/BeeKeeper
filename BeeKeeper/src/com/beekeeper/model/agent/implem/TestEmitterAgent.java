package com.beekeeper.model.agent.implem;

import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimulusFactory;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public class TestEmitterAgent extends EmitterAgent
{
	private static Stimulus[] stimuli = {Stimulus.StimulusA, Stimulus.StimulusB, Stimulus.StimulusC};
	
	private Stimulus smell;
	
	public TestEmitterAgent(StimuliManagerServices stimuliManagerServices, double x, double y) {
		super(stimuliManagerServices, x, y);
		
		this.type = AgentType.TEST_EMITTERAGENT;
		
		smell = stimuli[(int)(Math.random() * 3)];
		
		this.setEnergy(Math.random() /2 + 0.5);
	}

	@Override
	public void live() {
		this.stimuliLoad.emit(StimulusFactory.get(this.smell, 1-this.getEnergy()));
		this.addToEnergy(-0.001);
		
		if(this.getEnergy() < 0)
		{
			this.alive = false;
		}
	}

}
