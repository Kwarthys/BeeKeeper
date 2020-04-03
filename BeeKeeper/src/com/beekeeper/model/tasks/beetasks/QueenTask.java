package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class QueenTask extends Task {

	private boolean cellFound = false;
	private boolean moved = false;

	public QueenTask(WorkingAgentServices agentServices) {
		super(agentServices, "QueenTask");
		
		this.motivated = false;

		//LAY EGG
		this.rootActivity.addTaskNode(new Action(3,0.001, agentServices) {
			
			@Override
			public void execute() {
				agentServices.layEgg();
				System.out.println("Laid");
				cellFound = false;
			}
			
			@Override
			public boolean check() {
				return cellFound;
			}
		});

		//INSPECT CELL
		this.rootActivity.addTaskNode(new Action(0.2,0.001, agentServices) {
			
			@Override
			public void execute() {
				cellFound = agentServices.getHostCell().content == CellContent.empty;
				moved = false;
				System.out.println("Inspected");
			}
			
			@Override
			public boolean check() {
				return moved;
			}
		});

		//RANDOM MOVE
		this.rootActivity.addTaskNode(new Action(0.5,0.001, agentServices) {
			
			@Override
			public void execute() {
				agentServices.randomMove();
				moved = true;
				System.out.println("RandomMoved");
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return agentServices.getOvarianDev()/1.5;
	}

}
