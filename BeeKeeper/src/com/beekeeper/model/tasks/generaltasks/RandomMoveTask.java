package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Task;

public class RandomMoveTask extends Task
{	
	public static final String randomWalkTaskName = "RandomWalk";
	
	public RandomMoveTask(WorkingAgentServices s)
	{
		super(s, randomWalkTaskName);
		
		rootActivity.addTaskNode(new RandomMoveAction(s));
	}

	@Override
	public double compute(StimuliMap smap) {
		return Math.min(0.3,agentServices.getEnergy());
	}
}
