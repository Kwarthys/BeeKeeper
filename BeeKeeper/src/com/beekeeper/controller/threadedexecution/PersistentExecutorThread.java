package com.beekeeper.controller.threadedexecution;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.parameters.ModelParameters;

public class PersistentExecutorThread implements Runnable {
	
	public int index = 0;
	
	private volatile boolean working = false;
	private volatile boolean pilingUp = false;
	
	private volatile boolean interuptionAsked = false;
	
	private volatile ArrayBlockingQueue<Agent> agentPoll;
	
	public PersistentExecutorThread(int index)
	{
		this.index = index;
		agentPoll = new ArrayBlockingQueue<>(ModelParameters.colonyMajoredEstimatedMaxSize);
		//System.out.println("PresistentThread"+index+"Created");
	}
	
	public boolean isWorking()
	{
		return agentPoll.size() != 0 || working || pilingUp;
	}
	
	public void addWork(List<Agent> agents)
	{		
		if(agentPoll==null)System.out.println("agentPoll null");
		pilingUp = true;
		for(Agent a : agents)
		{
			if(a==null)
			{
				//System.out.println("null Agent added to PersistentThread - AddWork");
			}
			else
			{
				agentPoll.add(a);
			}
		}
		pilingUp = false;
		
		//System.out.println(index + " Just added " + agents.size() + " now at " + agentPoll.size());
	}
	
	public void addWork(Agent agent)
	{
		agentPoll.add(agent);
	}
	
	public void askInterruption()
	{
		interuptionAsked = true;
	}

	@Override
	public void run()
	{
		while(!interuptionAsked)
		{
			try {
				
				Agent a = agentPoll.take();
				working = true;
				if(!interuptionAsked)
				{
					a.live();
				}
				
				/*
				if(!(agentPoll.size() != 0 || pilingUp))
				{
					System.out.println(index + " job's done " + System.nanoTime());
				}
				*/	
				
				working = false;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("PersistentExecutorThread" + index + " shutting off");
	}
}
