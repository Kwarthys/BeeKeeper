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
		
		for(int i = 0; i < 30; i++)
		{
			bees.add(new BroodBee(sManager.getNewServices()));
		}
		
		for(int i = 0; i < 60; i++)
		{
			cells.add(new CombCell());
		}
		
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
