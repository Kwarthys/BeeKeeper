package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.agent.implem.FoodSource;
import com.beekeeper.model.agent.implem.TestAgent;
import com.beekeeper.model.agent.implem.TestEmitterAgent;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.utils.CustomRule;
import com.beekeeper.utils.MyUtils;

public class AgentFactory
{
	public ArrayList<Agent> allAgents = new ArrayList<>();


	public ArrayList<Agent> spawnFoodAgent(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services)
	{
		ArrayList<Agent> cells = new ArrayList<>();
		
		for(int i = 0; i < number; i++)
		{
			//Point2D.Double point = MyUtils.getPointInRule(rule);
			FoodSource c = null;//new FoodSource(point.getX(), point.getY(), services); 
			// TODO NULL HERE 
		    cells.add(c);
			allAgents.add(c);
		}
		
		return cells;
	}
	
	
	public ArrayList<EmitterAgent> spawnTestEmitterAgent(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services)
	{
		ArrayList<EmitterAgent> agents = new ArrayList<>();
		
		for(int i = 0; i < number; i++)
		{
			//Point2D.Double point = MyUtils.getPointInRule(rule);
			TestEmitterAgent c = null;//new TestEmitterAgent(services, point.getX(), point.getY()); 
			// TODO NULL HERE 
			agents.add(c);
			allAgents.add(c);
		}
		
		return agents;
	}

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
		
		for(int i = 0; i < number; i++)
		{
			//Point2D.Double point = MyUtils.getPointInRule(rule);
			WorkingAgent bee = null;//new BroodBee(services, controllerServices, point.getX(), point.getY());
			// TODO NULL HERE 
			bees.add(bee);
			allAgents.add(bee);
		}
		
		return bees;
	}

	public ArrayList<WorkingAgent> spawnWorkers(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		ArrayList<WorkingAgent> bees = new ArrayList<WorkingAgent>();
		
		for(int i = 0; i < number; i++)
		{
			//Point2D.Double point = MyUtils.getPointInRule(rule);
			WorkingAgent bee = null;//new AdultBee(services, controllerServices, point.getX(), point.getY());
			// TODO NULL HERE 
			bees.add(bee);
			allAgents.add(bee);
		}
		
		return bees;
	}
}
