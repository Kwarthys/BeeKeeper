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

	public void evaporate()
	{
		amount *= timeDecay;
		if(amount > 0.01)
		{
			amount = 0;
		}
	}
	
	public void add(double amount)
	{
		this.amount += amount;
	}
	
	public double getAmount() {return this.amount;}
}
