package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.beekeeper.controller.AgentFactory;
import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.EmitterAgent;
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

	private Dimension combSize = new Dimension(78,50);

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

	public void liftFrame(int frameIndex)
	{
		//which combs ? frameIndex*2 et frameIndex*2+1
		Comb cA = getCombOfID(frameIndex*2);
		Comb cB = getCombOfID(frameIndex*2+1);

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
			//System.out.println("Creating the comb " + combNumber + " associated to SM" + (combNumber+1)/2);
			StimuliManager sm = stimuliManagers.get((combNumber+1)/2);
			Comb c = new Comb(combNumber, combSize,sm.getServices(), this.combManagerServices);
			
			int combWidthDivisor = (int)(Math.min(combSize.height, combSize.width)/2 * 1/(Math.sqrt(ModelParameters.NUMBER_LARVAE / Math.PI)));
			System.out.println("combWidthDivisor: " + combWidthDivisor);
			
			/*
			int combWidthDivisor = 30;
			while(combSize.width/combWidthDivisor * combSize.width/combWidthDivisor * Math.PI < ModelParameters.NUMBER_LARVAE)
			{
				--combWidthDivisor;
				System.out.println(combWidthDivisor);
			}
			System.out.println("bite");
			*/
			if(combNumber == 1)
			{
				agentFactory.spawnAQueen(c, MyUtils.getCirclePointRule(center, Math.min(combSize.height, combSize.width)/2), sm.getServices(), controlServices);
				//agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES*numberOfFrames*2, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
				//agentFactory.spawnWorkers(100, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
			}
			System.out.println("queen");
			agentFactory.spawnBroodCells(ModelParameters.NUMBER_LARVAE, c, MyUtils.getCirclePointRule(center, Math.min(combSize.height, combSize.width)/combWidthDivisor/2), sm.getServices(), controlServices);
			System.out.println("brood");
			agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES/numberOfFrames/2, c, sm.getServices(), controlServices);
			//agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES/numberOfFrames/2, c, MyUtils.getCirclePointRule(center, Math.min(combSize.height, combSize.width)/2), sm.getServices(), controlServices);
			System.out.println("workers");
			
			this.combs.add(c);

			c.addFood();

		}

		return combs;
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
