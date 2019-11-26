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

	public abstract void execute();

	@Override
	public abstract boolean check();
	
	public Action search()
	{
		return this;
	}


	public void run()
	{
		upkeep();
		if(actionOver)
		{
			execute();
		}
	}

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
