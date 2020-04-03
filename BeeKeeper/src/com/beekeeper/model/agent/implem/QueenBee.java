package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.beetasks.AskFoodTask;
import com.beekeeper.model.tasks.beetasks.QueenTask;
import com.beekeeper.model.tasks.generaltasks.RestTask;

public class QueenBee extends WorkingAgent {

	public QueenBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices) {
		super(stimuliManagerServices, controllerServices);
		this.type = AgentType.QUEEN;
		
		this.currentTask = taskList.get(0);
	}

	@Override
	protected void fillTaskList() {
		taskList.add(new QueenTask(ownServices));
		taskList.add(new RestTask(ownServices));
		taskList.add(new AskFoodTask(ownServices));
	}

	@Override
	protected void advanceMetabolism()
	{
		
	}

	@Override
	public String getStringName() {
		return "Queen " + ID;
	}
	
	@Override
	protected void layEgg()
	{
		controllerServices.layEgg(this.hostCell);
	}

}
