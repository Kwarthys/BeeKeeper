package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class LarvaTask extends Task {

	public LarvaTask(WorkingAgentServices agentServices)
	{
		super(agentServices, "larvaTask");

		this.rootActivity.addTaskNode(new Action(1, 0.001, agentServices) {
			
			@Override
			public Action execute() {
				agentServices.emit(Stimulus.Ocimene, 10);
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
		return 1;
	}

}
