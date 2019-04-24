package com.beekeeper.model.tasks;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.parameters.ModelParameters;

public abstract class Task
{
	public double threshold = 3;
	public double energyCost;
	public String taskName = "default";

	public void learn()
	{
		threshold += ModelParameters.TASK_LEARN_RATE;
	}
	
	public void forget()
	{
		threshold += ModelParameters.TASK_FORGET_RATE;
	}
	
	public double thresholdSigmoid(double s)
	{
		return s*s / ( s*s + threshold*threshold);
	}
	
	public abstract boolean checkInterrupt(StimuliMap load);
	
	public abstract double compute(StimuliMap load);
	
	public abstract void execute();
	
	public abstract void interrupt();
}
