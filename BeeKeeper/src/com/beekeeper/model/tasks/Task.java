package com.beekeeper.model.tasks;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.utils.MyUtils;

public abstract class Task
{
	public double threshold = 0.5;
	public double energyCost;
	public String taskName = "default";
	//public int midDuration = 20;
	public boolean printLearning = false;

	/*
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
		threshold = MyUtils.clamp(threshold, ModelParameters.MIN_TASK_THRESHOLD, ModelParameters.MAX_TASK_THRESHOLD);
	}
	*/
	
	public double thresholdSigmoid(double s)
	{
		return MyUtils.sigmoid(s, threshold);
	}
	
	public abstract boolean checkInterrupt(StimuliMap load);
	
	public abstract double compute(StimuliMap load);
	
	public abstract void execute();
	
	public abstract void interrupt();
}
