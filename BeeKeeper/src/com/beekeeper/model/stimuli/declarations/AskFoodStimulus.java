package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;

public class AskFoodStimulus extends AStimulus {

	public AskFoodStimulus()
	{
		halfLife = 0.1;
		transmissibility_halflifelike = 0.04;
		type = Stimulus.AskFood;
	}

}
