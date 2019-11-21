package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class GiveFoodTask extends Task {

	public GiveFoodTask(WorkingAgentServices agentServices) {
		super(agentServices);
		this.taskName = "Give Food";
		
		this.rootActivity.addTaskNode(new Action(2,0, agentServices) {
			
			@Override
			public Action execute() {
				agentServices.giveFoodToClosestHungry();
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
		return this.thresholdSigmoid(smap.getAmount(Stimulus.AskFood));
	}

}
