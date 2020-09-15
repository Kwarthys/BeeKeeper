package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

public class EthyleOleate extends AStimulus {

	public EthyleOleate()
	{
		this.halfLife = ModelParameters.ETHYLE_OLEATE_HALFLIFE;
		this.transmissibility_halflifelike = ModelParameters.ETHYLE_OLEATE_TRANSMISSIBILITY;
		this.type = Stimulus.EthyleOleate;
	}

}
