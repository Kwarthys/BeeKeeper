package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.implem.BroodBee;
import com.beekeeper.model.agent.implem.FoodSource;
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
		public BroodBee getLarvaeByPos(Point2D.Double larvaePos, int combID) {
			return (BroodBee)getAgentByPos(larvaePos, AgentType.BROOD_BEE, combID);
		}

		@Override
		public void logMyTaskSwitch(Task newTask, int beeID) {
			MainController.this.logger.logTask(beeID, newTask.taskName);				
		}

		@Override
		public FoodSource getFoodSourceByPos(Double pos, int combID) {
			return (FoodSource)getAgentByPos(pos, AgentType.FOOD_SOURCE, combID);
		}

		@Override
		public double getHiveTemperature(){
			return hive.getTemperature();
		}

		@Override
		public EmitterAgent getAgentByTypeNPos(AgentType type, Double pos, int combID) {
			return (EmitterAgent)getAgentByPos(pos, type, combID);
		}

		@Override
		public boolean isCellTaken(Double cellPos, int combID) {
			combs.get(combID).isCellTaken(cellPos);
			return false;
		}
	};

	public MainController()
	{
		this.agentFactory = new AgentFactory();	
		
		Point2D.Double center = new Point2D.Double(100,100);
		
		this.hive = new BeeHive();
		
		for(int i = 0; i < 1; ++i)
		{
			ArrayList<Agent> bees = new ArrayList<>();
			
			StimuliManager sm = new StimuliManager();
			
			sManagers.add(sm);
			
			//bees.addAll(agentFactory.spawnBroodCells(200, MyUtils.getCirclePointRule(center, 50), sm.getNewServices(), this.controlServices));
			//bees.addAll(agentFactory.spawnFoodAgent(30, MyUtils.getDonutPointRule(center, 50, 60), sm.getNewServices()));		
			bees.addAll(agentFactory.spawnWorkers(20, MyUtils.getCirclePointRule(center, 70), sm.getNewServices(), this.controlServices));
			
			
			//bees.addAll(agentFactory.spawnTestEmitterAgent(30, MyUtils.getCirclePointRule(center, 50), sm.getNewServices()));
			//bees.addAll(agentFactory.spawnTestAgents(5, MyUtils.getCirclePointRule(center, 100), sm.getNewServices(), this.controlServices));
			
			Comb c = new Comb(bees);
			c.setID(i);
			this.combs.add(c);			
			
			CombDrawer drawer = new CombDrawer(c.getServices(), sm.getNewServices());
			
			this.drawers.add(drawer);
		}
		
		TaskGrapher g = new TaskGrapher(agentFactory.allAgents);

		this.window = new BeeWindow(g,drawers);

		programLoop();
	}
	
	private Agent getAgentByPos(Point2D.Double pos, AgentType type, int combID)
	{
		for(Agent bee : combs.get(combID).getAgents())
		{
			if(bee.getBeeType() == type)
			{
				if(pos.equals(bee.getPosition()))
				{
					return bee;
				}
			}
		}
		return null;
	}

	private void programLoop()
	{
		int turnIndex = 0;
		while(turnIndex < 1)
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
				Thread.sleep(30);//30
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
