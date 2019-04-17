package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.EmptyBee;

@SuppressWarnings("serial")
public class BeeDrawer extends JPanel{
	
	private ArrayList<EmptyBee> agents = new ArrayList<>();
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		for(EmptyBee a : agents)
		{
			switch(a.getBeeType())
			{
			case ADULT_BEE:
				g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
				g.fillOval((int)a.getPosition().x-2, (int)a.getPosition().y-2, 4, 4);
				g.setColor(Color.BLACK);
				g.drawOval((int)a.getPosition().x-2, (int)a.getPosition().y-2, 4, 4);
				break;
			
			case BROOD_BEE:
				g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
				g.fillRect((int)a.getPosition().x-2, (int)a.getPosition().y-2, 4, 4);
				g.setColor(Color.BLACK);
				g.drawRect((int)a.getPosition().x-2, (int)a.getPosition().y-2, 4, 4);
				break;
			}
			
		}
		
		g.dispose();
	}
	
	public void setBees(ArrayList<EmptyBee> agents)
	{
		this.agents = agents;
	}

}
