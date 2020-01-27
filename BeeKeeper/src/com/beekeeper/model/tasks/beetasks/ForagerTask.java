package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class ForagerTask extends Task {
	
	private boolean back = false;
	private int c = 0;

	public ForagerTask(WorkingAgentServices agentServices)
	{
		super(agentServices, "Foraging");		
		
		//Wandering inHive
		this.rootActivity.addTaskNode(new Action(0.2,0,agentServices) {
			@Override
			public void execute() {
				//System.out.println(agentServices.getID() + " Wandering");
				if(!agentServices.tryMoveUp())
				{
					agentServices.dropMotivation();
				}
				c++;
			}
			
			@Override
			public boolean check() {
				return back && agentServices.isInside() && c < 20;
			}
		});
		
		//Entering Hive
		this.rootActivity.addTaskNode(new Action(0.2,0,agentServices) {
			@Override
			public void execute() {
				agentServices.enterHive();
				c=0;
			}
			
			@Override
			public boolean check() {
				return !agentServices.isInside() && back;
			}
		});
		
		//Foraging outside
		this.rootActivity.addTaskNode(new Action(20,0,agentServices) {
			@Override
			public void execute() {
				back = true;
			}
			
			@Override
			public boolean check() {
				return !agentServices.isInside() && !back;
			}
		});
		
		//Go Out
		this.rootActivity.addTaskNode(new Action(0.2,0,agentServices) {
			@Override
			public void execute() {
				ForagerTask.this.motivated = false;
				back = false;
				if(!agentServices.tryMoveDown())
				{
					agentServices.dropMotivation();
				}
				
				if(!agentServices.isInside())
				{
					agentServices.resetMotivation();
					ForagerTask.this.motivated = true;
				}
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
