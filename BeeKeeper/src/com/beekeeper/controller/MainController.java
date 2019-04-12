package com.beekeeper.controller;

import java.util.ArrayList;

import com.beekeeper.ihm.BeeDrawer;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.model.agent.AdultBee;
import com.beekeeper.model.agent.Agent;

public class MainController
{
	ArrayList<Agent> bees = new ArrayList<>();
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
			for(Agent bee : bees)
			{
				bee.live();
			}

			System.out.println("aaaaaaaaa");
			this.drawer.setBees(bees);
			this.w.repaint();
		}
	}
}
