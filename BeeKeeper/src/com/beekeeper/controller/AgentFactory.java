package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.agent.AdultBee;
import com.beekeeper.model.agent.BroodBee;
import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.utils.CustomRule;
import com.beekeeper.utils.MyUtils;

public class AgentFactory
{
	public ArrayList<EmptyBee> allAgents = new ArrayList<>();
	public ArrayList<CombCell> allCells = new ArrayList<>();


	public ArrayList<CombCell> spawnCombCells(int number, CustomRule<Point2D.Double> rule)
	{
		ArrayList<CombCell> cells = new ArrayList<>();
		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			CombCell c = new CombCell(point.getX(), point.getY()); 
			cells.add(c);
			allCells.add(c);
		}
		
		return cells;
	}

	public ArrayList<EmptyBee> spawnBroodCells(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		ArrayList<EmptyBee> bees = new ArrayList<EmptyBee>();
		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			EmptyBee bee = new BroodBee(services, controllerServices, point.getX(), point.getY());
			bees.add(bee);
			allAgents.add(bee);
		}
		
		return bees;
	}

	public ArrayList<EmptyBee> spawnWorkers(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices)
	{
		ArrayList<EmptyBee> bees = new ArrayList<EmptyBee>();
		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			EmptyBee bee = new AdultBee(services, controllerServices, point.getX(), point.getY());
			bees.add(bee);
			allAgents.add(bee);
		}
		
		return bees;
	}
}
