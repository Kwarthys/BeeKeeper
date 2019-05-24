package com.beekeeper.model.stimuli;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class StimuliLoad
{	
	private HashMap<Stimulus, AStimulus> pheromonesLoadMap = new HashMap<>();
	public Point2D.Double emiterPos;
	
	public StimuliLoad(Point2D.Double pos)
	{
		this.emiterPos = pos;
	}
	
	public HashMap<Stimulus, AStimulus> getMapCopy()
	{
		return new HashMap<Stimulus, AStimulus>(pheromonesLoadMap);
	}
	
	public StimuliLoad emit(AStimulus s)
	{
		Stimulus key = s.getStimulusType();
		double amount = s.getAmount();
		if(pheromonesLoadMap.containsKey(key))
		{
			pheromonesLoadMap.get(key).add(amount);
		}
		else
		{
			pheromonesLoadMap.put(key, s);			
		}
		return this;
	}
	
	public double getPheromoneAmount(Stimulus p)
	{
		if(pheromonesLoadMap.containsKey(p))
		{
			return pheromonesLoadMap.get(p).getAmount();
		}
		else
		{
			return 0;
		}
	}
	
	public double getSensedStimulusAmount(Stimulus p, double distance)
	{
		if(pheromonesLoadMap.containsKey(p))
		{
			return pheromonesLoadMap.get(p).getAmountOverDistance(distance);
		}
		else
		{
			return 0;
		}
	}
	
	private boolean setAmount(Stimulus key, double amount)
	{
		if(pheromonesLoadMap.containsKey(key))
		{
			pheromonesLoadMap.get(key).amount = amount;
		}
		else
		{
			return false;
		}
		
		return true;
	}

	public void evaporate()
	{
		pheromonesLoadMap.forEach((ph,stimulus) -> {
			stimulus.evaporate();
		});
		pheromonesLoadMap.entrySet().removeIf(entry -> entry.getValue().getAmount() == 0);
	}

	public void contact(StimuliLoad externalStimuli)
	{
		this.pheromonesLoadMap.forEach((key, stimulus) -> {
			double mean = stimulus.amount + externalStimuli.getPheromoneAmount(key) / 2;
			stimulus.amount = mean;
			if(!externalStimuli.setAmount(key, mean))
			{
				externalStimuli.emit(StimulusFactory.get(key, mean));
			}
		});
	}
}
