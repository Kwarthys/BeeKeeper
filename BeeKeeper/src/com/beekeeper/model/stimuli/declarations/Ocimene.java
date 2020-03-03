package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

public class Ocimene extends AStimulus {

	public Ocimene()
	{
		this.halfLife = ModelParameters.OCIMENE_HALFLIFE;
		this.transmissibility_halflifelike = ModelParameters.OCIMENE_TRANSMISSIBILITY;
		this.type = Stimulus.Ocimene;
	}

}
