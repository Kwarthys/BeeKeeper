package com.beekeeper.controller;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.ihm.CombDrawer;
import com.beekeeper.ihm.TaskGrapher;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.hive.BeeHive;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

public class MainController
{
	ArrayList<CombDrawer> drawers = new ArrayList<CombDrawer>();
	
	private ArrayList<Comb> combs = new ArrayList<Comb>();
	
	private ArrayList<StimuliManager> sManagers = new ArrayList<>();
	
	private MyLogger logger = new MyLogger();
	
	private BeeWindow window;
	
	private AgentFactory agentFactory;
	
	private BeeHive hive;
	
	private int simuStep = 0;

	private boolean closed;
	
	private MainControllerServices controlServices = new MainControllerServices() {

		@Override
		public void logMyTaskSwitch(Task newTask, int beeID) {
			MainController.this.logger.logTask(beeID, newTask.taskName);				
		}

		@Override
		public double getHiveTemperature(){
			return hive.getTemperature();
		}

		@Override
		public CombCell askLandingZone() {
			ArrayList<Comb> theCombs = new ArrayList<>(combs);
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

		@Override
		public void notifyWindowClosed() {
			MainController.this.closed = true;
		}
	};

	public MainController()
	{
		this.agentFactory = new AgentFactory();	
		
		Dimension combSize = new Dimension(30,30);
		
		Point2D.Double center = new Point2D.Double(combSize.width/2,combSize.height/2);
		
		this.hive = new BeeHive();
		
		for(int i = 0; i < 1; ++i)
		{
			//ArrayList<Agent> bees = new ArrayList<>();
			Comb c = new Comb(combSize);
			
			StimuliManager sm = new StimuliManager(c);
			
			int combWidthDivisor = 10;
			
			while(combSize.width/combWidthDivisor * combSize.width/combWidthDivisor * Math.PI < ModelParameters.NUMBER_LARVAE)
			{
				--combWidthDivisor;
			}
			
			System.out.println("combWidthDivisor : " + combWidthDivisor);
			
			sManagers.add(sm);
			
			agentFactory.spawnBroodCells(ModelParameters.NUMBER_LARVAE, c, MyUtils.getCirclePointRule(center, combSize.width/combWidthDivisor), sm.getServices(), this.controlServices);		
			agentFactory.spawnWorkers(ModelParameters.NUMBER_BEES, c, MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), this.controlServices);
			
			//bees.addAll(agentFactory.spawnTestEmitterAgent(30, MyUtils.getCirclePointRule(center, 50), sm.getNewServices()));
			//bees.addAll(agentFactory.spawnTestAgents(5, MyUtils.getCirclePointRule(center, 100), sm.getNewServices(), this.controlServices));	
			//bees.addAll(agentFactory.spawnTestEmitterAgent(30, c,MyUtils.getCirclePointRule(center, 50), sm.getNewServices()));
			//agentFactory.spawnTestAgents(3, c,MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), this.controlServices);
			
			c.setID(i);
			this.combs.add(c);	
			
			c.addFood();
			
			CombDrawer drawer = new CombDrawer(c.getServices(), sm.getServices());
			
			this.drawers.add(drawer);
		}
		
		TaskGrapher g = new TaskGrapher(agentFactory.allAgents);

		this.window = new BeeWindow(g,drawers, this.controlServices);
		closed = false;

		programLoop();
		
		this.window.dispose();
		
		this.logger.closing();
		
		try {
			System.out.println("Waiting");
			this.logger.getThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		while(!logger.threadFinished())
		{
			System.out.println("Waiting");
			try {
				Thread.sleep(500);//30
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/
		System.out.println("expe done");
	}
	
	private void logTurn(int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		logger.log(turnIndex, beeID, beeTaskName, beePhysio);
	}
	
	private void logTurn(String... ss)
	{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < ss.length; ++i)
		{
			if(i!=0)
			{
				sb.append(",");
			}
			sb.append(ss[i]);
		}
		
		logger.log(sb.toString());
	}

	private void programLoop()
	{
		int turnIndex = 0;
		int displayBar = 20;
		
		System.out.print("|");
		for(int i = 1; i < displayBar-1; ++i)
		{			
			System.out.print("-");
		}
		System.out.println("|");
		
		logTurn("turnIndex", "beeID", "TaskName", "Physio");
		while(turnIndex < ModelParameters.SIMU_LENGTH && !closed)
		{
			if(turnIndex%(int)(ModelParameters.SIMU_LENGTH/displayBar) == 0)
			{
				System.out.print("|");
			}
	
			
			
			turnIndex++;
			
			ArrayList<Agent> copy = new ArrayList<>(agentFactory.allAgents);
			Collections.shuffle(copy);
			
			for(Agent b : copy)
			{
				b.live();
				if(b.getBeeType() == AgentType.ADULT_BEE || b.getBeeType() == AgentType.BROOD_BEE)
				{
					WorkingAgent w = (WorkingAgent) b;
					logTurn(turnIndex, b.getID(), w.getTaskName(), w.getPhysio());
				}
			}
			
			for(Comb c : combs)
			{
				c.update();
			}
			
			agentFactory.allAgents.removeIf(new Predicate<Agent>() {
				@Override
				public boolean test(Agent t) {
					return !t.alive;
				}
			});

			for(StimuliManager s : sManagers)
			{
				s.updateStimuli();
			}
			
			this.hive.computeInternalTemperature(Math.cos(simuStep++ * 1.0 / 300) * 10 + 15);

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MainController.this.window.repaint();					
				}
			});
			
			//System.out.println(turnIndex);

			try {
				Thread.sleep(0);//30
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
}
