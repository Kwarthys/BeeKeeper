package com.beekeeper.model.stimuli.external;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.parameters.ModelParameters;

public class HungryLarvaeStimulus extends AStimulus
{	
	public HungryLarvaeStimulus(Point2D.Double agentPos, double d)
	{
		super(agentPos, d);
		this.timeDecay = ModelParameters.TIME_DECAY_HungryLarvae;
		this.transmissibility = ModelParameters.TRANSMISSIBILITY_HungryLarvae;
		this.type = ExternalStimuli.HungryLarvae;
	}
}
