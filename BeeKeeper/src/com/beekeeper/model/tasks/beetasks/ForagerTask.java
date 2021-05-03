package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public class ForagerTask extends Task {
	
	private volatile boolean back = false;
	private int c = 0;

	//private int trackedIdsUp = 1000;
	//private int trackedIdsDown = 998;
	
	public static final String foragingTaskName = "Foraging";

	public ForagerTask(WorkingAgentServices agentServices)
	{
		super(agentServices, foragingTaskName);	
		
		this.motivated = true;
		
		//Wandering inHive
		this.rootActivity.addTaskNode(new Action(1,0,agentServices) {
			@Override
			public void execute() {
				//if(agentServices.getID() < trackedIdsUp && agentServices.getID() > trackedIdsDown)System.out.println(agentServices.getID() + " Wandering");
				if(!agentServices.tryMoveUp())
				{
					agentServices.dropMotivation();
				}
				
				if(c < 2)
				{
					agentServices.killMotivation();
				}
				c--;
			}
			
			@Override
			public boolean check() {
				return back && agentServices.isInside() && c > 0;
			}
		});
		
		//Entering Hive
		this.rootActivity.addTaskNode(new Action(1,0,agentServices) {
			@Override
			public void execute() {
				agentServices.tryEnterHive();
				//if(agentServices.getID() < trackedIdsUp && agentServices.getID() > trackedIdsDown)System.out.println(agentServices.getID() + " Entering - " + entered);
				c = (int) (Math.random() * 20 + 10);
			}
			
			@Override
			public boolean check() {
				return !agentServices.isInside() && back;
			}
		});
		
		//Foraging outside
		this.rootActivity.addTaskNode(new Action(ModelParameters.FORAGING_TIME, ModelParameters.FORAGING_ENERGYCOST,agentServices) {//200,0,age...
			@Override
			public void execute() {
				//if(agentServices.getID() < trackedIdsUp && agentServices.getID() > trackedIdsDown)System.out.println(agentServices.getID() + " go forage");
				back = true;
				agentServices.resetMotivation();
			}
			
			@Override
			public boolean check() {
				return !agentServices.isInside() && !back;
			}
		});
		
		//Go Out
		this.rootActivity.addTaskNode(new Action(1,0,agentServices) {
			@Override
			public void execute() {
				back = false;
				if(!agentServices.tryMoveDown(true))
				{
					agentServices.dropMotivation();
				}
				
				//if(agentServices.getID() < trackedIdsUp && agentServices.getID() > trackedIdsDown)System.out.println(agentServices.getID() + " MovingOut out?-" + agentServices.isInside());
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
		
		double s = 0;
		
		if(agentServices.getHJTiter() < 0.5)
		{
			s = 0;
		}
		else
		{
			s = smap.getAmount(Stimulus.Energy) - ModelParameters.FORAGING_ENERGYCOST;
		}
		
		
		//s = agentServices.getHJTiter() < 0.5 ? 0 : smap.getAmount(Stimulus.Energy) > 0.7 ? 0.5 : 0;
		
		return this.thresholdSigmoid(s);
	}

}
