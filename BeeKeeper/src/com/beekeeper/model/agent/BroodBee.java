package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.declarations.HungryLarvaeStimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;

public class BroodBee extends EmptyBee {

	public BroodBee(StimuliManagerServices stimuliManagerServices) {
		this(stimuliManagerServices, 200+Math.random()*100, 200+Math.random()*100);
	}

	public BroodBee(StimuliManagerServices stimuliManagerServices, double x, double y) {
		super(stimuliManagerServices,x,y);
		this.type = BeeType.BROOD_BEE;
		this.stimuliLoad = new StimuliLoad(this.position);
		if(this.getEnergy() < 0.5)
		{
			this.addToEnergy(0.5);
		}
		
		fillTaskList();
	}

	@Override
	public void live()
	{
		if(!this.alive)
		{
			return;
		}
		
		if(getEnergy() == 0)
		{
			this.alive = false;
			System.err.println(ID + " died of starvation, how sad.");
			return;
		}
		
		this.addToEnergy(-currentTask.energyCost);
		currentTask.execute();
		
		
	}

	@Override
	public void move(double dx, double dy) {}

	@Override
	protected void fillTaskList() 
	{
		Task askFood = new Task() {			
			@Override
			public void execute() {
				if(BroodBee.this.getEnergy() < 0.9)
				{
					BroodBee.this.stimuliLoad.emit(new HungryLarvaeStimulus(1-BroodBee.this.getEnergy()));
					//System.out.println(this.ID + " " + this.stimuliLoad.getPheromoneAmount(Stimuli.HungryLarvae));
				}
			}

			@Override
			public double compute(StimuliMap load) {
				return 1;
			}

			@Override
			public void interrupt() {
				BroodBee.this.currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				return false;
			}
		};	

		askFood.energyCost = 0.001;
		askFood.taskName = "LarvaeAskFood";

		taskList.add(askFood);
		
		currentTask = askFood;
	}
}
