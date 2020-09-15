package com.beekeeper.controller;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;

public class MyThreadedExecutor implements Runnable {
	
	private int startIndex;
	private int stopIndex;
	private ArrayList<Agent> agentList;
	
	public MyThreadedExecutor(ArrayList<Agent> agentList, int startIndex, int stopIndex)
	{
		this.startIndex = startIndex;
		this.stopIndex = stopIndex;
		this.agentList = agentList;
	}

	@Override
	public void run()
	{
		for(int i = startIndex; i < stopIndex; ++i)
		{
			agentList.get(i).live();
		}
	}

}
