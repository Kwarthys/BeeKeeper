package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.beetasks.LarvaTask;

public class BroodBee extends WorkingAgent
{
	public BroodBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices) {
		super(stimuliManagerServices, controllerServices);
		this.type = AgentType.BROOD_BEE;
		if(this.getEnergy() < 0.5)
		{
			this.addToEnergy(0.5);
		}
	}
	
	@Override
	public boolean isHungry() {
		return this.getEnergy() < 0.8;
	}
	
	@Override
	public void recieveFood() {
		//System.out.print(getStringName() + " got fed : " + getEnergy());
		this.addToEnergy(0.3);
		//System.out.println(" to " + getEnergy());
	}

	@Override
	protected void fillTaskList() 
	{
		this.taskList.add(new LarvaTask(ownServices));
	}

	@Override
	protected void advanceMetabolism()
	{
		this.bodySmell.addAmount(Stimulus.EthyleOleate, 1);
	}

	@Override
	public String getStringName() {
		return "BroodBee " + ID;
	}
}
