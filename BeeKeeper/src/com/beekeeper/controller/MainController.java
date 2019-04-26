package com.beekeeper.controller;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.beekeeper.ihm.BeeDrawer;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.model.agent.AdultBee;
import com.beekeeper.model.agent.BeeType;
import com.beekeeper.model.agent.BroodBee;
import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.utils.CustomRule;

public class MainController
{
	private ArrayList<EmptyBee> bees = new ArrayList<>();
	private ArrayList<CombCell> cells = new ArrayList<>();
	private BeeDrawer drawer;

	private StimuliManager sManager;

	public MainController()
	{
		MainControllerServices controlServices = new MainControllerServices() {			
			@Override
			public BroodBee getLarvaeByPos(Point2D.Double larvaePos) {
				for(EmptyBee bee : bees)
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
			public CombCell getCellByPos(Double targetPos) {
				for(CombCell cell : cells)
				{
					if(targetPos.equals(cell.getPosition()))
					{
						return cell;
					}					
				}
				return null;
			}
		};

		sManager = new StimuliManager(bees, cells);	
		
		Point2D.Double center = new Point2D.Double(250,250);

		spawnBroodCells(100, getDonutPointRule(center, 0, 50), sManager.getNewServices());

		spawnCombCells(10, getDonutPointRule(center, 50, 60));

		for(int i = 0; i < 400; i++)
		{
			bees.add(new AdultBee(sManager.getNewServices(), controlServices));
		}

		this.drawer = new BeeDrawer();		
		this.drawer.setBees(bees);
		this.drawer.setCells(cells);

		new BeeWindow(drawer);

		programLoop();
	}

	private void spawnCombCells(int number, CustomRule<Point2D.Double> rule)
	{		
		for(int i = 0; i < number; i++)
		{
			Point2D.Double pointCandidate = new Point2D.Double(100+Math.random()*300, 100+Math.random()*300);
			while(!rule.isValid(pointCandidate))
			{
				pointCandidate = new Point2D.Double(100+Math.random()*300, 100+Math.random()*300);
			}
			cells.add(new CombCell(pointCandidate.getX(), pointCandidate.getY()));
		}
	}

	private void spawnBroodCells(int number, CustomRule<Point2D.Double> rule, StimuliManagerServices services)
	{		
		for(int i = 0; i < 30; i++)
		{
			Point2D.Double pointCandidate = new Point2D.Double(100+Math.random()*300, 100+Math.random()*300);
			while(!rule.isValid(pointCandidate))
			{
				pointCandidate = new Point2D.Double(100+Math.random()*300, 100+Math.random()*300);
			}
			bees.add(new BroodBee(services, pointCandidate.getX(), pointCandidate.getY()));
		}
	}

	private CustomRule<Point2D.Double> getDonutPointRule(Point2D.Double center, double innerRadius, double outterRadius)
	{
		return new CustomRule<Point2D.Double>() {			
			@Override
			public boolean isValid(Point2D.Double t) {
				double distance = t.distance(center);
				return distance > innerRadius && distance < outterRadius;
			}
		};
	}

	private void programLoop()
	{
		while(true)
		{
			for(EmptyBee bee : bees)
			{
				bee.live();
			}
			for(CombCell cell : cells)
			{
				cell.live();
			}

			sManager.updateStimuli();

			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					MainController.this.drawer.repaint();					
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
