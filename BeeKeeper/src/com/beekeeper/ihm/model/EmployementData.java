package com.beekeeper.ihm.model;

import java.util.ArrayList;
import java.util.HashMap;

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
