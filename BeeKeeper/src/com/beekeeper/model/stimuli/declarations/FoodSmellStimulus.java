package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

public class FoodSmellStimulus extends AStimulus {

	public FoodSmellStimulus()
	{
		this.timeDecay = ModelParameters.TIME_DECAY_FoodSmell;
		this.transmissibility = ModelParameters.TRANSMISSIBILITY_FoodSmell;
		this.type = Stimulus.FoodSmell;
	}
}
