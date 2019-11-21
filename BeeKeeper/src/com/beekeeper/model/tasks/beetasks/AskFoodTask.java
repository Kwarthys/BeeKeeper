package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class AskFoodTask extends Task {

	public AskFoodTask(WorkingAgentServices agentServices) {
		super(agentServices);
		this.taskName = "Asking Food";
		
		this.rootActivity.addTaskNode(new Action(0.5, 0, agentServices) {
			
			@Override
			public Action execute() {
				if(Math.random() > 0.8)
					agentServices.randomMove();
				agentServices.emit(Stimulus.AskFood, 10);
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
		// TODO Auto-generated method stub
		return agentServices.getHunger();
	}

}
