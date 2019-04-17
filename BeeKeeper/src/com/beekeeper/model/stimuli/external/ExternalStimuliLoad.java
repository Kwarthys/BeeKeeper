package com.beekeeper.model.stimuli.external;

import java.util.HashMap;

import com.beekeeper.parameters.ModelParameters;

public class ExternalStimuliLoad
{	
	private HashMap<ExternalStimuli, Double> pheromonesLoadMap = new HashMap<>();

	
	public ExternalStimuliLoad hungerLarvae(double amount)
	{
		pheromonesLoadMap.put(ExternalStimuli.HungryLarvae, amount);
		return this;
	}
	
	private ExternalStimuliLoad addAny(ExternalStimuli key, double amount)
	{
		if(pheromonesLoadMap.containsKey(key))
		{
			pheromonesLoadMap.put(key, amount + pheromonesLoadMap.get(key));
		}
		else
		{
			pheromonesLoadMap.put(key, amount);			
		}
		return this;
	}

	
	public ExternalStimuliLoad addHngerLarvae(double amount)
	{
		return addAny(ExternalStimuli.HungryLarvae, amount);
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

	public void evaporate()
	{
		pheromonesLoadMap.forEach((ph,amount) -> {
			switch(ph) {
			case Dance:
				break;
			case HungerBee:
				break;
			case HungryLarvae:
				amount *= ModelParameters.TIME_DECAY_HungryLarvae; //NOT the right thing to do
				break;
			default:
				break;
			
			}
		});
	}
}
