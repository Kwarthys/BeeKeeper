package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.agent.implem.AdultBee;
import com.beekeeper.model.agent.implem.BroodBee;
import com.beekeeper.model.agent.implem.FoodSource;
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
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			FoodSource c = new FoodSource(point.getX(), point.getY(), services); 
			cells.add(c);
			allAgents.add(c);
		}
		
		return cells;
	}

	public ArrayList<WorkingAgent> spawnBroodCells(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		ArrayList<WorkingAgent> bees = new ArrayList<WorkingAgent>();
		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			WorkingAgent bee = new BroodBee(services, controllerServices, point.getX(), point.getY());
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
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			WorkingAgent bee = new AdultBee(services, controllerServices, point.getX(), point.getY());
			bees.add(bee);
			allAgents.add(bee);
		}
		
		return bees;
	}
}
