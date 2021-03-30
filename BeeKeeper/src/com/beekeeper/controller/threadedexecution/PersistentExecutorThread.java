package com.beekeeper.controller.threadedexecution;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.beekeeper.model.agent.Agent;

public class PersistentExecutorThread implements Runnable {
	
	public int index = 0;
	
	private volatile boolean working = false;
	private volatile boolean pilingUp = false;
	
	private volatile boolean interuptionAsked = false;
	
	private volatile ArrayBlockingQueue<Agent> agentPoll = new ArrayBlockingQueue<>(16000);
	
	public PersistentExecutorThread(int index)
	{
		this.index = index;
	}
	
	public boolean isWorking()
	{
		return agentPoll.size() != 0 || working || pilingUp;
	}
	
	public void addWork(ArrayList<Agent> agents)
	{		
		pilingUp = true;
		agentPoll.addAll(new ArrayList<Agent>(agents));
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
				working = false;
				
				/*
				if(!isWorking())
				{
					System.out.println(index + " job's done");
				}
				*/
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("PersistentExecutorThread" + index + " shutting off");
	}
}
