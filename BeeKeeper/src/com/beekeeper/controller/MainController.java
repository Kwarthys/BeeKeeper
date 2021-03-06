package com.beekeeper.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.ihm.CombDrawer;
import com.beekeeper.ihm.TaskGrapher;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentStateSnapshot;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.CombManager;
import com.beekeeper.model.comb.CombServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyLockedList;
import com.beekeeper.utils.MyUtils;

public class MainController
{
	ArrayList<CombDrawer> drawers = new ArrayList<CombDrawer>();

	private ArrayList<Comb> combs = new ArrayList<Comb>();

	private MyLogger logger;

	private CombManager combManager;

	private BeeWindow window;

	private AgentFactory agentFactory;

	//private ArrayList<Agent> foragers = new ArrayList<>();
	private List<Agent> foragers = Collections.synchronizedList(new ArrayList<Agent>());
	private ArrayList<Integer> foragersIDS = new ArrayList<>();  //shortcut to avoid thread collapse
	private MyLockedList<Integer> deadAdults = new MyLockedList<>();

	private List<Agent> newLandings = Collections.synchronizedList(new ArrayList<Agent>());;	 
	private List<Agent> newLiftOffs = Collections.synchronizedList(new ArrayList<Agent>());;	
	private List<Agent> newDeds = Collections.synchronizedList(new ArrayList<Agent>());;

	//private int simuStep = 0;

	private boolean closed;

	private int timeStepPauseToIgnore = 100000;//TODO SET BACK UP TO 0 : this was for demo sake

	private volatile int turnIndex = -1;

	//private HashMap<Integer, Double> contactsQuantitiesByIndex = new HashMap<>();
	//private boolean contactsLocked = false;

	private volatile boolean restartAsked = false;

	private volatile boolean rebaseAsked = false;
	private int[] rebaseData;
	private boolean rebaseKeepForagers = false;

	private volatile boolean timeStepOver = false;
	
	private int lastAllAdultsCount = 1000;
	private boolean refreshAllAdultCount = true;

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

		public void notifyDeath(Agent a) {
			
			combManager.notifyDead(a);
			agentFactory.allAgents.remove(a);	
			
			synchronized (newDeds) {
				if(!newDeds.contains(a))
				{
					newDeds.add(a);					
				}
			}
			/*
			combManager.notifyDead(a);
			agentFactory.allAgents.remove(a);

			if(a.getBeeType() != AgentType.BROOD_BEE && !deadAdults.contains(a.getID()))
			{
				deadAdults.waitAndPost(a.getID());				
			}
			*/
			/*
			while(contactsLocked) {}; //Might be a problem to actively wait for lock. Should be ok tho.
			contactsQuantitiesByIndex.remove(a.getID());
			*/
		}

		@Override
		public void askRestart()
		{
			//MainController.this.restartAsked = true;
			restartAsked = true;
			System.out.println("RestartAsked on " + this);
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
			return foragersIDS;
		}

		@Override
		public ArrayList<Integer> getTheDead() {
			return deadAdults.waitGetFullCopyAndEmpty();
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

		/*
		@Override
		public void notifyAgentContact(int id1, int id2, double amount) {
			//System.out.println("contact " + id1 + " " + id2 + " -> " + amount);
			registerContactFor(id1, amount);
			registerContactFor(id2, amount);
			//String s = id1 + " " + id2 + " " + (int)amount;
			//boolean add = contactBlockingQueue.add(s);
			//System.out.println(id1 + "-" + id2 + " exchanged " + amount + " : " + add);
		}
		*/

		/*
		@Override
		public HashMap<Integer, Double> getAgentContacts() {
			contactsLocked = true;
			return contactsQuantitiesByIndex;
		}

		@Override
		public void freeLockAgentContacts() {
			contactsLocked = false;
		}
		*/
		
		@Override
		public boolean isFastForward() {
			return MainController.this.timeStepPauseToIgnore != 0 || ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP == 0;
		}

		@Override
		public void spawnBeeAt(int combID, Point position) {
			agentFactory.spawnWorkerAt(combManager.getCombOfID(combID), position, MainController.this.controlServices);
		}

		@Override
		public /*synchronized*/ void notifyLiftoff(Agent agent)
		{
			synchronized (newLiftOffs)
			{				
				newLiftOffs.add(agent);
			}
			
			//synchronized (foragers) {
			//	foragers.add(agent);
			//	foragersIDS.add(agent.getID());
			//}
		}

		@Override
		public /*synchronized*/ void askLanding(Agent agent) {
			//foragers.remove(agent);
			synchronized (newLandings)
			{
				newLandings.add(agent);
			}
			//moved to mainloop
			//int index = foragersIDS.indexOf(agent.getID());
			//if(index != -1)
			//{
			//	foragersIDS.remove(index);				
			//}
		}

		@Override
		public void waitForTimeStep()
		{
			while(!timeStepOver && !restartAsked);
			timeStepOver = false;
			return;
		}

		@Override
		public void hitFrame(int frameIndex) {
			combManager.hitFrame(frameIndex);
		}

		@Override
		public void rebase(int[] frameIds, boolean keepForagers) {
			rebaseData = frameIds;
			rebaseAsked = true;	
			rebaseKeepForagers = keepForagers;
		}

		@Override
		public int getAllAdultsCount() {
			refreshAllAdultCount = true;
			return lastAllAdultsCount;
		}
	};

	public MainController()
	{
		this.agentFactory = new AgentFactory();

		ModelParameters.applyPhysioParameters();

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

		if(ModelParameters.LOGGING)
		{
			logger = new MyLogger();
		}

		combManager.printCombPopulations();

		System.out.println("MC Built");
	}

	public MainControllerServices getServices()
	{
		return controlServices;
	}

	public boolean start()
	{		
		boolean restart = programLoop();

		if(this.window != null)
		{
			this.window.dispose();
		}

		if(ModelParameters.LOGGING)this.logger.closing();
		if(ModelParameters.BEELOGGING)this.combManager.terminateBeeLogging();
		this.combManager.shutDownExecutionThreads();

		try {
			//System.out.println("Waiting");
			if(ModelParameters.LOGGING)this.logger.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//System.out.println("expe done");

		return restart;
	}

	/*
	private void registerContactFor(int agentIndex, double quantity)
	{
		if(contactsLocked)
		{
			//System.out.println("registerContactFor : Locked");
			return; //If map is being read by another thread, we're throwing data away : shouldn't be much of an issue given the shear amount of data
		}

		//System.out.println("Quanttt " + quantity);

		if(agentFactory.typesOfIndex.get(agentIndex) == AgentType.ADULT_BEE)
		{
			if(!contactsQuantitiesByIndex.containsKey(agentIndex))
			{
				contactsQuantitiesByIndex.put(agentIndex, quantity);
			}
			else
			{
				if(contactsQuantitiesByIndex.get(agentIndex)==null)
				{
					System.out.println("quantity inside contactsQuantitiesByIndex is null - MainController - registerContactFor");
				}
				
				double newValue = contactsQuantitiesByIndex.get(agentIndex) + quantity;
				//TODO rare nullPointerException here, added the SYNCHRONIZED to check, and removed it
				contactsQuantitiesByIndex.put(agentIndex, newValue); // 2
			}
		}
	}
	*/

	private void setNbOfTimeStepToGoFast(int number)
	{
		timeStepPauseToIgnore = number;
	}
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

	/*
	private void logTurn(int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		logger.log(turnIndex, beeID, beeTaskName, beePhysio);
	}
	 */
	
	private void updateAllAgentCount()
	{
		int count = 0;
		
		for(Agent a : combManager.getTotalCombPopulation())
		{
			if(a != null) //can happen if agent died recently, i guess
			{
				if(a.getBeeType() == AgentType.ADULT_BEE || a.getBeeType() == AgentType.QUEEN)
				{
					count++;
				}
			}
		}
		
		count += foragers.size();
		
		
		/**** Dunno why but foragers are not included in agentFactory.allAgents ****/ 
		/*
		for(Agent a : agentFactory.allAgents)
		{
			if(a != null) //can happen if agent died recently, i guess
			{
				if(a.getBeeType() == AgentType.ADULT_BEE || a.getBeeType() == AgentType.QUEEN)
				{
					count++;
				}
				else
				{
					otherCount++;
				}
			}
		}
		*/		
		
		lastAllAdultsCount = count;
	}

	private boolean programLoop()
	{		
		boolean DEBUGTIME = false;
		boolean MONITORTIME = false;
		int minLoopMs = -1;
		int maxLoopMs = 0;
		long totalLoopMs = 0;
		long totalAgents = 0;
		long totalDeaths = 0;
		int startingAverageIndex = 0;

		int logTurnInterval = ModelParameters.SIMU_LENGTH / 200; //Want 200 logs
		logTurnInterval = Math.max(logTurnInterval, 1); //can't be lower than 1 or that % will explode

		turnIndex = 0;
		int displayBar = 100;

		System.out.print("|");
		for(int i = 1; i < displayBar-1; ++i)
		{			
			System.out.print("-");
		}
		System.out.println("|");

		if(ModelParameters.LOGGING)logger.log("TurnIndex", "BeeID", "TaskName", "HJ", "EO", "RealAge", "pHExchanged");

		while(turnIndex < ModelParameters.SIMU_LENGTH && !closed && !restartAsked)
		{
			long startLoopTime = System.nanoTime();

			//System.out.println("turn " + turnIndex + "services:" + getServices() + " | " + agentFactory.allAgents.size() + " agents.");
			//System.out.println("\n\nturn " + turnIndex);

			if(turnIndex%(int)(ModelParameters.SIMU_LENGTH/displayBar) == 0)
			{
				System.out.print("|");
			}

			turnIndex++;
			/*
			System.out.println(turnIndex + " FORK");

			if(turnIndex%logTurnInterval == 0 && ModelParameters.LOGGING)
			{
				System.out.println("LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOGGING");
			}
			 */
			try {
				combManager.liveAgents(timeStepPauseToIgnore != 0, foragers);

				if(turnIndex%logTurnInterval == 1 && ModelParameters.LOGGING)
				{
					combManager.logTurn(logger, turnIndex, foragers);
					//System.out.println("Logging " + turnIndex + "/" + ModelParameters.SIMU_LENGTH);
				}

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			//System.out.println(turnIndex + " JOIN into FORAGERS");

			//long milis = System.nanoTime();

			//int foragersNb = foragers.size();
			//if(turnIndex%200==0)System.out.println(foragersNb + " foragers / " + (combManager.getNumberOfAgents() + foragersNb) + " total.");

			//System.out.println("Starting forager turn " + turnIndex + " total agents " + agentFactory.allAgents.size() + " (" + foragers.size() + ") " + System.nanoTime());

			/**** NEW FORAGER MANAGEMENT ****/
			boolean debugLandings = false;
			
			Iterator<Agent> foragerIterator = newLandings.iterator();
			StringBuffer sb = new StringBuffer();
			if(debugLandings)sb.append("Landings :");
			while(foragerIterator.hasNext())
			{
				Agent a = foragerIterator.next();
				
				CombCell cell = controlServices.askLandingZone();
				if(cell != null)
				{
					//Successfully inside
					a.hostCell = cell;
					cell.notifyLanding((EmitterAgent)a);

					if(debugLandings)
					{
						sb.append(" ");
						sb.append(a.getID());
						sb.append("(");
						sb.append(cell.number);
						sb.append(")");
					}
					
					foragers.remove(a);				
					foragersIDS.remove(new Integer(a.getID()));
				}
				else
				{
					System.out.println(a.getID() + " Couldn't land.");
					//couldn't enter, will check next turn
				}
			}
			if(debugLandings && newLandings.size() > 0)System.out.println(sb.toString());
			newLandings.clear();

			foragerIterator = newLiftOffs.iterator();
			while(foragerIterator.hasNext())
			{
				Agent a = foragerIterator.next();
				foragers.add(a);
				foragersIDS.add(a.getID());
				
				foragerIterator.remove();
			}
			
			if(turnIndex % 1000 == 0)
			{
				//Clean dead foragers
				foragerIterator = foragers.iterator();
				while(foragerIterator.hasNext())
				{
					Agent a = foragerIterator.next();
					if(!a.alive)
					{
						foragerIterator.remove();
						//System.out.println("removed ded forager " + a.getID());
					}
				}
			}
			/**** OLD FORAGER MANAGEMENT ****/
			/*
			synchronized (foragers)
			{
				Iterator<Agent> foragersIT = foragers.iterator();
				while(foragersIT.hasNext())
				{	

					try
					{
						Agent a = foragersIT.next();	
						//Agent a = foragersIT.next(); // CONCURRENT MODIFICATION EXCEPTION HERE :: should be fixed

						if(a==null)
						{
							System.out.println("MainController loop : Null entry in the foragers - that is weird");
							//TODO investigate null here
						}
						else
						{
							a.live();
							if(turnIndex%logTurnInterval == 0 && ModelParameters.LOGGING)
							{
								a.logTurn(logger, turnIndex);
							}
						}

						if(!a.alive)
						{
							foragersIT.remove();
						}

					}		
					catch (ConcurrentModificationException e)
					{
						System.out.println("Exception " + System.nanoTime());
						e.printStackTrace();
						System.exit(1);

					}
				}

				//long timeForagers = (System.nanoTime() - milis)/1000;
				//if(timeForagers > 2000)System.out.println("Foragers took " + timeForagers + "us.");

				Iterator<Agent> it = newLandings.iterator();
				while(it.hasNext())
				{
					Agent a = it.next();
					foragers.remove(a);
					it.remove();
				}


			}
			*/
			//System.out.println("Finished forager turn " + turnIndex + " total agents " + agentFactory.allAgents.size() + " (" + foragers.size() + ")");

			if(DEBUGTIME)System.out.println("AllAgent lived at t+" + (System.nanoTime() - startLoopTime)/1000000 + "ms.");
			if(MONITORTIME)
				totalAgents += (System.nanoTime() - startLoopTime)/1000000;

			if(MONITORTIME)
				totalDeaths += (System.nanoTime() - startLoopTime)/1000000;

			if(DEBUGTIME)System.out.println("Deaths cleanup at t+" + (System.nanoTime() - startLoopTime)/1000000 + "ms.");

			this.combManager.updateStimuli();

			if(DEBUGTIME)System.out.println("updateStimuli at t+" + (System.nanoTime() - startLoopTime)/1000000 + "ms.");

			/* ******* THIS HAS BEEN MOVED INSIDE EACH AGENTS BODYSMELL
			if(!contactsLocked)
			{
				contactsQuantitiesByIndex.forEach((Integer beeID, Double amount) -> {amount*=0.99;});
			}
			*/

			if(rebaseAsked)
			{
				rebaseAsked = false;

				for(Comb c : combs)
				{
					boolean found = false;

					for(int datai = 0; datai < rebaseData.length && !found; datai++)
					{
						if(rebaseData[datai] == c.ID/2) //converting from combID to FrameID
						{
							found = true;
						}
					}

					if(!found)
					{
						c.reset(); // reset if not in the list
					}
				}

				System.out.println("rebaseKeepForagers " + rebaseKeepForagers);

				if(!rebaseKeepForagers)
				{
					Iterator<Agent> fit = foragers.iterator();
					while(fit.hasNext())
					{
						Agent a = fit.next();
						a.alive = false;
					}
				}
			}
			
			if(refreshAllAdultCount)
			{
				updateAllAgentCount();
				refreshAllAdultCount = false;
			}
			
			Iterator<Agent> dedIterator = newDeds.iterator();
			while(dedIterator.hasNext())
			{
				Agent a = dedIterator.next();

				if(a.getBeeType() != AgentType.BROOD_BEE && !deadAdults.contains(a.getID()))
				{
					deadAdults.waitAndPost(a.getID());
				}
				
				dedIterator.remove();
			}

			timeStepOver = true;

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

				if(turnIndex%3600*ModelParameters.secondToTimeStepCoef == 0)
				{
					StringBuffer theLog = new StringBuffer();
					theLog.append("1h Average: ");
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

					theLog.append(" || Turn ");
					theLog.append(turnIndex);

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

		return restartAsked;

	}
}
