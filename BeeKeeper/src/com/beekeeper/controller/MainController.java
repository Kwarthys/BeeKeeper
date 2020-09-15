package com.beekeeper.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.ihm.CombDrawer;
import com.beekeeper.ihm.TaskGrapher;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentStateSnapshot;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.CombManager;
import com.beekeeper.model.comb.CombServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.network.NetworkManager;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

public class MainController
{	
	ArrayList<CombDrawer> drawers = new ArrayList<CombDrawer>();

	private ArrayList<Comb> combs = new ArrayList<Comb>();

	private MyLogger logger = new MyLogger();

	private CombManager combManager;

	private BeeWindow window;

	private AgentFactory agentFactory;
	
	private ArrayList<Integer> foragers = new ArrayList<>(); //for perf issues
	private ArrayList<Integer> deadAdults = new ArrayList<>(); //same

	//private int simuStep = 0;

	private boolean closed;
	
	private int timeStepPauseToIgnore = 0;
	
	private volatile int turnIndex = -1;
	
	private HashMap<Integer, Integer> contactsQuantitiesByIndex = new HashMap<>();
	private boolean contactsLocked = false;

	private MainControllerServices controlServices = new MainControllerServices() {

		@Override
		public void logMyTaskSwitch(Task newTask, int beeID) {
			MainController.this.logger.logTask(beeID, newTask.taskName);				
		}

		@Override
		public CombCell askLandingZone() {
			ArrayList<Comb> theCombs = new ArrayList<>(combs);
			
			Iterator<Comb> it = theCombs.iterator();
			
			while(it.hasNext())
			{
				Comb c = it.next();
				if(c.isUp())
				{
					it.remove();
					//System.out.println(c.ID + " is Up, can't land");
				}
			}			
			
			Collections.shuffle(theCombs);
			for(Comb cb : theCombs)
			{				
				ArrayList<CombCell> landingCells = new ArrayList<>(cb.getLandingZone());
				Collections.shuffle(landingCells);
				for(CombCell c : landingCells)
				{
					if(c.visiting == null)
					{
						return c;
					}
				}
			}

			return null;
		}

		@Override
		public void notifyWindowClosed() {
			MainController.this.closed = true;
		}

		@Override
		public void switchFrames(int index1, int index2) {
			MainController.this.switchFrames(index1, index2);
		}

		@Override
		public void reverseFrame(int index) {
			MainController.this.reverseFrame(index);
		}

		@Override
		public void layEgg(CombCell cell) {
			MainController.this.layEgg(cell);	
		}

		@Override
		public ArrayList<Comb> getCombs() {
			return MainController.this.combs;
		}

		@Override
		public ArrayList<Integer> getForagers() {
			return MainController.this.foragers;
		}

		@Override
		public ArrayList<Integer> getTheDead() {
			ArrayList<Integer> theDeads = new ArrayList<>();
			deadAdults.forEach((Integer id) -> theDeads.add(id));
			deadAdults.clear();
			return theDeads;
		}

		@Override
		public void liftFrame(int frameIndex) {
			combManager.liftFrame(frameIndex);
		}

		@Override
		public void putFrame(int frameIndex, int pos, boolean reverse) {
			combManager.putFrame(frameIndex, pos, reverse);
		}

		@Override
		public ArrayList<AgentStateSnapshot> getAllAdults() {
			ArrayList<AgentStateSnapshot> toReturn = new ArrayList<>();
			for(Agent a : agentFactory.allAgents)
			{
				if(a.getBeeType() == AgentType.ADULT_BEE || a.getBeeType() == AgentType.QUEEN)
				{
					toReturn.add(new AgentStateSnapshot((WorkingAgent)a));
				}
			}
			
			return toReturn;
		}

		@Override
		public int getCurrentTimeStep() {
			return turnIndex;
		}

		@Override
		public void setNumberOfSecondsToGoFast(int seconds) {
			MainController.this.setNbOfTimeStepToGoFast((int) (seconds * ModelParameters.secondToTimeStepCoef));			
		}

		@Override
		public void notifyAgentContact(int id1, int id2, double amount) {
			registerContactFor(id1, (int) amount);
			registerContactFor(id2, (int) amount);
			//String s = id1 + " " + id2 + " " + (int)amount;
			//boolean add = contactBlockingQueue.add(s);
			//System.out.println(id1 + "-" + id2 + " exchanged " + amount + " : " + add);
		}

		@Override
		public HashMap<Integer, Integer> getAgentContacts() {
			contactsLocked = true;
			return contactsQuantitiesByIndex;
		}

		@Override
		public void freeLockAgentContacts() {
			contactsLocked = false;
		}
	};

	public MainController()
	{
		this.agentFactory = new AgentFactory();

		this.combManager = new CombManager();
		combs = this.combManager.initiateFrames(ModelParameters.NUMBER_FRAMES, agentFactory, this.controlServices);

		for(CombServices c : combManager.getCombsServices())
		{
			this.drawers.add(new CombDrawer(c));
		}

		TaskGrapher g = new TaskGrapher(agentFactory.allAgents);

		if(ModelParameters.UI_ENABLED == true)
		{
			this.window = new BeeWindow(g,drawers, this.controlServices);
			closed = false;			
		}
		
		NetworkManager nm = new NetworkManager(controlServices);

		programLoop();

		if(this.window != null)
		{
			this.window.dispose();
		}

		this.logger.closing();
		nm.closing();

		try {
			System.out.println("Waiting");
			this.logger.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/*
		while(!logger.threadFinished())
		{
			System.out.println("Waiting");
			try {
				Thread.sleep(500);//30
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		 */
		System.out.println("expe done");
	}
	
	private void registerContactFor(int agentIndex, int quantity)
	{
		if(contactsLocked)
		{
			System.out.println("registerContactFor : Locked");
			return; //If map is being read by another thread, we're throwing data away : shouldn't be much of an issue given the shear amount of data
		}
		
		if(agentFactory.typesOfIndex.get(agentIndex) == AgentType.ADULT_BEE)
		{
			if(!contactsQuantitiesByIndex.containsKey(agentIndex))
			{
				contactsQuantitiesByIndex.put(agentIndex, quantity);
			}
			else
			{
				contactsQuantitiesByIndex.put(agentIndex, contactsQuantitiesByIndex.get(agentIndex) + quantity);
			}
		}
	}
	
	private void setNbOfTimeStepToGoFast(int number)
	{
		timeStepPauseToIgnore = number;
	}

	/*
	private void logTurn(int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		logger.log(turnIndex, beeID, beeTaskName, beePhysio);
	}
	 */
	private void reverseFrame(int index)
	{
		if(index >= combManager.getCombsServices().size()/2 || index < 0)
		{
			System.out.println("refused reverse");
			return;
		}

		combManager.reverseFrame(index);
		MyUtils.switchElementsInList(drawers, index*2, index*2+1);

		this.window.updateDrawersPos();
	}

	private void switchFrames(int index1, int index2)
	{
		if(index1 >= combManager.getCombsServices().size()/2 || index2 >= combManager.getCombsServices().size()/2 || index1 < 0 || index2 < 0 || index1 == index2)
		{
			System.out.println("refused switch");
			return;
		}

		combManager.switchFrames(index1, index2);

		MyUtils.switchElementsInList(drawers, index1*2, index2*2);
		MyUtils.switchElementsInList(drawers, index1*2+1, index2*2+1);

		this.window.updateDrawersPos();
	}
	
	private void layEgg(CombCell cell)
	{
		Comb host = combManager.getCombOfID(cell.getCombID());
		this.agentFactory.spawnALarvae(cell, host, host.getServices().getCurrentSManagerServices(), controlServices);
	}

	private void logTurn(String... ss)
	{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < ss.length; ++i)
		{
			if(i!=0)
			{
				sb.append(",");
			}
			sb.append(ss[i]);
		}

		logger.log(sb.toString());
	}

	private void programLoop()
	{
		boolean DEBUGTIME = false;
		boolean MONITORTIME = false;
		int minLoopMs = -1;
		int maxLoopMs = 0;
		long totalLoopMs = 0;
		long totalAgents = 0;
		long totalDeaths = 0;
		int startingAverageIndex = 0;
		
		turnIndex = 0;
		int displayBar = 20;

		System.out.print("|");
		for(int i = 1; i < displayBar-1; ++i)
		{			
			System.out.print("-");
		}
		System.out.println("|");

		logTurn("turnIndex", "beeID", "TaskName", "Physio");
		while(turnIndex < ModelParameters.SIMU_LENGTH && !closed)
		{
			long startLoopTime = System.nanoTime();
			
			if(turnIndex%(int)(ModelParameters.SIMU_LENGTH/displayBar) == 0)
			{
				System.out.print("|");
			}
			
			/**** FRAME MOVEMENT DEBUG ****/
			//will keep it for a lil time
			/*
			if(turnIndex == 2)
			{
				System.out.println("LIFTING FRAME 1");
				combManager.liftFrame(1);
			}
			else if(turnIndex == 50)
			{
				System.out.println("HITTING FRAME 1");
				combManager.hitFrame(1);
			}
			else if(turnIndex == 300)
			{
				System.out.println("DROPING FRAME 1");
				combManager.putFrame(1, 1, false);
			}
			
			else if(turnIndex == 40)
			{
				System.out.println("LIFTING FRAME 2");
				combManager.liftFrame(2);
			}
			else if(turnIndex == 50)
			{
				System.out.println("DROPING FRAME 2");
				combManager.putFrame(2, 1, false);
				System.out.println("DROPING FRAME 1");
				combManager.putFrame(1, 2, true);
			}
			*/
			turnIndex++;
			ArrayList<Agent> copy = new ArrayList<>(agentFactory.allAgents);
			Collections.shuffle(copy);
			
			// MULTITHREADING NOT CONVINCING ENOUGH
			/*
			int numberOfThread = 3;
			int threadsItemCount = copy.size() / numberOfThread;
			int leftOvers = copy.size() % numberOfThread;
			
			Thread threads[] = new Thread[numberOfThread];
			
			for(int i = 0; i < numberOfThread; ++i)
			{
				int startIndex = i * threadsItemCount;
				int stopIndex = startIndex + threadsItemCount;
				
				if(i == numberOfThread-1)//lastThread
				{
					//System.out.println("adding " + leftOvers);
					stopIndex += leftOvers;
				}
				
				//System.out.println("Thread[" + i + "] : " + startIndex + "->" + stopIndex + " / " + copy.size());
				
				threads[i] = new Thread(new MyThreadedExecutor(copy, startIndex, stopIndex));
				threads[i].start();
			}
			
			
			for(int i = 0; i < numberOfThread; ++i)
			{
				try {
					//System.out.println("waiting thread" + i);
					threads[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			 */
			ArrayList<Integer> newForagers = new ArrayList<>();
			for(Agent b : copy)
			{
				b.live();
				
				if(b.getBeeType() == AgentType.ADULT_BEE || b.getBeeType() == AgentType.BROOD_BEE)
				{
					//WorkingAgent w = (WorkingAgent) b;
					//logTurn(turnIndex, b.getID(), w.getTaskName(), w.getPhysio());
					if(!b.isInside())
					{
						newForagers.add(b.getID());
					}
				}				
			}
			
			if(DEBUGTIME)System.out.println("AllAgent lived at t+" + (System.nanoTime() - startLoopTime)/1000000 + "ms.");
			if(MONITORTIME)
				totalAgents += (System.nanoTime() - startLoopTime)/1000000;
			
			this.foragers = newForagers;

			for(Comb c : combs)
			{
				c.update();
			}

			ArrayList<Integer> newAdultDeaths = new ArrayList<>();
			
			for(Agent a : agentFactory.allAgents)
			{
				if(!a.alive)
				{
					if(a.getBeeType() == AgentType.ADULT_BEE || a.getBeeType() == AgentType.QUEEN)
					{
						newAdultDeaths.add(a.getID());
					}
				}
			}
			
			deadAdults.addAll(newAdultDeaths);

			agentFactory.allAgents.removeIf(new Predicate<Agent>() {
				@Override
				public boolean test(Agent t) {
					return !t.alive;
				}
			});
			
			if(MONITORTIME)
				totalDeaths += (System.nanoTime() - startLoopTime)/1000000;
			
			if(DEBUGTIME)System.out.println("Deaths cleanup at t+" + (System.nanoTime() - startLoopTime)/1000000 + "ms.");

			this.combManager.updateStimuli();
			
			if(DEBUGTIME)System.out.println("updateStimuli at t+" + (System.nanoTime() - startLoopTime)/1000000 + "ms.");
			
			/*** MONITOR TIME ***/
			if(MONITORTIME)
			{
				long loopMS = (System.nanoTime() - startLoopTime)/1000000;
				totalLoopMs += loopMS;
				minLoopMs = (int) Math.min(loopMS, minLoopMs);
				maxLoopMs = (int) Math.max(loopMS, maxLoopMs);
				
				if(minLoopMs == -1)
				{
					minLoopMs = (int) loopMS;
				}
				
				if(turnIndex%60*ModelParameters.secondToTimeStepCoef == 0)
				{
					StringBuffer theLog = new StringBuffer();
					theLog.append("60s Average: ");
					theLog.append(totalLoopMs/(turnIndex-startingAverageIndex));
					theLog.append("(");
					theLog.append(totalAgents/(turnIndex-startingAverageIndex));
					theLog.append("+");
					theLog.append((totalDeaths-totalAgents)/(turnIndex-startingAverageIndex));
					theLog.append("+");
					theLog.append((totalLoopMs - totalDeaths)/(turnIndex-startingAverageIndex));
					theLog.append(") Min: ");
					theLog.append(minLoopMs);
					theLog.append(" Max: ");
					theLog.append(maxLoopMs);
					
					System.out.println(theLog.toString());

					minLoopMs = -1;
					maxLoopMs = 0;
					totalLoopMs = 0;
					totalAgents = 0;
					totalDeaths = 0;
					startingAverageIndex = turnIndex;
				}
			}
			/********************/

			if(this.window != null)
			{
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						MainController.this.window.repaint();					
					}
				});
			}

			//System.out.println(turnIndex);
			
			long loopTime = (System.nanoTime() - startLoopTime)/1000000;
			
			if(DEBUGTIME)System.out.println("Loop took " + loopTime + "ms.");
			
			if(ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP > 0)
			{
				if(timeStepPauseToIgnore == 0)
				{
					try {
						long pause = Math.max(0, ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP - loopTime);
						//System.out.println("Loop took " + loopTime + " pausing for " + pause + " to match " + ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP);
						Thread.sleep(pause);
						//System.out.println("Paused");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}									
				}
				else
				{
					//System.out.println("Ignored. " + timeStepPauseToIgnore);
					timeStepPauseToIgnore = Math.max(0, timeStepPauseToIgnore-1); //decrementing and making sure it won't go below zero
				}
			}
		}
		System.out.println();
	}
}
