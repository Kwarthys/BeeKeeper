package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.declarations.HungryLarvaeStimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public class BroodBee extends EmptyBee {

	public BroodBee(StimuliManagerServices stimuliManagerServices) {
		super(stimuliManagerServices);
		this.type = BeeType.BROOD_BEE;
		
		this.stimuliLoad = new StimuliLoad(this.position);
	}

	@Override
	public void live()
	{
		this.addToEnergy(-0.005);
		if(this.getEnergy() < 0.7)
		{
			this.stimuliLoad.emit(new HungryLarvaeStimulus(0.7-this.getEnergy()));
			//System.out.println(this.ID + " " + this.stimuliLoad.getPheromoneAmount(Stimuli.HungryLarvae));
		}
	}

	@Override
	public void move(double dx, double dy) {}

	@Override
	protected void fillTaskList() {}
}
