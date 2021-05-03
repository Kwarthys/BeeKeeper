package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.agent.implem.BroodBee.LarvalState;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public class EggTask extends Task {
	
	public static String EggTaskName = "EggTask";

	public EggTask(WorkingAgentServices agentServices) {
		super(agentServices, EggTaskName);
		
		this.rootActivity.addTaskNode(new Action(1, 0, agentServices) {
			
			@Override
			public void execute() {
				agentServices.emit(Stimulus.EthyleOleate, ModelParameters.EGG_EO_TIMELY_EMMISION);
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return agentServices.getLarvalState() == LarvalState.Egg ? 1 : 0; 
	}

}
