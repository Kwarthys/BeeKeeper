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
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.hive.BeeHive;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.model.tasks.Task;
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
	
	private MainControllerServices controlServices = new MainControllerServices() {

		@Override
		public void logMyTaskSwitch(Task newTask, int beeID) {
			MainController.this.logger.logTask(beeID, newTask.taskName);				
		}

		@Override
		public double getHiveTemperature(){
			return hive.getTemperature();
		}
	};

	public MainController()
	{
		this.agentFactory = new AgentFactory();	
		
		Dimension combSize = new Dimension(20,20);
		
		Point2D.Double center = new Point2D.Double(combSize.width/2,combSize.height/2);
		
		this.hive = new BeeHive();
		
		for(int i = 0; i < 1; ++i)
		{
			//ArrayList<Agent> bees = new ArrayList<>();
			Comb c = new Comb(combSize);
			
			StimuliManager sm = new StimuliManager(c);
			
			sManagers.add(sm);
			
			//bees.addAll(agentFactory.spawnBroodCells(200, MyUtils.getCirclePointRule(center, 50), sm.getNewServices(), this.controlServices));
			//bees.addAll(agentFactory.spawnFoodAgent(30, MyUtils.getDonutPointRule(center, 50, 60), sm.getNewServices()));		
			//bees.addAll(agentFactory.spawnWorkers(20, MyUtils.getCirclePointRule(center, 70), sm.getServices(), this.controlServices));
	

			
			//bees.addAll(agentFactory.spawnTestEmitterAgent(30, MyUtils.getCirclePointRule(center, 50), sm.getNewServices()));
			//bees.addAll(agentFactory.spawnTestAgents(5, MyUtils.getCirclePointRule(center, 100), sm.getNewServices(), this.controlServices));	
			//bees.addAll(agentFactory.spawnTestEmitterAgent(30, c,MyUtils.getCirclePointRule(center, 50), sm.getNewServices()));
			agentFactory.spawnTestAgents(3, c,MyUtils.getCirclePointRule(center, combSize.width/2), sm.getServices(), this.controlServices);
			
			c.setID(i);
			this.combs.add(c);			
			
			CombDrawer drawer = new CombDrawer(c.getServices(), sm.getServices());
			
			this.drawers.add(drawer);
		}
		
		TaskGrapher g = new TaskGrapher(agentFactory.allAgents);

		this.window = new BeeWindow(g,drawers);

		programLoop();
	}

	private void programLoop()
	{
		int turnIndex = 0;
		while(turnIndex < 1000)
		{
			turnIndex++;
			//Collections.shuffle(agentFactory.allAgents);
			
			ArrayList<Agent> copy = new ArrayList<>(agentFactory.allAgents);
			Collections.shuffle(copy);
			
			for(Agent b : copy)
			{
				b.live();
			}
			
			for(Comb c : combs)
			{
				c.removeTheDead();
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

			try {
				Thread.sleep(200);//30
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
