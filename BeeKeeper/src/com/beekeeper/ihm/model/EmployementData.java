package com.beekeeper.ihm.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;

public class EmployementData
{
	public HashMap<String, Integer> data = new HashMap<String, Integer>();
	
	public static EmployementData getDataFromList(ArrayList<Agent> bees)
	{
		EmployementData d = new EmployementData();
		
		for(Agent a : bees)
		{
			AgentType at = a.getBeeType();
			if(at == AgentType.ADULT_BEE || at == AgentType.BROOD_BEE)
			{
				WorkingAgent b = (WorkingAgent) a;
				
				if(b.getCurrentTask() == null)
				{
					d.incrementKey("SwitchingTask");
				}
				else
				{
					String taskName = b.getCurrentTask().taskName;
					d.incrementKey(taskName);				
				}
			}			
		}		
		
		return d;
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
