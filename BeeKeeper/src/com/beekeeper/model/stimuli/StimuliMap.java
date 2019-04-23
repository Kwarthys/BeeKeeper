package com.beekeeper.model.stimuli;

import java.util.HashMap;

public class StimuliMap
{
	private HashMap<Stimulus, Double> amounts = new HashMap<>();
	
	public void addAmount(Stimulus type, double amount)
	{
		if(amounts.containsKey(type))
		{
			amounts.put(type, amount + amounts.get(type));
		}
		else
		{
			amounts.put(type, amount);
		}
	}
	
	public double getAmount(Stimulus type)
	{
		if(amounts.containsKey(type))
		{
			return amounts.get(type);
		}
		else
		{
			return 0;
		}
	}
}
