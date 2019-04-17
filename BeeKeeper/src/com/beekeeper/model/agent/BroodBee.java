package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.external.HungryLarvaeStimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public class BroodBee extends EmptyBee {

	public BroodBee(StimuliManagerServices stimuliManagerServices) {
		super(stimuliManagerServices);
		this.type = BeeType.BROOD_BEE;
		
		this.position = new Point2D.Double(Math.random()*600, Math.random()*600);
	}

	@Override
	public void live()
	{
		this.addToEnergy(-0.01);
		this.stimuliManagerServices.emitStimulus(new HungryLarvaeStimulus(1-this.getEnergy()));
	}

	@Override
	public void move(double dx, double dy) {}

	@Override
	protected void fillTaskList() {}
}
