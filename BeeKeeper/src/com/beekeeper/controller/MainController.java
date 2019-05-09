package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.ihm.CombDrawer;
import com.beekeeper.ihm.TaskGrapher;
import com.beekeeper.model.agent.AdultBee;
import com.beekeeper.model.agent.BeeType;
import com.beekeeper.model.agent.BroodBee;
import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.utils.CustomRule;
import com.beekeeper.utils.MyUtils;

public class MainController
{
	ArrayList<CombDrawer> drawers = new ArrayList<CombDrawer>();
	
	private ArrayList<Comb> combs = new ArrayList<Comb>();
	
	private MyLogger logger = new MyLogger();

	private StimuliManager sManager;
	
	private BeeWindow window;
	
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
		ArrayList<EmptyBee> totalBees = new ArrayList<>();
		ArrayList<CombCell> totalCells = new ArrayList<>();
		
		sManager = new StimuliManager(totalBees, totalCells);	
		
		Point2D.Double center = new Point2D.Double(100,100);
		
		for(int i = 0; i < 1; ++i)
		{
			ArrayList<EmptyBee> bees = new ArrayList<>();
			ArrayList<CombCell> cells = new ArrayList<>();
			
			spawnBroodCells(200, MyUtils.getCirclePointRule(center, 50), sManager.getNewServices(), bees);
			spawnCombCells(30, MyUtils.getDonutPointRule(center, 50, 60), cells);		
			spawnWorkers(400, MyUtils.getCirclePointRule(center, 70), sManager.getNewServices(), this.controlServices, bees);
			
			Comb c = new Comb(bees, cells);
			c.setID(i);
			this.combs.add(c);
			
			CombDrawer drawer = new CombDrawer();
			drawer.setBees(bees);
			drawer.setCells(cells);
			
			this.drawers.add(drawer);
			
			totalBees.addAll(bees);
			totalCells.addAll(cells);
		}
		
		TaskGrapher g = new TaskGrapher(totalBees);

		this.window = new BeeWindow(g,drawers);

		programLoop();
	}

	private void spawnCombCells(int number, CustomRule<Point2D.Double> rule, ArrayList<CombCell> cells)
	{		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			cells.add(new CombCell(point.getX(), point.getY()));
		}
	}

	private void spawnBroodCells(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services, ArrayList<EmptyBee> bees)
	{		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			bees.add(new BroodBee(services, point.getX(), point.getY()));
		}
	}

	private void spawnWorkers(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services, MainControllerServices controllerServices, ArrayList<EmptyBee> bees)
	{		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double point = MyUtils.getPointInRule(300, 0, rule);
			bees.add(new AdultBee(services, controllerServices, point.getX(), point.getY()));
		}
	}

	private void programLoop()
	{
		while(true)
		{
			for(Comb c : combs)
			{
				c.liveAgents();
				c.liveCells();
			}

			sManager.updateStimuli();

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
