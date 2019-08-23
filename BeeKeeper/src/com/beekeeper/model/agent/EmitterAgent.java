package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public abstract class EmitterAgent extends Agent
{
	protected StimuliMap bodySmell;
	protected StimuliManagerServices stimuliManagerServices;

	public EmitterAgent(StimuliManagerServices stimuliManagerServices, double x, double y)
	{
		super(x,y);
		this.stimuliManagerServices = stimuliManagerServices;
		
		this.bodySmell = new StimuliMap();
	}
	
	public void emit(Stimulus s, double amount)
	{
		stimuliManagerServices.emit(s, amount, getPosition());
	}

	public StimuliMap getBodySmells() {return this.bodySmell;}
}
