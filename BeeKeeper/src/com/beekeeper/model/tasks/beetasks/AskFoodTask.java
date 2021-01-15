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
	
	public static final String AskingFoodTaskName = "AskingFood";

	public AskFoodTask(WorkingAgentServices agentServices) {
		super(agentServices, AskingFoodTaskName);
		
		this.motivated = false;
		
		//eating at cell
		this.rootActivity.addTaskNode(new Action(30, 0, agentServices) {		
			@Override
			public void execute() {
				//if(agentServices.getID() == 1000)System.out.println(agentServices.getID() + " eating at cell");	
				agentServices.receiveFood();
				foundFood = false;
			}
			
			@Override
			public boolean check() {
				return foundFood;
			}
		});
		
		//checking cell
		this.rootActivity.addTaskNode(new Action(10, 0, agentServices) {
			
			@Override
			public void execute() {
				//if(agentServices.getID() == 1000)System.out.println(agentServices.getID() + " checking cell");
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
		this.rootActivity.addTaskNode(new Action(1,0.001,agentServices) {			
			@Override
			public void execute() {
				//if(agentServices.getID() == 1000)System.out.println(agentServices.getID() + " RandomMove");
				if(Math.random() > 0.05)
				{
					agentServices.randomMove();	
				}
				else
				{
					agentServices.tryMoveDown(false);
				}
				foundFood = false;
				cellChecked = false;
			}
			
			@Override
			public boolean check() {
				return (Math.random() > 0.99 || !agentServices.agentNearby()) && !agentServices.isReceivingFood();
			}
		});
		
		//Asking
		this.rootActivity.addTaskNode(new Action(1, 0, agentServices) {
			
			@Override
			public void execute() {
				//if(agentServices.getID() == 1000)System.out.println(agentServices.getID() + " emits");
				agentServices.emit(Stimulus.AskFood, 400);
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
