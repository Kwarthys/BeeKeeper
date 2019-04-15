package com.beekeeper.model.stimuli.pheromones;

import java.util.HashMap;

public class PheromoneLoad
{
	private HashMap<Pheromones, Double> pheromonesLoadMap = new HashMap<>();
	
	public PheromoneLoad hungerLarvae(double amount)
	{
		pheromonesLoadMap.put(Pheromones.HungerLarvae, amount);
		return this;
	}
	
	public PheromoneLoad hungerBee(double amount)
	{
		pheromonesLoadMap.put(Pheromones.HungerBee, amount);
		return this;
	}
	
	public double getPheromoneAmount(Pheromones p)
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
