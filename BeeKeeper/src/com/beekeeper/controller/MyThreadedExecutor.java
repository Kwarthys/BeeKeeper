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
		//long start = System.nanoTime();
		for(int i = 0; i < agentList.size(); ++i)
		{
			//System.out.println("Living " + agentList.get(i).getStringName());
			agentList.get(i).live();
		}
		//long progTime = (System.nanoTime() - start)/1000;
		//if(progTime > 2000)System.out.println("Thread took " + progTime + " us.");
		
		//System.out.println("ThreadFinished in " + progTime + " µs");
	}

}
