package com.beekeeper.model.stimuli;

import java.util.HashMap;

import com.beekeeper.parameters.ModelParameters;

public class StimuliMap
{
	//private int counter = (int)(Math.random()*1000);
	
	private HashMap<Stimulus, Double> amounts = new HashMap<>();
	
	public StimuliMap(StimuliMap toCopy)
	{
		addAllAmounts(toCopy);
	}
	
	public StimuliMap(){} //Just to let java know it's allowed
	
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
				//System.out.print(smell + " " + amount);
				amount *= StimulusFactory.getEvapRate(smell);
				if(amount < ModelParameters.SMELL_THRESHOLD)
				{
					amount = 0.0;
				}
				
				amounts.put(smell, amount);
				//System.out.println(" -> " + amount);
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

	public void setAmount(Stimulus smell, double amount)
	{
		if(amount < ModelParameters.SMELL_THRESHOLD)
		{
			amount = 0;
		}
		amounts.put(smell, amount);
	}

	public boolean isEmpty()
	{
		for(double v : amounts.values())
		{
			if(v != 0)
			{
				return false;
			}
		}
		return true;
	}

	public void addAllAmounts(StimuliMap sm)
	{
		sm.amounts.forEach((smell, amount)->{
			StimuliMap.this.addAmount(smell, amount);
		});
	}
}
