package com.beekeeper.model.stimuli;

import java.util.HashMap;

import com.beekeeper.parameters.ModelParameters;

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
	
	public String getDisplayString()
	{
		StringBuffer sb = new StringBuffer();
		
		amounts.forEach((k,v) -> {
			sb.append(k);
			sb.append(" :");
			sb.append(v);
			sb.append("\n");
		});
		
		return sb.toString();
	}
	
	public void evaporate()
	{
		amounts.forEach((smell, amount) -> {
			if(amount != 0)
			{
				//System.out.println(smell + " " + amount);
				amount *= StimulusFactory.get(smell, 0).timeDecay;
				if(amount < ModelParameters.SMELL_THRESHOLD)
				{
					amount = 0.0;
				}				
			}
		});
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

	public void divideAmount(Stimulus st, double d)
	{
		amounts.put(st, amounts.get(st) / d);
	}

	public void setAmount(Stimulus smell, double tmpAmount)
	{
		amounts.put(smell, tmpAmount);
	}
}
