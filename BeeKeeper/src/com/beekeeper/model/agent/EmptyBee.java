package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.external.ExternalStimuliLoad;

public abstract class EmptyBee extends Agent
{
	protected abstract void fillTaskList();
	protected double energy;
	protected ExternalStimuliLoad pheromoneLoad;
}
