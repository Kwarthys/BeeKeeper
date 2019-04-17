package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.external.ExternalStimuliLoad;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public abstract class EmptyBee extends Agent
{
	protected abstract void fillTaskList();
	private double energy;
	protected ExternalStimuliLoad pheromoneLoad;
	protected StimuliManagerServices stimuliManagerServices;
	
	protected BeeType type;
	
	public EmptyBee(StimuliManagerServices stimuliManagerServices)
	{
		this.stimuliManagerServices = stimuliManagerServices;
		setEnergy(Math.random()*0.8+0.2);
	}
	
	public BeeType getBeeType() {return this.type;}
	
	public void setEnergy(double amount)
	{
		this.energy = 0;
		addToEnergy(amount);
	}
	
	public double getEnergy() {return this.energy;}
	public ExternalStimuliLoad getExternalStimuli() {return this.pheromoneLoad;}
	
	protected void addToEnergy(double amount)
	{
		this.energy += amount;
		this.energy = this.energy < 0 ? 0 : this.energy > 1 ? 1 : this.energy; //Clamp energy between 0 and 1
	}
}
