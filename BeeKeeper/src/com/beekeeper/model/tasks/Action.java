package com.beekeeper.model.tasks;

import com.beekeeper.model.agent.WorkingAgentServices;

public abstract class Action implements TaskNode {
	
	public double totalTimeSteps = 1;
	public double energyCost;	
	public int timeSteps = 0;
	
	protected WorkingAgentServices agentServices;
	
	public Action(double durationSec, double energyCost, WorkingAgentServices s)
	{
		this.totalTimeSteps = durationSec;
		this.energyCost = energyCost;
		this.agentServices = s;
	}

	@Override
	public abstract Action execute();

	@Override
	public abstract boolean check();

	
	/**
	 * Returns True if the action is done, False otherwise
	*/
	public boolean run()
	{
		this.execute();
		return upkeep();
	}

	/**
	 * Returns True if the action is done, False otherwise
	*/
	protected boolean upkeep()
	{

		agentServices.addToEnergy(-energyCost);
		return advanceTimeStep();
	}
	
	/**
	 * Returns True if the action is done, False otherwise
	*/
	protected boolean advanceTimeStep()
	{
		timeSteps++;
		if(timeSteps >= totalTimeSteps)
		{
			timeSteps = 0;
			return true;
		}
		
		return false;
	}
	
	

}
