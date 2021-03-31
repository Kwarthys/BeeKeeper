package com.beekeeper.controller.threadedexecution;

import java.util.ArrayList;
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
		agentPoll = new ArrayBlockingQueue<>((int)(ModelParameters.NUMBER_BEES * 1.1));
	}
	
	public boolean isWorking()
	{
		return agentPoll.size() != 0 || working || pilingUp;
	}
	
	public void addWork(ArrayList<Agent> agents)
	{		
		pilingUp = true;
		agentPoll.addAll(new ArrayList<Agent>(agents)); //TODO Most weird nullPointerException right here
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("PersistentExecutorThread" + index + " shutting off");
	}
}
