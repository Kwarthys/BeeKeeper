package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class RestTask extends Task
{	
	public static final String restTaskName = "Rest";
	
	public RestTask(WorkingAgentServices agentServices)
	{
		super(agentServices, restTaskName);
		
		this.motivated = false;
		
		rootActivity.addTaskNode(new Action(1,-0.1,agentServices) {			
			@Override
			public void execute() {}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return 1-this.agentServices.getEnergy();
	}
}
