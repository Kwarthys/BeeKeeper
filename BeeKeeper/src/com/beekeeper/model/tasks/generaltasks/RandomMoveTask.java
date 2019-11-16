package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class RandomMoveTask extends Task
{
	
	public RandomMoveTask(WorkingAgent a)
	{
		this.agent = a;
		
		taskName = "Random Walk";
		energyCost = 0.1;
		
		rootActivity.addTaskNode(new Action() {
			
			@Override
			public void execute() {
				System.out.println("RandomAction ! " + agent.getEnergy());
				agent.randomMove();
				agent.emit(Stimulus.StimulusA, 1);
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return agent.getEnergy();
	}
}
