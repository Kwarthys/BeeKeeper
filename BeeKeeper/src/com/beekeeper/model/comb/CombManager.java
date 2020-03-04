package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.controller.AgentFactory;
import com.beekeeper.controller.MainControllerServices;
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

	public void initiateCombs(int numberOfCombs, AgentFactory agentFactory, MainControllerServices controlServices)
	{
		Point2D.Double center = new Point2D.Double(combSize.width/2,combSize.height/2);

		for(int i = 0; i < numberOfCombs+1;++i)
		{
			stimuliManagers.add(new StimuliManager(combSize));
		}
		//System.out.println("Created " + (numberOfCombs+1) + " StimuliManagers");

		for(int combNumber = 0; combNumber < numberOfCombs*2; ++combNumber)
		{
			//System.out.println("Creatin the comb " + combNumber + " associated to SM" + (combNumber+1)/2);
			StimuliManager sm = stimuliManagers.get((combNumber+1)/2);
			Comb c = new Comb(combSize,sm.getServices());


			int combWidthDivisor = 10;

			while(combSize.width/combWidthDivisor * combSize.width/combWidthDivisor * Math.PI < ModelParameters.NUMBER_LARVAE)
			{
				--combWidthDivisor;
			}

			stimuliManagers.add(sm);
			agentFactory.spawnBroodCells(combNumber, c, MyUtils.getCirclePointRule(center, combSize.width/combWidthDivisor), sm.getServices(), controlServices);		
			agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
			
			//agentFactory.spawnBroodCells(ModelParameters.NUMBER_LARVAE, c, MyUtils.getCirclePointRule(center, combSize.width/combWidthDivisor), sm.getServices(), controlServices);		
			//agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), controlServices);
			
			

			c.setID(combNumber);
			this.combs.add(c);	

			c.addFood();
		}
	}

	public void updateStimuli()
	{
		for(StimuliManager s : stimuliManagers)
		{
			s.updateStimuli();
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
