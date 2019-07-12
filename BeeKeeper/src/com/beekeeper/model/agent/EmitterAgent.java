package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public abstract class EmitterAgent extends Agent
{	
	protected StimuliLoad stimuliLoad;
	protected StimuliManagerServices stimuliManagerServices;

	public EmitterAgent(StimuliManagerServices stimuliManagerServices, double x, double y)
	{
		super(x,y);
		this.stimuliManagerServices = stimuliManagerServices;
		
		this.stimuliLoad = new StimuliLoad(this.position);
	}

	public StimuliLoad getStimuliLoad() {return this.stimuliLoad;}
}
