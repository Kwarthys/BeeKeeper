package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public abstract class EmitterAgent extends Agent
{
	protected StimuliMap bodySmell;
	protected StimuliManagerServices stimuliManagerServices;

	public EmitterAgent(StimuliManagerServices stimuliManagerServices)
	{
		this.stimuliManagerServices = stimuliManagerServices;
		
		this.bodySmell = new StimuliMap();
	}
	
	public void emit(Stimulus s, double amount)
	{
		stimuliManagerServices.emit(s, amount, null);
		//TODO replace NULL by something meaningful
	}

	public StimuliMap getBodySmells() {return this.bodySmell;}
}