package com.beekeeper.ihm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;

public class EmployementData
{
	public HashMap<String, Integer> data = new HashMap<String, Integer>();
	
	private static EmployementData getDataFromList(ArrayList<Agent> bees, boolean filterPrints)
	{
		EmployementData d = new EmployementData();
		
		for(Agent a : bees)
		{
			AgentType at = a.getBeeType();
			if(at == AgentType.ADULT_BEE || at == AgentType.BROOD_BEE || at == AgentType.TEST_AGENT)
			{
				WorkingAgent b = (WorkingAgent) a;
				
				if(b.getCurrentTask() == null)
				{
					d.incrementKey("SwitchingTask");
				}
				else
				{
					if(!(filterPrints && !b.getCurrentTask().printLearning))
					{
						String taskName = b.getCurrentTask().taskName;
						d.incrementKey(taskName);										
					}
				}
			}			
		}		
		
		return d;
	}
	
	private void put(String task, Integer workers)
	{
		this.data.put(task, workers);
	}
	
	private Set<String> getKeys()
	{
		return this.data.keySet();
	}
	
	public static EmployementData mean(EmployementData a, EmployementData b, double coefA, double coefB)
	{
		ArrayList<String> keys = new ArrayList<String>(a.getKeys());
		for(String s : b.getKeys())
		{
			if(!keys.contains(s))
			{
				keys.add(s);
			}
		}
		
		EmployementData ed = new EmployementData();
		
		for(String key : keys)
		{
			ed.put(key, (int) ((a.get(key) * coefA + b.get(key) * coefB) /(coefA + coefB)));
		}
				
		return ed;
	}
	
	public static EmployementData mean(EmployementData a, EmployementData b)
	{				
		return mean(a,b,1,1);
	}

	public static EmployementData getDataToPrintFromList(ArrayList<Agent> bees)
	{
		return getDataFromList(bees, true);
	}
	
	public static EmployementData getDataFromList(ArrayList<Agent> bees)
	{
		return getDataFromList(bees, false);
	}
	
	public int get(String key)
	{
		return data.containsKey(key) ? data.get(key) : 0;
	}
	
	private void incrementKey(String key)
	{
		if(data.containsKey(key))
		{
			data.put(key, data.get(key) + 1);
		}
		else
		{
			data.put(key, 1);
		}
	}
}
