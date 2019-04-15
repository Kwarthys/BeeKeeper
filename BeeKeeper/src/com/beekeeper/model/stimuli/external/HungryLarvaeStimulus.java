package com.beekeeper.model.stimuli.external;

import com.beekeeper.model.stimuli.AStimulus;

public class HungryLarvaeStimulus extends AStimulus
{
	private static double timeDecayHL = 0.1;
	private static double transmissibilityHL = 0.1;
	
	public HungryLarvaeStimulus()
	{
		this.timeDecay = timeDecayHL;
		this.transmissibility = transmissibilityHL;
	}
}
