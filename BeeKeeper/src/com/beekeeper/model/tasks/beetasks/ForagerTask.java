package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class ForagerTask extends Task {
	
	private boolean back = false;
	private int c = 0;

	public ForagerTask(WorkingAgentServices agentServices) {
		super(agentServices, "Foraging");
		
		//Wandering inHive
		this.rootActivity.addTaskNode(new Action(0.2,0,agentServices) {
			@Override
			public void execute() {
				//System.out.println(agentServices.getID() + " Wandering");
				agentServices.dropMotivation();
				agentServices.randomMove();
				c++;
			}
			
			@Override
			public boolean check() {
				return back && c < 10;
			}
		});
		
		//Foraging outside
		this.rootActivity.addTaskNode(new Action(20,0,agentServices) {
			@Override
			public void execute() {
				//System.out.println(agentServices.getID() + " BackFromForaging");
				agentServices.dropMotivation();
				agentServices.enterHive(); //find a comb and setInside to true
				back = true;
				c=0;
			}
			
			@Override
			public boolean check() {
				return !agentServices.isInside();
			}
		});
		
		//Go Out
		this.rootActivity.addTaskNode(new Action(0.2,0,agentServices) {
			@Override
			public void execute() {
				//System.out.println(agentServices.getID() + " Moved Down");
				back = false;
				agentServices.tryMoveDown();
			}
			
			@Override
			public boolean check() {
				return agentServices.isInside();
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		this.threshold = 1-(agentServices.getHJTiter()*0.7);
		return this.thresholdSigmoid(0.5);
	}

}
