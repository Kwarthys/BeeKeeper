package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class RestTask extends Task
{	
	public RestTask(WorkingAgentServices agentServices)
	{
		super(agentServices);
		this.taskName = "Rest";
		
		rootActivity.addTaskNode(new Action(1,-0.05,agentServices) {			
			@Override
			public Action execute() {
				//System.out.println("Resting");
				agentServices.dropMotivation();
				
				return this;
			}
			
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
