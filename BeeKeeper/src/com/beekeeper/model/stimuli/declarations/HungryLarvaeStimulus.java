package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

public class HungryLarvaeStimulus extends AStimulus
{	
	public HungryLarvaeStimulus()
	{
		this.timeDecay = ModelParameters.TIME_DECAY_HungryLarvae;
		this.transmissibility = ModelParameters.TRANSMISSIBILITY_HungryLarvae;
		this.type = Stimulus.HungryLarvae;
	}
}
