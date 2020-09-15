package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.agent.implem.AdultBee;
import com.beekeeper.model.agent.implem.BroodBee;
import com.beekeeper.model.agent.implem.QueenBee;
import com.beekeeper.model.agent.implem.TestAgent;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.utils.CustomRule;
import com.beekeeper.utils.MyUtils;

public class AgentFactory
{
	public ArrayList<Agent> allAgents = new ArrayList<>();
	
	public HashMap<Integer, AgentType> typesOfIndex = new HashMap<>();

	public void spawnTestAgents(int number, Comb host, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(rule);
			int x;
			int y;			
			
			do
			{
				point = MyUtils.getPointInRule(rule);
				x = (int)point.x;
				y = (int)point.y;
			}while(!host.isCellVisitEmpty(x, y));
			
			WorkingAgent bee = new TestAgent(services, controllerServices);

			host.setCellVisit(x, y, bee);

			allAgents.add(bee);
		}
	}

	public ArrayList<WorkingAgent> spawnBroodCells(int number, Comb host, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		ArrayList<WorkingAgent> bees = new ArrayList<WorkingAgent>();
		
		int fails = 0;
		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(rule);
			int x;
			int y;			
			
			do
			{
				point = MyUtils.getPointInRule(rule);
				x = (int)point.x;
				y = (int)point.y;
				if(++fails % 500 == 0)
				{
					System.err.println("AgentFactory - spawnBroodCells : WARNING can't find suitable point after " + fails + " attempts.");
				}
				
			}while(!host.isCellContentEmpty(x, y));
			
			WorkingAgent bee = new BroodBee(services, controllerServices);
			
			typesOfIndex.put(bee.getID(), AgentType.BROOD_BEE);
			
			host.setCellAgentContent(x, y, bee);
			
			allAgents.add(bee);
		}
		
		return bees;
	}

	public void spawnWorkers(int number, Comb host, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(rule);
			int x;
			int y;			
	
			do
			{
				point = MyUtils.getPointInRule(rule);
				x = (int)point.x;
				y = (int)point.y;
			}while(!host.isCellVisitEmpty(x, y));
	
			WorkingAgent bee = new AdultBee(services, controllerServices);			
			typesOfIndex.put(bee.getID(), AgentType.ADULT_BEE);
			host.setCellVisit(x, y, bee);
			allAgents.add(bee);
		}
	}

	public void spawnWorkers(int number, Comb host, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = new Point2D.Double();
			int x;
			int y;	
			
			int fails = 0;
	
			do
			{
				point.x = Math.random() * host.getDimension().width;
				point.y = Math.random() * host.getDimension().height;
				x = (int)point.x;
				y = (int)point.y;
				if(++fails % 100 == 0)
				{
					System.err.println("AgentFactory - spawnWorkers without rule : can't find suitable point.");
				}
				
			}while(!host.isCellVisitEmpty(x, y));
	
			WorkingAgent bee = new AdultBee(services, controllerServices);		
			typesOfIndex.put(bee.getID(), AgentType.ADULT_BEE);
			host.setCellVisit(x, y, bee);
			allAgents.add(bee);
		}
	}

	public void spawnALarvae(CombCell cell, Comb host, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		WorkingAgent larvae = new BroodBee(services, controllerServices);
		
		host.setCellAgentContent(cell.x, cell.y, larvae);
		
		allAgents.add(larvae);
	}

	public void spawnAQueen(Comb host, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		Point2D.Double point = MyUtils.getPointInRule(rule);
		int x;
		int y;			
		
		do
		{
			point = MyUtils.getPointInRule(rule);
			x = (int)point.x;
			y = (int)point.y;
		}while(!host.isCellContentEmpty(x, y));
		
		WorkingAgent queen = new QueenBee(services, controllerServices);	
		typesOfIndex.put(queen.getID(), AgentType.ADULT_BEE);
		
		host.setCellVisit(x, y, queen);
		
		allAgents.add(queen);
	}
}
