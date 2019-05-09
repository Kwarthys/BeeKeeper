package com.beekeeper.model.tasks;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

public abstract class Task
{
	public double threshold = 3;
	public double energyCost;
	public String taskName = "default";
	public int midDuration = 20;

	public void learn(int duration)
	{
		System.out.print("Lasted " + duration + " mid is " + midDuration + " change of " + ModelParameters.TASK_LEARN_RATE * (MyUtils.sigmoid(duration, midDuration)*2-1));
		threshold -= ModelParameters.TASK_LEARN_RATE * (MyUtils.sigmoid(duration, midDuration)*2-1);
		System.out.println(" new TETA=" + threshold);
		checkThresholdBoundary();
	}
	
	private void checkThresholdBoundary()
	{
		threshold = threshold < 0 ? 0 : threshold > 10 ? 10 : threshold;
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
