package com.beekeeper.model.tasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.utils.MyUtils;

public abstract class Task
{
	protected Activity rootActivity = new Activity();
	
	public double threshold = 0.5;
	public String taskName;
	//public int midDuration = 20;
	public boolean printLearning = false;
	
	protected boolean motivated = true;
	public boolean isMotivated() {return motivated;}
	
	protected WorkingAgentServices agentServices;
	
	public Task(WorkingAgentServices agentServices, String taskName)
	{
		this.agentServices = agentServices;
		this.taskName = taskName;
	}
	
	protected double thresholdSigmoid(double s)
	{
		return MyUtils.sigmoid(s, threshold);
	}
	
	public abstract double compute(StimuliMap smap);
	
	public Action execute()
	{
		Action a = rootActivity.execute();
		a.upkeep();
		return a;
	}
}
