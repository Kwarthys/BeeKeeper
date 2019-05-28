package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.Stimulus;

@SuppressWarnings("serial")
public class CombDrawer extends JPanel{

	private ArrayList<EmptyBee> agents = new ArrayList<>();
	private ArrayList<CombCell> cells = new ArrayList<>();

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
		for(CombCell c : cells)
		{
			int x = (int)c.getPosition().x;
			int y = (int)c.getPosition().y;

			int phs = (int)(c.getExternalStimuli().getPheromoneAmount(Stimulus.FoodSmell));
			g.setColor(foodPhColor);
			g.fillOval((int)(x*zoom-phs/2), (int)(y*zoom-phs/2), phs, phs);

		}

		for(EmptyBee a : agents)
		{
			//boolean fill = false;
			
			int x = (int)a.getPosition().x;
			int y = (int)a.getPosition().y;

			int phs = (int)(a.getStimuliLoad().getPheromoneAmount(Stimulus.HungryLarvae) * 15);
			g.setColor(hungryLarvaePhColor);
			g.drawOval((int)(x*zoom-phs/2), (int)(y*zoom-phs/2), phs, phs);

		}
	}

	protected void paintActors(Graphics g)
	{
		for(CombCell c : cells)
		{
			int x = (int)c.getPosition().x;
			int y = (int)c.getPosition().y;

			g.setColor(Color.WHITE);
			g.drawRect((int)(zoom*x-4), (int)(zoom*y-4), 8, 8);
		}

		for(EmptyBee a : agents)
		{
			int x = (int)a.getPosition().x;
			int y = (int)a.getPosition().y;

			switch(a.getBeeType())
			{
			case ADULT_BEE:
				g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
				g.fillOval((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				g.setColor(Color.WHITE);
				g.drawOval((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				/** DEBUG **/
				if(a.target != null)
				{
					g.setColor(Color.GRAY);
					g.drawLine((int)(x*zoom), (int)(y*zoom), (int)(a.target.x*zoom), (int)(a.target.y*zoom));
				}
				/***********/
				break;

			case BROOD_BEE:
				g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
				g.fillRect((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				g.setColor(Color.WHITE);
				g.drawRect((int)(zoom*x-2), (int)(zoom*y-2), 4, 4);
				break;
			}
		}
	}

	public void setBees(ArrayList<EmptyBee> agents)
	{
		this.agents = agents;
	}

	public void setCells(ArrayList<CombCell> cells)
	{
		this.cells = cells;
	}

}
