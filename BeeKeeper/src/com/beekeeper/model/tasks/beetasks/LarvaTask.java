package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public class LarvaTask extends Task {

	public LarvaTask(WorkingAgentServices agentServices)
	{
		super(agentServices, "LarvaTask");

		this.rootActivity.addTaskNode(new Action(1, ModelParameters.LARVAE_HUNGER_INCREMENT, agentServices) {
			
			@Override
			public void execute() {
				agentServices.emit(Stimulus.EthyleOleate, ModelParameters.LARVA_EO_TIMELY_EMMISION);
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
