package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;

public class BroodBee extends WorkingAgent {

	public BroodBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices) {
		this(stimuliManagerServices, controllerServices, 200+Math.random()*100, 200+Math.random()*100);
	}

	public BroodBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, double x, double y) {
		super(stimuliManagerServices, controllerServices, x,y);
		this.type = AgentType.BROOD_BEE;
		if(this.getEnergy() < 0.5)
		{
			this.addToEnergy(0.5);
		}
		
		fillTaskList();
	}

	@Override
	public void move(double dx, double dy) {}

	@Override
	protected void fillTaskList() 
	{
		Task askFood = new Task() {			
			@Override
			public void execute() {
				BroodBee.this.emit(Stimulus.HungryLarvae, 1-BroodBee.this.getEnergy());				
			}

			@Override
			public double compute(StimuliMap load) {
				return getEnergy();
			}

			@Override
			public void interrupt() {
				BroodBee.this.currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				return getEnergy() > 0.5;
			}
		};	

		askFood.energyCost = 0.001;
		askFood.taskName = "LarvaeAskFood";

		taskList.add(askFood);
		
		
		
		Task rest = new Task() {			
			@Override
			public void execute(){}

			@Override
			public double compute(StimuliMap load) {
				return 1-getEnergy();
			}

			@Override
			public void interrupt() {
				BroodBee.this.currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				return getEnergy() < 0.5;
			}
		};	

		rest.energyCost = 0.001;
		rest.taskName = "LarvaeRest";

		taskList.add(rest);
	}
}
