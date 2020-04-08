package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class AskFoodTask extends Task {

	private boolean cellChecked = false;
	private boolean foundFood = false;

	public AskFoodTask(WorkingAgentServices agentServices) {
		super(agentServices, "Asking Food");
		
		this.motivated = false;
		
		//eating at cell
		this.rootActivity.addTaskNode(new Action(2, 0, agentServices) {			
			@Override
			public void execute() {
				agentServices.receiveFood();
				foundFood = false;
			}
			
			@Override
			public boolean check() {
				return foundFood;
			}
		});
		
		//checking cell
		this.rootActivity.addTaskNode(new Action(0.5, 0, agentServices) {
			
			@Override
			public void execute() {
				cellChecked = true;
				if(agentServices.getHostCell().content == CellContent.food)
				{
					foundFood = true;
				}
			}
			
			@Override
			public boolean check() {
				return !cellChecked;
			}
		});
		
		//RandomMove
		this.rootActivity.addTaskNode(new Action(0.5,0.001,agentServices) {			
			@Override
			public void execute() {
				agentServices.randomMove();	
				foundFood = false;
				cellChecked = false;
			}
			
			@Override
			public boolean check() {
				return (Math.random() > 0.95 || !agentServices.agentNearby()) && !agentServices.isReceivingFood();
			}
		});
		
		//Asking
		this.rootActivity.addTaskNode(new Action(0.1, 0, agentServices) {
			
			@Override
			public void execute() {
				agentServices.emit(Stimulus.AskFood, 15);
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return smap.getAmount(Stimulus.HungerBee)*0.9;
	}

}
