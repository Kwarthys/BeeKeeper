package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.declarations.HungryLarvaeStimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

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
			System.out.println(ID + " died of starvation, how sad.");
			return;
		}
		
		this.addToEnergy(-0.0005);
		if(this.getEnergy() < 0.9)
		{
			this.stimuliLoad.emit(new HungryLarvaeStimulus(1-this.getEnergy()));
			//System.out.println(this.ID + " " + this.stimuliLoad.getPheromoneAmount(Stimuli.HungryLarvae));
		}
		
	}

	@Override
	public void move(double dx, double dy) {}

	@Override
	protected void fillTaskList() {}
}
