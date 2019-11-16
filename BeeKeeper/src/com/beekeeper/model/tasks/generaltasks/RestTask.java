package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class RestTask extends Task
{	
	public RestTask(WorkingAgent a)
	{
		this.agent = a;
		
		this.energyCost = -0.01;
		this.taskName = "Rest";
		
		rootActivity.addTaskNode(new Action() {			
			@Override
			public void execute() {
				System.out.println("Resting");
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return 0.2;
	}
}
