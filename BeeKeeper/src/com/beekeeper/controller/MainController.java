package com.beekeeper.controller;

import java.util.ArrayList;

import com.beekeeper.ihm.BeeDrawer;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.model.agent.AdultBee;

public class MainController
{
	ArrayList<AdultBee> bees = new ArrayList<>();
	BeeWindow w;
	private BeeDrawer drawer;
	
	public MainController()
	{		
		for(int i = 0; i < 500; i++)
		{
			bees.add(new AdultBee());
		}
		
		this.drawer = new BeeDrawer();
		this.w = new BeeWindow(drawer);
		
		programLoop();
	}
	
	private void programLoop()
	{
		while(true)
		{
			for(AdultBee bee : bees)
			{
				bee.live();
			}

			this.drawer.setBees(bees);
			this.w.repaint();
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
