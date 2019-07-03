package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public abstract class EmitterAgent extends Agent
{
	private double energy;
	
	protected StimuliLoad stimuliLoad;
	protected StimuliManagerServices stimuliManagerServices;

	public EmitterAgent(StimuliManagerServices stimuliManagerServices, double x, double y)
	{
		super(x,y);
		this.stimuliManagerServices = stimuliManagerServices;
		
		this.stimuliLoad = new StimuliLoad(this.position);
	}

	public StimuliLoad getStimuliLoad() {return this.stimuliLoad;}

	
	public double getEnergy() {return this.energy;}
	
	public void addToEnergy(double amount)
	{
		this.energy += amount;
		this.energy = this.energy < 0 ? 0 : this.energy > 1 ? 1 : this.energy; //Clamp energy between 0 and 1
	}
	
	public void setEnergy(double amount)
	{
		this.energy = 0;
		addToEnergy(amount); //ensuring energy stays in [0;1]
	}
}
