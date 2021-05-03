package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.agent.implem.BroodBee.LarvalState;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class NympheaTask extends Task {
	
	public static String NympheaTaskName = "NympheaTask";

	public NympheaTask(WorkingAgentServices agentServices)
	{
		super(agentServices, NympheaTaskName);
		
		this.rootActivity.addTaskNode(new Action(1, 0, agentServices) {
			
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
		return agentServices.getLarvalState() == LarvalState.Nymphea ? 1 : 0; 
	}

}
