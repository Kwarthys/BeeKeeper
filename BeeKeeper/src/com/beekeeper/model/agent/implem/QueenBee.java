package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.beetasks.AskFoodTask;
import com.beekeeper.model.tasks.beetasks.QueenTask;
import com.beekeeper.model.tasks.generaltasks.RandomMoveTask;
import com.beekeeper.model.tasks.generaltasks.RestTask;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

public class QueenBee extends WorkingAgent {

	public QueenBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices) {
		super(stimuliManagerServices, controllerServices);
		this.type = AgentType.QUEEN;
		
		this.currentTask = taskList.get(0);
		this.ovarianDev = 0;
	}

	@Override
	protected void fillTaskList() {
		QueenTask qt = new QueenTask(ownServices);
		qt.updateLayEggDuration(ModelParameters.getQueenSpeedFromColonySize(controllerServices.getAllAdultsCount()));
		taskList.add(qt);
		
		taskList.add(new RestTask(ownServices));
		taskList.add(new AskFoodTask(ownServices));
		taskList.add(new RandomMoveTask(ownServices));
	}

	@Override
	protected void initPhysiology(boolean randomInit) {
		age = 0;
	}
	
	private int refreshRate = 2000;
	private int index = 0;

	@Override
	protected void advanceMetabolism()
	{
		//System.out.println(getStringName() + " e:" + getEnergy() + " - " + ModelParameters.QUEEN_TASKS_ENERGYCOSTS);
		if(index++ > refreshRate)
		{
			if(currentTask.taskName == QueenTask.queenTaskName)
			{
				/*** UPDATING QUEEN LAYING SPEED DEPENDING ON CURRENT COLONY SIZE, mimicking nurses adapting queen feeding to control laying rate ***/
				((QueenTask)currentTask).updateLayEggDuration(ModelParameters.getQueenSpeedFromColonySize(controllerServices.getAllAdultsCount()));
				//System.out.println("Updating target colony size at : " + controllerServices.getAllAgentsCount());
				

				index = 0;
			}
		}

		this.bodySmell.addAmount(Stimulus.EthyleOleate, ModelParameters.QUEEN_EO_TIMELY_EMMISION);
		ovarianDev += ModelParameters.QUEEN_OVARDEV_INCREMENT;
		
		hunger = MyUtils.clamp(hunger + ModelParameters.HUNGER_INCREMENT/2);
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
