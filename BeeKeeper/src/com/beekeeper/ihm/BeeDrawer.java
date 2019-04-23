package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.BeeType;
import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.stimuli.Stimulus;

@SuppressWarnings("serial")
public class BeeDrawer extends JPanel{

	private ArrayList<EmptyBee> agents = new ArrayList<>();


	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		for(EmptyBee a : agents)
		{
			int x = (int)a.getPosition().x;
			int y = (int)a.getPosition().y;
			
			if(a.getBeeType() == BeeType.BROOD_BEE)
			{
				int phs = (int)(a.getExternalStimuli().getPheromoneAmount(Stimulus.HungryLarvae) * 5);
				g.setColor(Color.GREEN);
				g.fillOval(x-phs/2, y-phs/2, phs, phs);
			}
		}

		for(EmptyBee a : agents)
		{
			int x = (int)a.getPosition().x;
			int y = (int)a.getPosition().y;

			switch(a.getBeeType())
			{
			case ADULT_BEE:
				g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
				g.fillOval(x-2, y-2, 4, 4);
				g.setColor(Color.BLACK);
				g.drawOval(x-2, y-2, 4, 4);
				break;

			case BROOD_BEE:
				g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
				g.fillRect(x-2, y-2, 4, 4);
				g.setColor(Color.BLACK);
				g.drawRect(x-2, y-2, 4, 4);
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
