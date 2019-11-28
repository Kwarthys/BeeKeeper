package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;

public class Ocimene extends AStimulus {

	public Ocimene()
	{
		this.timeDecay = 0.99;
		this.transmissibility = 0.5;
		this.type = Stimulus.Ocimene;
	}

}
