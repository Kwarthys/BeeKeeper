package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Activity;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public class FeedLarva extends Task {

	private boolean moved = false;
	private boolean feeding = false;
	
	public FeedLarva(WorkingAgentServices agentServices)
	{
		super(agentServices, "FeedLarvae");
		
		Activity larvaNear = new Activity() {			
			@Override
			public boolean check() {
				return moved || agentServices.getCoopInteractor() != null;
			}
		};
		
		//FeedLarva
		larvaNear.addTaskNode(new Action(ModelParameters.LARVA_FEEDING_MEANDURATION,0,agentServices) {
			
			@Override
			public void execute() {
				WorkingAgent larva = agentServices.getCoopInteractor();
				
				if(!feeding)
				{
					return;
				}
				
				if(larva.isHungry())
				{
					larva.recieveFood();
					//System.out.println("FeedLarva - Larva fed");
				}
				
				//CheckIfNotHungryAnymore
				if(!larva.isHungry())
				{
					feeding = false;
					agentServices.resetCoopInteractor();
				}
			}
			
			@Override
			public boolean check() {
				return feeding && agentServices.getCoopInteractor()!=null;
			}
		});
		
		//CheckLarva
		larvaNear.addTaskNode(new Action(0.5,0,agentServices) {			
			@Override
			public void execute() {
				WorkingAgent larva = agentServices.getCoopInteractor();
				
				if(larva.isHungry())
				{
					feeding = true;
					//System.out.println("FeedLarva - Starting food distribution");
				}
				else
				{
					agentServices.resetCoopInteractor();
					feeding = false;
					//System.out.println("FeedLarva - LarvaNotHungry");
				}
			}
			
			@Override
			public boolean check() {
				return agentServices.getCoopInteractor() != null;
			}
		});
		
		//Inspect Cell
		larvaNear.addTaskNode(new Action(0.1,0,agentServices) {
			
			@Override
			public void execute() {
				if(agentServices.getHostCell().content == CellContent.brood)
				{
					agentServices.setInteractorTo(agentServices.getHostCell().getAgentInside());
				}
				moved = false;
				//System.out.println("FeedLarva - CheckedCell");
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
		
		this.rootActivity.addTaskNode(larvaNear);
		//Random Move
		this.rootActivity.addTaskNode(new Action(0.2,0.001,agentServices) {			
			@Override
			public void execute() {
				moved = true;
				agentServices.randomMove();
				agentServices.dropMotivation();
				//System.out.println("FeedLarva - RandomMoved");
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
		this.threshold = 0.3 + 0.7*agentServices.getHJTiter();
		return this.thresholdSigmoid(0.5);
	}

}
