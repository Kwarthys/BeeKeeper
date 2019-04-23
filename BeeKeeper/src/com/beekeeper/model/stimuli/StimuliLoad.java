package com.beekeeper.model.stimuli;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class StimuliLoad
{	
	private HashMap<Stimuli, AStimulus> pheromonesLoadMap = new HashMap<>();
	public Point2D.Double emiterPos;
	
	public StimuliLoad(Point2D.Double pos)
	{
		this.emiterPos = pos;
	}
	
	public HashMap<Stimuli, AStimulus> getMapCopy()
	{
		return new HashMap<Stimuli, AStimulus>(pheromonesLoadMap);
	}
	
	public StimuliLoad emit(AStimulus s)
	{
		Stimuli key = s.getStimulusType();
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
	
	public double getPheromoneAmount(Stimuli p)
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

	public void evaporate()
	{
		pheromonesLoadMap.forEach((ph,stimulus) -> {
			stimulus.evaporate();
		});
		pheromonesLoadMap.entrySet().removeIf(entry -> entry.getValue().getAmount() == 0);
	}
}
