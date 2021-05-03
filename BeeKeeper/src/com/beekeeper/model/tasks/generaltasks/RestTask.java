package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public class RestTask extends Task
{	
	public static final String restTaskName = "Rest";
	
	public RestTask(WorkingAgentServices agentServices)
	{
		super(agentServices, restTaskName);
		
		this.motivated = false;
		
		rootActivity.addTaskNode(new Action(1,-ModelParameters.RESTTASK_RESTORATION,agentServices) {			
			@Override
			public void execute() {
				//if(agentServices.getID() == 900)System.out.println("Resting: e" + agentServices.getEnergy());
				if(Math.random() > 0.95)
				{
					agentServices.randomMove();
				}
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
