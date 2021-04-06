package com.beekeeper.controller.threadedexecution;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;

public class WorkDispatcher {
	
	private int millisToWait = 0;
	
	private ArrayList<PersistentExecutorThread> threads = new ArrayList<>();
	
	public WorkDispatcher(int startingPopulationSize)
	{		
		for(int i = 0; i < startingPopulationSize; ++i)
		{
			createAndStartANewThread();
		}
	}
	
	public void stopAllThreads(Agent dummy)
	{
		for(PersistentExecutorThread t : threads)
		{
			t.askInterruption();
			t.addWork(dummy);
		}
	}
	
	private PersistentExecutorThread createAndStartANewThread()
	{
		PersistentExecutorThread t = new PersistentExecutorThread(threads.size());
		
		Thread nt = new Thread(t);		
		nt.setName("PersistentThread" + threads.size());		
		nt.start();
		
		threads.add(t);
		
		return t;
	}
	
	public void getThatWorkDone(ArrayList<Agent> agents)
	{
		boolean jobStarted = false;
		
		for(int i = 0; i < threads.size() && !jobStarted; ++i)
		{
			if(!threads.get(i).isWorking())
			{
				threads.get(i).addWork(agents);
				jobStarted = true;
			}
		}
		
		if(!jobStarted)
		{			
			createAndStartANewThread().addWork(agents);
			//System.out.println("All thread busy, created a new one: " + threads.size() + " total");
		}
	}
	
	public void waitForWorkToEnd()
	{
		boolean debugThreads = false;
		boolean allDone = false;		
		
		while(!allDone)
		{
			allDone = true;
			
			for(PersistentExecutorThread t : threads)
			{
				allDone = allDone && !t.isWorking();
				
				if(debugThreads)
				{
					System.out.print(!t.isWorking() ? "0" : "1");
				}
			}
			
			if(debugThreads)
			{
				System.out.println();
			}
			
			if(millisToWait > 0)
			{
				try {
					Thread.sleep(millisToWait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
			
		}
		
		if(debugThreads)
		{
			System.out.println("Wait is over");			
		}
	}
}
