package com.beekeeper.model.stimuli;

import com.beekeeper.model.stimuli.pheromones.PheromoneLoad;

public class StimuliLoad
{
	public PheromoneLoad phs;
	public double beeEnergy;
	
	public StimuliLoad(PheromoneLoad phs, double beeEnergy)
	{
		this.phs = phs;
		this.beeEnergy = beeEnergy;
	}
}
