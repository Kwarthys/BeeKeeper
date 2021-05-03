package com.beekeeper.model.tasks.beetasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public class QueenTask extends Task {

	private boolean cellFound = false;
	private boolean moved = false;
	
	public static final String queenTaskName = "QueenTask";
	
	private Action layEggAction;
	
	public void updateLayEggDuration(double duration)
	{
		layEggAction.setDurationTimeStep(duration);
	}

	public QueenTask(WorkingAgentServices agentServices) {
		super(agentServices, queenTaskName);
		
		this.motivated = false;

		//LAY EGG
		layEggAction = new Action(ModelParameters.LAYEGG_MEANDURATION, ModelParameters.QUEEN_TASKS_ENERGYCOSTS, agentServices) {
			
			@Override
			public void execute() {
				agentServices.layEgg();
				cellFound = false;
			}
			
			@Override
			public boolean check() {
				return cellFound;
			}
		};
		this.rootActivity.addTaskNode(layEggAction);

		//INSPECT CELL
		this.rootActivity.addTaskNode(new Action(1,ModelParameters.QUEEN_TASKS_ENERGYCOSTS, agentServices) {
			
			@Override
			public void execute() {
				cellFound = agentServices.getHostCell().content == CellContent.empty;
				moved = false;
			}
			
			@Override
			public boolean check() {
				return moved;
			}
		});

		//RANDOM MOVE
		this.rootActivity.addTaskNode(new Action(1,ModelParameters.QUEEN_TASKS_ENERGYCOSTS, agentServices) {
			
			@Override
			public void execute() {
				agentServices.randomMove();
				moved = true;
			}
			
			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return agentServices.getOvarianDev() > .99 ? ModelParameters.MAX_MOTIVATION : 0;
	}

}
