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
	
	private void setAmount(Stimulus key, double amount)
	{
		if(pheromonesLoadMap.containsKey(key))
		{
			pheromonesLoadMap.get(key).amount = amount;
		}
		else
		{
			emit(StimulusFactory.get(key, amount));
		}
	}
/*
	public void evaporate()
	{
		pheromonesLoadMap.forEach((ph,stimulus) -> {
			stimulus.evaporate();
		});
		pheromonesLoadMap.entrySet().removeIf(entry -> entry.getValue().getAmount() == 0);
	}
*/
	public void contact(StimuliLoad externalStimuli)
	{
		for(Stimulus key : Stimulus.values())
		{
			double mean = this.getPheromoneAmount(key) + externalStimuli.getPheromoneAmount(key) / 2;
			if(mean != 0)
			{
				this.setAmount(key, mean);
				externalStimuli.setAmount(key, mean);
				
				//System.out.println(mean + " = " + externalStimuli.getPheromoneAmount(key));
			}
		}
	}
}
