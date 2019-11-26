package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Activity;
import com.beekeeper.model.tasks.Task;

public class FeedLarva extends Task {

	private boolean moved = false;
	private boolean feeding = false;
	
	public FeedLarva(WorkingAgentServices agentServices)
	{
		super(agentServices, "FeedLarvae");
		
		Activity larvaNear = new Activity() {			
			@Override
			public boolean check() {
				return moved || feeding;
			}
		};
		
		//FeedLarva
		larvaNear.addTaskNode(new Action(1,0,agentServices) {
			
			@Override
			public Action execute() {
				WorkingAgent larva = agentServices.getCoopInteractor();
				
				if(larva.isHungry())
				{
					larva.recieveFood();
					System.out.println("FeedLarva - Larva fed");
				}
				else
				{
					feeding = false;
				}
				return this;
			}
			
			@Override
			public boolean check() {
				return feeding;
			}
		});
		
		//CheckLarva
		larvaNear.addTaskNode(new Action(0.1,0,agentServices) {			
			@Override
			public Action execute() {
				WorkingAgent larva = agentServices.getCoopInteractor();
				
				if(larva.isHungry())
				{
					feeding = true;
				}
				else
				{
					agentServices.resetCoopInteractor();
					feeding = false;
					System.out.println("FeedLarva - LarvaNotHungry");
				}
				return this;
			}
			
			@Override
			public boolean check() {
				return agentServices.getCoopInteractor() != null;
			}
		});
		
		//Inspect Cell
		larvaNear.addTaskNode(new Action(0.1,0,agentServices) {
			
			@Override
			public Action execute() {
				if(agentServices.getHostCell().content == CellContent.brood)
				{
					agentServices.setInteractorTo(agentServices.getHostCell().getAgentInside());
				}
				moved = false;
				System.out.println("FeedLarva - CheckingCell");
				return this;
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
		
		this.rootActivity.addTaskNode(larvaNear);
		//Random Move
		this.rootActivity.addTaskNode(new Action(0.1,0.001,agentServices) {			
			@Override
			public Action execute() {
				moved = true;
				agentServices.randomMove();
				agentServices.dropMotivation();
				System.out.println("FeedLarva - RandomMove");
				return this;
			}			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap)
	{
		// TODO YOUNG PHYSIOLOGICAL STATE
		return 0.9;
	}

}
