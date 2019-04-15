package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.AdultBee;

@SuppressWarnings("serial")
public class BeeDrawer extends JPanel{
	
	private ArrayList<AdultBee> agents = new ArrayList<>();
	
	
	@Override
	protected void paintComponent(Graphics g) {
		for(AdultBee a : agents)
		{
			g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
			g.fillOval((int)a.getPosition().x-2, (int)a.getPosition().y-2, 4, 4);
			g.setColor(Color.BLACK);
			g.drawOval((int)a.getPosition().x-2, (int)a.getPosition().y-2, 4, 4);
		}
	}
	
	public void setBees(ArrayList<AdultBee> agents)
	{
		this.agents = agents;
	}

}
