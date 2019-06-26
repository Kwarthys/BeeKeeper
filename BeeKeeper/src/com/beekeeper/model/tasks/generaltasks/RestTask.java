package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Task;

public class RestTask extends Task
{
	private WorkingAgent agent;
	
	public RestTask(WorkingAgent a)
	{
		agent = a;
		
		this.energyCost = -0.01;
		this.taskName = "Rest";
	}

	@Override
	public boolean checkInterrupt(StimuliMap load) {
		return agent.getEnergy() > 0.9;
	}

	@Override
	public double compute(StimuliMap load) {
		return agent.getEnergy() < 0.2 ? 1:0;
	}

	@Override
	public void execute(){}

	@Override
	public void interrupt()
	{
		agent.interruptTask();
	}

}
