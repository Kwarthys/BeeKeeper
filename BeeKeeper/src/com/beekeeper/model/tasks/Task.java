package com.beekeeper.model.tasks;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

public abstract class Task
{
	public double threshold = 5;
	public double energyCost;
	public String taskName = "default";
	public int midDuration = 20;
	public boolean printLearning = false;

	public void learn()
	{
		threshold -= ModelParameters.TASK_LEARN_RATE;
		checkThresholdBoundary();
	}
	
	public void forget()
	{
		threshold += ModelParameters.TASK_FORGET_RATE;
		checkThresholdBoundary();
	}
	
	private void checkThresholdBoundary()
	{
		threshold = threshold < 1 ? 1 : threshold > ModelParameters.MAX_TASK_THRESHOLD ? ModelParameters.MAX_TASK_THRESHOLD : threshold;
	}
	
	public double thresholdSigmoid(double s)
	{
		return MyUtils.sigmoid(s, threshold);
	}
	
	public abstract boolean checkInterrupt(StimuliMap load);
	
	public abstract double compute(StimuliMap load);
	
	public abstract void execute();
	
	public abstract void interrupt();
}
