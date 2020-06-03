package com.beekeeper.model.comb;

import java.util.HashMap;

public class TrafficJamManager {
	
	private HashMap<Integer, Integer> jamRecord = new HashMap<>();
	
	public CombServices combServices;
	
	public void resetAll()
	{
		//System.out.println("reset all");
		jamRecord.clear();
	}
	
	public TrafficJamManager(CombServices combServices) {
		this.combServices = combServices;
	}
	
	
	public void registerSwapDemand(int cellIndexAsking, int cellIndexJammer)
	{
		if(jamRecord.containsKey(cellIndexAsking))
		{
			if(jamRecord.get(cellIndexAsking) == cellIndexJammer)
			{
				//System.out.println(cellIndexAsking + " swapping with " + cellIndexJammer + " on comb " + combServices.getID());
				combServices.swap(jamRecord.get(cellIndexAsking), cellIndexAsking);
				jamRecord.remove(cellIndexAsking);
				//Conflict has been resolved
				return;
			}
		}

		//System.out.println(cellIndexAsking + " blocked by " + cellIndexJammer + " on comb " + combServices.getID());
		jamRecord.put(cellIndexJammer, cellIndexAsking);			
		
	}	
}
