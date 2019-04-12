package com.beekeeper.ihm;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.Agent;

@SuppressWarnings("serial")
public class BeeDrawer extends JPanel{
	
	private ArrayList<Agent> agents = new ArrayList<>();
	
	
	@Override
	protected void paintComponent(Graphics g) {
		for(Agent a : agents)
		{
			g.drawOval((int)a.getPosition().x-2, (int)a.getPosition().y-2, 4, 4);
		}
	}
	
	public void setBees(ArrayList<Agent> agents)
	{
		this.agents = agents;
	}

}
