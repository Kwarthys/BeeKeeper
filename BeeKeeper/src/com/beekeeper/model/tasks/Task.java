package com.beekeeper.model.tasks;

import com.beekeeper.parameters.ModelParameters;

public abstract class Task
{
	public double threshold;
	public double energyCost;

	public void learn()
	{
		threshold += ModelParameters.TASK_LEARN_RATE;
	}
	
	public void forget()
	{
		threshold += ModelParameters.TASK_FORGET_RATE;
	}
	
	public abstract void execute();
	
	public void interrupt(){}
}
