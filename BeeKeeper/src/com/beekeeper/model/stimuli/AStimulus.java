package com.beekeeper.model.stimuli;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.external.ExternalStimuli;

public abstract class AStimulus
{
	protected double timeDecay;
	protected double transmissibility;
	protected ExternalStimuli type;
	
	protected Point2D.Double emiterAgentPos;
	
	protected double amount;
	
	public ExternalStimuli getStimulusType() {return type;}
	
	public AStimulus(Point2D.Double agentPos, double d)
	{
		this.amount = d;
		this.emiterAgentPos = agentPos;
	}

	public void evaporate()
	{
		amount *= timeDecay;
		if(amount > 0.01)
		{
			amount = 0;
		}
	}
	
	public double getAmount() {return this.amount;}
}
