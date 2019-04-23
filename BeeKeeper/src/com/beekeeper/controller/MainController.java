package com.beekeeper.controller;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.beekeeper.ihm.BeeDrawer;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.model.agent.AdultBee;
import com.beekeeper.model.agent.BroodBee;
import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.stimuli.manager.StimuliManager;

public class MainController
{
	private ArrayList<EmptyBee> bees = new ArrayList<>();
	private BeeDrawer drawer;
	
	private StimuliManager sManager;
	
	public MainController()
	{
		sManager = new StimuliManager(bees);	
		
		for(int i = 0; i < 1; i++)
		{
			bees.add(new BroodBee(sManager.getNewServices()));
		}
		
		for(int i = 0; i < 1; i++)
		{
			bees.add(new AdultBee(sManager.getNewServices()));
		}
		
		this.drawer = new BeeDrawer();
		MainController.this.drawer.setBees(bees);
		
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
