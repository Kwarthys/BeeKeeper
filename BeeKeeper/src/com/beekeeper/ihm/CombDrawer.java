package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.implem.AdultBee;
import com.beekeeper.model.agent.implem.BroodBee;
import com.beekeeper.model.stimuli.Stimulus;

@SuppressWarnings("serial")
public class CombDrawer extends JPanel{

	private ArrayList<Agent> agents = new ArrayList<>();

	private double zoom = 2;

	private Color hungryLarvaePhColor = GraphicParams.hungryLarvaePhColor;
	private Color foodPhColor = GraphicParams.foodPhColor;
	
	public CombDrawer()
	{
		this.setPreferredSize(new Dimension(400,400));
		this.setMinimumSize(new Dimension(350,350));
	}


	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(GraphicParams.BACKGROUND);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		paintPheromones(g);
		paintActors(g);

		g.dispose();
	}

	protected void paintPheromones(Graphics g)
	{
		for(Agent a : agents)
		{
			//boolean fill = false;
			
			EmitterAgent ea = (EmitterAgent) a;
			
			int x = (int)a.getPosition().x;
			int y = (int)a.getPosition().y;
			
			for(Stimulus s : Stimulus.values())
			{
				int phs = (int)(ea.getStimuliLoad().getPheromoneAmount(s) * 5);
				
				if(s == Stimulus.HungryLarvae)
				{
					//phs *= 15;
					g.setColor(hungryLarvaePhColor);
					g.drawOval((int)(x*zoom-phs/2), (int)(y*zoom-phs/2), phs, phs);
				}
				else if(s == Stimulus.FoodSmell)
				{
					g.setColor(foodPhColor);
					g.fillOval((int)(x*zoom-phs/2), (int)(y*zoom-phs/2), phs, phs);
				}
				else if(s == Stimulus.StimulusA)
				{
					g.setColor(Color.red);
					g.fillOval((int)(x*zoom-phs/2), (int)(y*zoom-phs/2), phs, phs);
				}
				else if(s == Stimulus.StimulusB)
				{
					g.setColor(Color.green);
					g.fillOval((int)(x*zoom-phs/2), (int)(y*zoom-phs/2), phs, phs);
				}
				else if(s == Stimulus.StimulusC)
				{
					g.setColor(Color.blue);
					g.fillOval((int)(x*zoom-phs/2), (int)(y*zoom-phs/2), phs, phs);
				}
			}	

		}
	}

	protected void paintActors(Graphics g)
	{
		for(Agent a : agents)
		{
			int x = (int)a.getPosition().x;
			int y = (int)a.getPosition().y;

			switch(a.getBeeType())
			{
			case ADULT_BEE:
				AdultBee b = (AdultBee) a;
				g.setColor(new Color(255, 255-(int)(b.getEnergy()*255), 255-(int)(b.getEnergy()*255)));
				g.fillOval((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				g.setColor(Color.WHITE);
				g.drawOval((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				/** DEBUG **/
				if(b.target != null)
				{
					g.setColor(Color.GRAY);
					g.drawLine((int)(x*zoom), (int)(y*zoom), (int)(b.target.x*zoom), (int)(b.target.y*zoom));
				}
				/***********/
				break;

			case BROOD_BEE:
				BroodBee bb = (BroodBee) a;
				g.setColor(new Color(255, 255-(int)(bb.getEnergy()*255), 255-(int)(bb.getEnergy()*255)));
				g.fillRect((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				g.setColor(Color.WHITE);
				g.drawRect((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				break;
			case FOOD_SOURCE:
				g.setColor(Color.WHITE);
				g.drawRect((int)(zoom*x-4), (int)(zoom*y-4), 8, 8);
				break;
			case TEST_AGENT:
				g.setColor(new Color(255, 255, 255));
				g.fillOval((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				g.setColor(Color.WHITE);
				g.drawOval((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				break;
			case TEST_EMITTERAGENT:
				g.setColor(Color.WHITE);
				g.drawRect((int)(zoom*x-4), (int)(zoom*y-4), 8, 8);
				break;
			default:
				break;
			}
		}
	}

	public void setBees(ArrayList<Agent> bees)
	{
		this.agents = bees;
	}

}
