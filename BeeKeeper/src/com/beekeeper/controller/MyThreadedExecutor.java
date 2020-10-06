package com.beekeeper.controller;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;

public class MyThreadedExecutor implements Runnable {
	
	private ArrayList<Agent> agentList;
	
	public MyThreadedExecutor(ArrayList<Agent> agentList)
	{
		this.agentList = agentList;
	}

	@Override
	public void run()
	{
		for(int i = 0; i < agentList.size(); ++i)
		{
			//System.out.println("Living " + agentList.get(i).getStringName());
			agentList.get(i).live();
		}
	}

}
