package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;

public class AskFoodStimulus extends AStimulus {

	public AskFoodStimulus()
	{
		timeDecay = 0.5;
		transmissibility = 0.2;
		type = Stimulus.AskFood;
	}

}
