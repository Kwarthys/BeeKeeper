package com.beekeeper.model.stimuli.external;

import java.util.HashMap;

public class ExternalStimuliLoad
{	
	private HashMap<ExternalStimuli, Double> pheromonesLoadMap = new HashMap<>();

	
	public ExternalStimuliLoad hungerLarvae(double amount)
	{
		pheromonesLoadMap.put(ExternalStimuli.HungryLarvae, amount);
		return this;
	}
	
	public ExternalStimuliLoad hungerBee(double amount)
	{
		pheromonesLoadMap.put(ExternalStimuli.HungerBee, amount);
		return this;
	}
	
	public ExternalStimuliLoad dance(double amount)
	{
		pheromonesLoadMap.put(ExternalStimuli.Dance, amount);
		return this;
	}
	
	public double getPheromoneAmount(ExternalStimuli p)
	{
		if(pheromonesLoadMap.containsKey(p))
		{
			return pheromonesLoadMap.get(p);
		}
		else
		{
			return 0;
		}
	}
}
