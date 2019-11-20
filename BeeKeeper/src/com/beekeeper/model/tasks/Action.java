package com.beekeeper.model.tasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.parameters.ModelParameters;

public abstract class Action implements TaskNode {
	
	protected double totalTimeSteps = 1;
	protected double energyCost;	
	protected int timeSteps = 0;
	
	protected boolean actionOver = false;
	
	protected WorkingAgentServices agentServices;
	
	public Action(double durationSec, double energyCost, WorkingAgentServices s)
	{
		this.totalTimeSteps = durationSec * ModelParameters.secondToTimeStepCoef;
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
	public void run()
	{
		this.execute();
		upkeep();
	}

	/**
	 * Returns True if the action is done, False otherwise
	*/
	protected void upkeep()
	{
		agentServices.addToEnergy(-energyCost);
		advanceTimeStep();
	}
	
	public boolean isOver()
	{
		if(actionOver)
		{
			actionOver = false;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns True if the action is done, False otherwise
	*/
	protected void advanceTimeStep()
	{
		timeSteps++;
		if(timeSteps >= totalTimeSteps)
		{
			timeSteps = 0;
			actionOver = true;
		}
	}
	
	

}
