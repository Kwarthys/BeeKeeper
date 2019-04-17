package com.beekeeper.model.stimuli.external;

import java.awt.geom.Point2D;
import java.util.HashMap;

import com.beekeeper.model.stimuli.AStimulus;

public class ExternalStimuliLoad
{	
	private HashMap<ExternalStimuli, AStimulus> pheromonesLoadMap = new HashMap<>();
	public Point2D.Double emiterPos;
	
	public ExternalStimuliLoad(Point2D.Double pos)
	{
		this.emiterPos = pos;
	}
	
	public ExternalStimuliLoad emit(AStimulus s)
	{
		ExternalStimuli key = s.getStimulusType();
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
	
	public double getPheromoneAmount(ExternalStimuli p)
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
