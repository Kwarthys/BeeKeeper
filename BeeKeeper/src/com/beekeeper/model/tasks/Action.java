package com.beekeeper.model.tasks;

public abstract class Action implements TaskNode {
	
	public double totalTimeSteps = 1;
	
	public int timeSteps = 0;
	
	public Action(double durationSec)
	{
		totalTimeSteps = durationSec;
	}

	@Override
	public abstract Action execute();

	@Override
	public abstract boolean check();
	
	/** Returns False if action is not finished, true otherwise
	*/
	public boolean advanceTimeStep()
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
