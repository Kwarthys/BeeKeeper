package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.parameters.ModelParameters;

public class HungryLarvaeStimulus extends AStimulus
{	
	public HungryLarvaeStimulus()
	{
		this.halfLife = ModelParameters.HungryLarvae_HALFLIFE;
		this.transmissibility_halflifelike = ModelParameters.TRANSMISSIBILITY_HungryLarvae;
		//this.type = Stimulus.HungryLarvae;
	}
}
