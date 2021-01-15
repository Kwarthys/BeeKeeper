package com.beekeeper.model.tasks;

import com.beekeeper.model.agent.WorkingAgentServices;

public abstract class Action implements TaskNode {
	
	protected double totalTimeSteps = 1;
	protected double energyCost;	
	protected int timeSteps = 0;
	
	protected boolean actionOver = false;
	
	protected WorkingAgentServices agentServices;
	
	public Action(double durationTimeStep, double energyCost, WorkingAgentServices s)
	{
		this.totalTimeSteps = Math.max(durationTimeStep, 1);
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
	
	public void randomizeLenght(int var)
	{
		totalTimeSteps += (Math.random() * var)-(var/2);
	}
	
	/**
	 * Used to unlock things that are locked by the execution
	 */
	public void notifyOver() {}


	public void run()
	{		
		if(timeSteps == 0)
		{
			execute();			
		}
		
		upkeep();
		
		if(actionOver)
		{
			notifyOver();
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
		if(++timeSteps >= totalTimeSteps)
		{
			timeSteps = 0;
			actionOver = true;
		}
	}
	
	

}
