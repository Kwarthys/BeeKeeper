package com.beekeeper.model.agent;

import java.awt.Point;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public abstract class EmitterAgent extends Agent
{
	protected StimuliMap bodySmell;
	public StimuliMap getBodySmells() {return new StimuliMap(bodySmell);}
	
	public double getTotalExchangedAmount()
	{
		return bodySmell.getTotalExchangedAmount();
	}
	
	protected StimuliManagerServices stimuliManagerServices;

	public EmitterAgent(StimuliManagerServices stimuliManagerServices)
	{
		this.stimuliManagerServices = stimuliManagerServices;
		
		this.bodySmell = new StimuliMap();
		this.bodySmell.agentID = ID;
	}
	
	protected void emit(Stimulus s, double amount)
	{
		this.stimuliManagerServices.emit(s, amount, new Point(hostCell.x, hostCell.y));
	}
	
	
	@Override
	public void registerNewStimuliManagerServices(StimuliManagerServices stimuliManagerServices)
	{
		this.stimuliManagerServices = stimuliManagerServices;
	}
	
	public int getStimuliManagerServicesID()
	{
		return stimuliManagerServices.getId();
	}
	
}
