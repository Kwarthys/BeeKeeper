package com.beekeeper.model.stimuli;

import com.beekeeper.model.stimuli.external.ExternalStimuli;

public abstract class AStimulus
{
	protected double timeDecay;
	protected double transmissibility;
	protected ExternalStimuli type;
	
	protected double amount;
	
	public ExternalStimuli getStimulusType() {return type;}
	
	public AStimulus(double d)
	{
		this.amount = d;
	}
}
