package com.beekeeper.model.stimuli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
	
	public Set<Stimulus> keySet()
	{
		return amounts.keySet();
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

	public static void contactBetween(StimuliMap map1, StimuliMap map2)
	{
		ArrayList<Stimulus> smells = new ArrayList<>();
		smells.addAll(map1.keySet());
		for(Stimulus s : map2.keySet())
		{
			if(!smells.contains(s))
			{
				smells.add(s);
			}
		}
		
		for(Stimulus s : smells)
		{
			double otherCoef = StimulusFactory.getPropag(s);
			double ownCoef = 1-otherCoef;

			double newAmount1 = ownCoef * map1.getAmount(s) + otherCoef * map2.getAmount(s);
			double newAmount2 = ownCoef * map2.getAmount(s) + otherCoef * map1.getAmount(s);
			
			/*
			//DEBUG
			System.out.println("\nContact");
			System.out.println("From " + map1.getAmount(s) + " to " + newAmount1);
			System.out.println("From " + map2.getAmount(s) + " to " + newAmount2);
			*/
			
			map1.setAmount(s, newAmount1);
			map2.setAmount(s, newAmount2);
		}		
	}
}
