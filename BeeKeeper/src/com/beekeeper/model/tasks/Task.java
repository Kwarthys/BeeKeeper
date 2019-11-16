package com.beekeeper.model.tasks;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.utils.MyUtils;

public abstract class Task
{
	protected Activity rootActivity = new Activity();
	
	public double threshold = 0.5;
	public double energyCost;
	public String taskName = "default";
	//public int midDuration = 20;
	public boolean printLearning = false;
	
	protected WorkingAgent agent;
	
	protected double thresholdSigmoid(double s)
	{
		return MyUtils.sigmoid(s, threshold);
	}
	
	public abstract double compute(StimuliMap smap);
	
	public void execute()
	{
		rootActivity.execute();
		
		agent.addToEnergy(-energyCost);
	}
}
