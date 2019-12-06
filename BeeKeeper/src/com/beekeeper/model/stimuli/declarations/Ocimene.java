package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

public class Ocimene extends AStimulus {

	public Ocimene()
	{
		this.timeDecay = ModelParameters.OCIMENE_EVAPRATE;
		this.transmissibility = ModelParameters.OCIMENE_TRANSMISSIBILITY;
		this.type = Stimulus.Ocimene;
	}

}
