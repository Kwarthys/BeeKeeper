package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
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
			Point2D.Double point = MyUtils.getPointInRule(rule);
			int x;
			int y;			
			
			do
			{
				point = MyUtils.getPointInRule(rule);
				x = (int)point.x;
				y = (int)point.y;
			}while(!host.isCellContentEmpty(x, y));
			
			WorkingAgent bee = new BroodBee(services, controllerServices);
			
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
		
		host.setCellVisit(x, y, queen);
		
		allAgents.add(queen);
	}
}
