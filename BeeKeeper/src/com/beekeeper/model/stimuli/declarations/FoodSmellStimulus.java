package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

public class FoodSmellStimulus extends AStimulus {

	public FoodSmellStimulus()
	{
		this.halfLife = ModelParameters.FoodSmell_HALFLIFE;
		this.transmissibility_halflifelike = ModelParameters.TRANSMISSIBILITY_FoodSmell;
		this.type = Stimulus.FoodSmell;
	}
}
