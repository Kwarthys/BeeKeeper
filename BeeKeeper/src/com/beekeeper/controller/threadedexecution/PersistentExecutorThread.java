package com.beekeeper.controller.threadedexecution;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.beekeeper.model.agent.Agent;

public class PersistentExecutorThread implements Runnable {
	
	public int index = 0;
	
	private volatile boolean interuptionAsked = false;
	
	private volatile ArrayBlockingQueue<Agent> agentPoll = new ArrayBlockingQueue<>(16000);
	
	public PersistentExecutorThread(int index)
	{
		this.index = index;
	}
	
	public boolean isWorking()
	{
		return agentPoll.size() != 0;
	}
	
	public void addWork(ArrayList<Agent> agents)
	{		
		agentPoll.addAll(new ArrayList<Agent>(agents));
		//System.out.println("Just added " + agents.size() + " now at " + agentPoll.size());
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
				if(!interuptionAsked)
				{
					a.live();
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("PersistentExecutorThread" + index + " shutting off");
	}
}
