package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

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
		if(this.getEnergy() < 0.7)
		{
			this.pheromoneLoad.addHngerLarvae(0.7-getEnergy());
		}
	}

	@Override
	public void move(double dx, double dy) {}

	@Override
	protected void fillTaskList() {}
}
