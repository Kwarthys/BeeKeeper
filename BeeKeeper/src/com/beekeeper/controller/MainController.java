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
import com.beekeeper.model.agent.BeeType;
import com.beekeeper.model.agent.BroodBee;
import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.cell.CombCell;
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
	
	private MainControllerServices controlServices = new MainControllerServices() {			
		@Override
		public BroodBee getLarvaeByPos(Point2D.Double larvaePos, int combID) {
			for(EmptyBee bee : combs.get(combID).getAgents())
			{
				if(bee.getBeeType() == BeeType.BROOD_BEE)
				{
					if(larvaePos.equals(bee.getPosition()))
					{
						return (BroodBee) bee;
					}
				}
			}
			return null;
		}

		@Override
		public CombCell getCellByPos(Double targetPos, int combID) {
			for(CombCell cell : combs.get(combID).getCells())
			{
				if(targetPos.equals(cell.getPosition()))
				{
					return cell;
				}					
			}
			return null;
		}

		@Override
		public void logMyTaskSwitch(Task newTask, int beeID) {
			MainController.this.logger.logTask(beeID, newTask.taskName);				
		}
	};

	public MainController()
	{
		this.agentFactory = new AgentFactory();
		
		ArrayList<CombCell> totalCells = new ArrayList<>();		
		
		Point2D.Double center = new Point2D.Double(100,100);
		
		for(int i = 0; i < 2; ++i)
		{
			ArrayList<EmptyBee> bees = new ArrayList<>();
			ArrayList<CombCell> cells = new ArrayList<>();
			
			StimuliManager sm = new StimuliManager(bees, cells);
			
			sManagers.add(sm);
			
			bees.addAll(agentFactory.spawnBroodCells(200, MyUtils.getCirclePointRule(center, 50), sm.getNewServices()));
			cells.addAll(agentFactory.spawnCombCells(30, MyUtils.getDonutPointRule(center, 50, 60)));		
			bees.addAll(agentFactory.spawnWorkers(500, MyUtils.getCirclePointRule(center, 70), sm.getNewServices(), this.controlServices));
			
			Comb c = new Comb(bees, cells);
			c.setID(i);
			this.combs.add(c);
			
			CombDrawer drawer = new CombDrawer();
			drawer.setBees(bees);
			drawer.setCells(cells);
			
			this.drawers.add(drawer);
			
			totalCells.addAll(cells);
		}
		
		TaskGrapher g = new TaskGrapher(agentFactory.allAgents);

		this.window = new BeeWindow(g,drawers);

		programLoop();
	}

	private void programLoop()
	{
		while(true)
		{
			//Collections.shuffle(agentFactory.allAgents);
			
			ArrayList<EmptyBee> copy = new ArrayList<>(agentFactory.allAgents);
			Collections.shuffle(copy);
			
			for(EmptyBee b : copy)
			{
				b.live();
			}
			
			for(CombCell c : agentFactory.allCells)
			{
				c.live();
			}
			
			for(Comb c : combs)
			{
				c.removeTheDead();
			}
			
			agentFactory.allAgents.removeIf(new Predicate<EmptyBee>() {
				@Override
				public boolean test(EmptyBee t) {
					return !t.alive;
				}
			});

			for(StimuliManager s : sManagers)
			{
				s.updateStimuli();
			}

			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					MainController.this.window.repaint();					
				}
			});

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
