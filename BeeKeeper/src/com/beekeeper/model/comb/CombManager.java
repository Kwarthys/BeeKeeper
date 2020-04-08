package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.controller.AgentFactory;
import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

/**
 * Class keeping track of which combs or facing which, and redistributing the stimulusManagers
 *
 */
public class CombManager {

	private ArrayList<Comb> combs = new ArrayList<>();
	private ArrayList<StimuliManager> stimuliManagers = new ArrayList<>();

	private Dimension combSize = new Dimension(30,30);

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
	};

	private Comb getCombFacingID(int combID)
	{
		for(int i = 0; i < combs.size(); ++i)
		{
			if(combs.get(i).ID == combID)
			{
				if(i != 0 && i != combs.size()-1)
				{
					int otherCombIndex = i + (i%2==0 ? -1 : 1);
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
			//System.out.println("Creatin the comb " + combNumber + " associated to SM" + (combNumber+1)/2);
			StimuliManager sm = stimuliManagers.get((combNumber+1)/2);
			Comb c = new Comb(combNumber, combSize,sm.getServices(), this.combManagerServices);


			int combWidthDivisor = 10;

			while(combSize.width/combWidthDivisor * combSize.width/combWidthDivisor * Math.PI < ModelParameters.NUMBER_LARVAE)
			{
				--combWidthDivisor;
			}

			if(combNumber == 1)
			{
				agentFactory.spawnAQueen(c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
				agentFactory.spawnAQueen(c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
				agentFactory.spawnAQueen(c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
				agentFactory.spawnAQueen(c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
				//agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES*numberOfFrames*2, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
				//agentFactory.spawnWorkers(100, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
			}
			agentFactory.spawnBroodCells(ModelParameters.NUMBER_LARVAE/3, c, MyUtils.getCirclePointRule(center, combSize.width/combWidthDivisor), sm.getServices(), controlServices);
			agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);

			this.combs.add(c);	

			c.addFood();
			
		}
		
		return combs;
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
		showCombsIndexAsList();

		reAttributeStimuliManagers();
	}

	private void reAttributeStimuliManagers()
	{
		for(int i = 0; i < combs.size(); ++i)
		{
			//System.out.println("Attributing SM" + ((i+1)/2) + " to comb" + combs.get(i).ID);
			combs.get(i).registerNewSManager(stimuliManagers.get((i+1)/2).getServices());
		}
	}

	public void switchFrames(int frame1, int frame2)
	{
		int index1 = 2*frame1;
		int index2 = 2*frame2;
		//showCombsIndexAsList();
		MyUtils.switchElementsInList(combs, index1, index2);
		MyUtils.switchElementsInList(combs, index1+1, index2+1);
		showCombsIndexAsList();

		reAttributeStimuliManagers();

	}
}
