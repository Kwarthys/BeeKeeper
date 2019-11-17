package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class RandomMoveTask extends Task
{
	
	public RandomMoveTask(WorkingAgentServices agentServices)
	{
		super(agentServices);
		
		taskName = "Random Walk";
		energyCost = 0.01;
		
		rootActivity.addTaskNode(new Action(2) {
			
			@Override
			public Action execute() {
				//System.out.println("RandomAction ! " + agentServices.getEnergy());
				agentServices.randomMove();
				agentServices.emit(Stimulus.StimulusA, 2);
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
		return agentServices.getEnergy();
	}
}
