package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class AskFoodTask extends Task {

	public AskFoodTask(WorkingAgentServices agentServices) {
		super(agentServices, "Asking Food");
		
		this.motivated = false;
		
		this.rootActivity.addTaskNode(new Action(0.5,0.001,agentServices) {			
			@Override
			public void execute() {
				agentServices.randomMove();				
			}
			
			@Override
			public boolean check() {
				return Math.random() > 0.9 && !agentServices.isReceivingFood();
			}
		});
		
		this.rootActivity.addTaskNode(new Action(0.1, 0, agentServices) {
			
			@Override
			public void execute() {
				agentServices.emit(Stimulus.AskFood, 15);
				//System.out.println(agentServices.getID() + " asking food");
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return smap.getAmount(Stimulus.HungerBee);
	}

}
