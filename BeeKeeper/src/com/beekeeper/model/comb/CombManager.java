package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.beekeeper.controller.AgentFactory;
import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.controller.threadedexecution.WorkDispatcher;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

/**
 * Class keeping track of which combs or facing which, and redistributing the stimulusManagers
 *
 */
public class CombManager {

	private ArrayList<Comb> combs = new ArrayList<>();
	private ArrayList<StimuliManager> stimuliManagers = new ArrayList<>();

	private HashMap<Integer, StimuliManagerServices> combsUpManagers = new HashMap<>();
	
	private ArrayList<Agent> loggerBees = new ArrayList<>();

	private Dimension combSize = new Dimension(78,50);
	
	private int startingPopulationOfThreadPoll = 5;
	private WorkDispatcher workDispatcher = new WorkDispatcher(startingPopulationOfThreadPoll);

	private CombManagerServices combManagerServices = new CombManagerServices() {		
		@Override
		public boolean isFacingAComb(int combID) {
			return getCombFacingID(combID) != null;
		}

		@Override
		public CombCell getFacingCombCell(int combID, int combCellIndex) {
			Comb facingComb = getCombFacingID(combID);
			if(facingComb != null)
			{
				return facingComb.getCell(combCellIndex);
			}
			return null;
		}

		@Override
		public void switchAgentHostComb(int startCombID, Agent who) {
			getCombOfID(startCombID).getServices().notifyTakeOff(who);
			getCombFacingID(startCombID).getServices().notifyLanding(who);

			//System.out.println("Agent" + who.getID() + " switched from comb" + startCombID + " to comb" + getCombFacingID(startCombID).ID + " on cell" + who.hostCell.number);

		}

		@Override
		public Comb getFacingComb(int combID) {
			return getCombFacingID(combID);
		}

		@Override
		public void liftFrame(int frameIndex) {
			CombManager.this.liftFrame(frameIndex);
		}

		@Override
		public void putFrame(int frameIndex, int pos, boolean reverse) {
			CombManager.this.putFrame(frameIndex, pos, reverse);
		}

		@Override
		public boolean isCombUp(int combID) {
			return CombManager.this.isCombUp(combID);
		}
	};
	
	public void printCombPopulations()
	{
		System.out.println("\n" + getNumberOfAgents() + " agents:");
		for(Comb c : combs)
		{
			System.out.println(c.ID + ": " + c.getAgents().size());
		}
	}
	
	public int getNumberOfAgents()
	{
		int n = 0;
		
		for(Comb c : combs)
		{
			n += c.getAgents().size();
		}
		
		return n;
	}
	
	public void logTurn(MyLogger logger, int turnIndex) throws InterruptedException
	{
		for(int i = 0; i < combs.size(); i++)
		{
			for(Agent a : combs.get(i).getAgents())
			{
				WorkingAgent w = (WorkingAgent) a;
				logger.log(String.valueOf(turnIndex), String.valueOf(w.getID()), w.getTaskName(), String.valueOf(w.getPhysio()), String.valueOf(w.getEO()), String.valueOf(w.getRealAge()), String.valueOf(w.getTotalExchangedAmount()));
			}
		}
	}
	
	public void liveAgents(boolean timeAccelerated) throws InterruptedException
	{
		liveAgentsThreaded();	
		
		/*
		if(!timeAccelerated)
		{
			liveAgentsThreaded();	
		}
		else
		{
			for(int i = 0; i < combs.size(); ++i)
			{
				Comb c = combs.get(i);
				Iterator<Agent> it = new ArrayList<Agent>(c.getAgents()).iterator();
				while(it.hasNext())
				{
					it.next().live();
				}
				
				//for(Agent a : c.getAgents())
				//{
				//	a.live();
				//}
			}
		}
		*/
	}
	
	public void shutDownExecutionThreads()
	{
		workDispatcher.stopAllThreads(combs.get(0).getAgents().get(0));
	}
	
	private void liveAgentsThreaded() throws InterruptedException
	{
		/******* OLD VERSION USING TERMINATING THREADS *******/
		/*		 
		ArrayList<Thread> threads = new ArrayList<>();
		MyThreadedExecutor exec = new MyThreadedExecutor(combs.get(0).getAgents());
		Thread t = new Thread(exec);
		threads.add(t);
		t.setName("LivingAgents0");
		//System.out.println("start0");
		t.start();
		
		//System.out.println("frame 0");
		
		for(int i = 1; i < combs.size()-1; i+=2)
		{
			//System.out.print(i);
			ArrayList<Agent> agents = new ArrayList<>(combs.get(i).getAgents());
			//System.out.println(i+1);
			agents.addAll(combs.get(i+1).getAgents());
			exec = new MyThreadedExecutor(agents);
			t = new Thread(exec);
			t.setName("LivingAgents"+i);
			threads.add(t);
			//System.out.println("start"+i);
			t.start();
		}
		
		exec = new MyThreadedExecutor(combs.get(combs.size()-1).getAgents());
		t = new Thread(exec);
		threads.add(t);
		t.setName("LivingAgentsLast");
		//System.out.println("startlast");
		t.start();
		//System.out.println("Started " + threads.size() + " threads");
		
		for(Thread th : threads)
		{
			//System.out.println("Waiting");
			th.join();
		}
		*/
		
		/******* NEW VERSION USING PERSISTENT THREAD POLL *****/
		
		boolean debugWorkAllocation = false;
		
		ArrayList<Agent> lastInsideCombList = new ArrayList<>();
		
		for(int i = 0; i < combs.size(); i+=2)
		{
			Comb faceA = combs.get(i);
			Comb faceB = combs.get(i+1);
			
			if(faceA.isUp())
			{
				if(lastInsideCombList.size() > 0)
				{
					if(debugWorkAllocation)System.out.println("launching memory alone");
					workDispatcher.getThatWorkDone(lastInsideCombList);
					lastInsideCombList.clear();
				}

				if(debugWorkAllocation)System.out.println("launching " + i + " " + (i+1));
				workDispatcher.getThatWorkDone(faceA.getAgents());
				workDispatcher.getThatWorkDone(faceB.getAgents());
			}
			else
			{
				if(debugWorkAllocation)System.out.println("launching " + i + " with memory and saving " + (i+1));
				lastInsideCombList.addAll(new ArrayList<Agent>(faceA.getAgents()));
				workDispatcher.getThatWorkDone(lastInsideCombList);
				lastInsideCombList = new ArrayList<Agent>(faceB.getAgents());
			}
		}
		
		if(lastInsideCombList.size() > 0)
		{
			if(debugWorkAllocation)System.out.println("launching memory outfor");
			workDispatcher.getThatWorkDone(lastInsideCombList);
			lastInsideCombList.clear();
		}
		
		workDispatcher.waitForWorkToEnd();
		if(debugWorkAllocation)System.out.println("TheEnd\n");
	}
	
	public void notifyDead(Agent a)
	{
		if(a.getBeeType() == AgentType.BROOD_BEE)
		{
			a.hostCell.inside = null;
			a.hostCell.content = CellContent.empty;
			a.hostCell.notifyAgentLeft(a);
		}
		else if(a.isInside())
		{
			a.hostCell.freeCell();
		}
	}

	public void liftFrame(int frameIndex)
	{
		//which combs ? frameIndex*2 et frameIndex*2+1
		Comb cA = getCombOfID(frameIndex*2);
		Comb cB = getCombOfID(frameIndex*2+1);
		
		System.out.println("lifting F" + frameIndex);

		//Lift the combs
		//Create two new temporary stimuli managers from the existings ones
		StimuliManagerServices smca = cA.getServicesOfClone();
		StimuliManagerServices smcb = cB.getServicesOfClone();
		combsUpManagers.put(cA.ID, smca);
		combsUpManagers.put(cB.ID, smcb);

		reAttributeStimuliManagers();
	}

	public void putFrame(int frameIndex, int pos, boolean reverse)
	{		
		System.out.println("Droping F" + frameIndex + " at " + pos + " r?" + reverse);
		
		showCombsIndexAsList();
		//is pos taken ?
		if(combsUpManagers.containsKey(combs.get(pos*2).ID))
		{
			//target is up
			//Which combs ?
			Comb combToDownA = getCombOfID(frameIndex*2);
			Comb combToDownB = getCombOfID(frameIndex*2+1);
			//Switch up combs
			if(pos != combs.indexOf(combToDownA)/2)
			{
				switchFrames(pos, combs.indexOf(combToDownA)/2, false);				
			}
			else
			{				
				//System.out.println("Putting frame back where it was");
			}
			//Merge StimuliManagers
			stimuliManagers.get(pos).mergeWith(combsUpManagers.get(combToDownA.ID));
			stimuliManagers.get(pos+1).mergeWith(combsUpManagers.get(combToDownB.ID));
			//Remove combs from Up
			combsUpManagers.remove(combToDownA.ID);
			combsUpManagers.remove(combToDownB.ID);

			if(reverse)
			{
				reverseFrame(pos);
			}
			showCombsIndexAsList();
			reAttributeStimuliManagers();
		}
		//target is already taken
	}

	private Comb getCombFacingID(int combID)
	{
		if(isCombUp(combID))
		{
			//Comb is up
			return null;
		}

		for(int i = 0; i < combs.size(); ++i)
		{
			if(combs.get(i).ID == combID)
			{
				if(i != 0 && i != combs.size()-1)
				{
					int otherCombIndex = i + (i%2==0 ? -1 : 1);
					if(isCombUp(otherCombIndex))
					{
						//System.out.println(combID + " to " + otherCombIndex + ": access denied, its up.");
						return null;
					}
					return combs.get(otherCombIndex);
				}
			}
		}
		return null;
	}

	public Comb getCombOfID(int combID)
	{
		for(int i = 0; i < combs.size(); ++i)
		{
			if(combs.get(i).ID == combID)
			{
				return combs.get(i);
			}
		}
		return null;
	}


	public ArrayList<CombServices> getCombsServices()
	{
		ArrayList<CombServices> s = new ArrayList<>();
		combs.forEach((c)->s.add(c.getServices()));
		return s;
	}

	public ArrayList<StimuliManager> getStimuliManagers()
	{
		return stimuliManagers;
	}

	public ArrayList<Comb> initiateFrames(int numberOfFrames, AgentFactory agentFactory, MainControllerServices controlServices)
	{
		Point2D.Double center = new Point2D.Double(combSize.width/2,combSize.height/2);

		for(int i = 0; i < numberOfFrames+1;++i)
		{
			stimuliManagers.add(new StimuliManager(combSize,i));
		}
		//System.out.println("Created " + (numberOfCombs+1) + " StimuliManagers");

		for(int combNumber = 0; combNumber < numberOfFrames*2; ++combNumber)
		{
			StimuliManager sm = stimuliManagers.get((combNumber+1)/2);
			Comb c = new Comb(combNumber, combSize,sm.getServices(), this.combManagerServices);
			
			int numberOfLarvae = (int) (ModelParameters.NUMBER_LARVAE / numberOfFrames * getLarvaCoef(numberOfFrames*2, combNumber+1));
			
			//int combWidthDivisor = ((int)(Math.min(combSize.height, combSize.width)/2 * 1/(Math.sqrt(numberOfLarvae / Math.PI))) - 1);
			
			double radiusForLarvae = Math.sqrt(numberOfLarvae / Math.PI / 2) * 1.5;
			
			//System.out.println("radiusForLarvae " + radiusForLarvae);

			if(combNumber == 1 && ModelParameters.SPAWN_A_QUEEN)
			{
				agentFactory.spawnAQueen(c, MyUtils.getCirclePointRule(center, Math.min(combSize.height, combSize.width)/2), sm.getServices(), controlServices);
			}
					
			//agentFactory.spawnBroodCells(numberOfLarvae, c, MyUtils.getCirclePointRule(center, Math.min(combSize.height, combSize.width)/combWidthDivisor/2), sm.getServices(), controlServices);
			agentFactory.spawnBroodCells(numberOfLarvae, c, MyUtils.getCirclePointRule(center, radiusForLarvae), sm.getServices(), controlServices);
			agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES/numberOfFrames/2, c, sm.getServices(), controlServices);
			
			c.addFood();

			this.combs.add(c);
		}
		
		//LOGGING BEE DATA
		if(ModelParameters.BEELOGGING)
		{
			ArrayList<Integer> ids = new ArrayList<>();
			
			for(int i = 0; i < ModelParameters.NB_BEE_LOGGING; ++i)
			{
				Comb c = combs.get((int)(Math.random() * combs.size()));
				Agent a;
				do
				{
					a = c.getAgents().get((int)(Math.random() * c.getAgents().size()));
				}while(a.getBeeType() != AgentType.ADULT_BEE || ids.contains(a.getID()));
				
				ids.add(a.getID());
				a.activateLogger();
				
				loggerBees.add(a);
			}
		}

		return combs;
	}
	
	public void terminateBeeLogging()
	{
		for(Agent a : loggerBees)
		{
			a.terminateLogging();
		}
	}
	
	private float getLarvaCoef(int totalCombAmount, int combNumber)
	{
		float middle = (totalCombAmount + 1) / 2.0f;
		
		float distance = Math.abs(combNumber * 1.0f - middle);
		
		float coef = 1 - distance / middle;
		
		//System.out.println(combNumber + " / " + totalCombAmount + " mid:" + middle + " d:" + distance + " -> " + coef);
		
		return coef;
	}

	public void hitFrame(int frameIndex)
	{
		//Get agents of hit combs
		Comb ca = combs.get(frameIndex*2);
		Comb cb = combs.get(frameIndex*2+1);

		spreadAgentsAround(ca, frameIndex*2, frameIndex*2+1);
		spreadAgentsAround(cb, frameIndex*2, frameIndex*2+1);
	}

	private void spreadAgentsAround(Comb c, int sourceA, int sourceB)
	{
		float dropRate = 0.95f;
		
		Iterator<Agent> it = c.getAgents().iterator();

		while(it.hasNext())
		{
			Agent a = it.next();
			
			CombCell oldCell = a.hostCell;

			if(Math.random() < dropRate && a.isInside())
			{
				//spread agents randomly inside those landing zones
				
				int newCombId;
				do
				{
					newCombId = (int)(Math.random() * combs.size());
				}while(newCombId == sourceA || newCombId == sourceB);

				it.remove();
				
				CombCell newHost = combs.get(newCombId).getRandomFreeVisitCell();
				newHost.notifyLanding((EmitterAgent) a);

				oldCell.visiting = null;
				
				a.hostCell = newHost;				
				
				//System.out.println(a.getStringName() + " " + c.ID + " from " + oldCell.number + " to " + newCombId + " on cell " + a.hostCell.number);
			}
		}
	}

	public void updateStimuli()
	{
		//int i = 0;
		for(StimuliManager s : stimuliManagers)
		{
			s.updateStimuli();

			//DEBUG
			//System.out.println("SM" + (++i) + "/" + stimuliManagers.size());
			//MyUtils.showSexyHashMap(s.getTotalAmounts());
		}

		combsUpManagers.forEach((Integer combID, StimuliManagerServices sm) -> 
		{
			sm.updateStimuli();
		});
	}


	public void showCombsIndexAsList()
	{
		combs.forEach((Comb c) -> System.out.print(c.ID + " "));
		System.out.println();
	}

	public void reverseFrame(int frameIndex)
	{
		//showCombsIndexAsList();
		MyUtils.switchElementsInList(combs, frameIndex*2, frameIndex*2+1);
		//showCombsIndexAsList();
	}

	private boolean isCombUp(int combID)
	{
		return combsUpManagers.containsKey(combID);
	}

	private void reAttributeStimuliManagers()
	{
		for(int i = 0; i < combs.size(); ++i)
		{
			//System.out.println("Attributing SM" + ((i+1)/2) + " to comb" + combs.get(i).ID);
			if(isCombUp(i))
			{
				combs.get(i).registerNewSManager(combsUpManagers.get(i));
			}
			else
			{
				combs.get(i).registerNewSManager(stimuliManagers.get((i+1)/2).getServices());			
			}
		}			

	}

	public void switchFrames(int frame1, int frame2)
	{
		switchFrames(frame1, frame2, true);
	}

	private void switchFrames(int frame1, int frame2, boolean reatribute)
	{
		System.out.println("Switching " + frame1 + " and " + frame2);
		int index1 = 2*frame1;
		int index2 = 2*frame2;
		//showCombsIndexAsList();
		MyUtils.switchElementsInList(combs, index1, index2);
		MyUtils.switchElementsInList(combs, index1+1, index2+1);
		if(reatribute)reAttributeStimuliManagers();
		//showCombsIndexAsList();
	}
}
