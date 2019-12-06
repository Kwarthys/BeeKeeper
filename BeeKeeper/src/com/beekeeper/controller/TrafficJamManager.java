package com.beekeeper.controller;

import java.util.HashMap;

import com.beekeeper.model.comb.CombServices;

public class TrafficJamManager {
	
	private HashMap<Integer, Integer> jamRecord = new HashMap<>();
	
	public CombServices combServices;
	
	public void resetAll()
	{
		jamRecord.clear();
	}
	
	public TrafficJamManager(CombServices combServices) {
		this.combServices = combServices;
	}
	
	
	public void registerSwapDemand(int cellIndexAsking, int cellIndexJammer)
	{
		if(jamRecord.containsKey(cellIndexAsking))
		{
			combServices.swap(jamRecord.get(cellIndexAsking), cellIndexAsking);
		}
		else
		{
			jamRecord.put(cellIndexJammer, cellIndexAsking);			
		}
	}
	
	
}
