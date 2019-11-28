package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class ForagerTask extends Task {

	public ForagerTask(WorkingAgentServices agentServices) {
		super(agentServices, "Foraging");
		
		this.rootActivity.addTaskNode(new Action(20,0,agentServices) {
			@Override
			public void execute() {
				System.out.println("BackFromForaging");
			}
			
			@Override
			public boolean check() {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		this.threshold = 1-agentServices.getHJTiter();
		return this.thresholdSigmoid(0.5);
	}

}
