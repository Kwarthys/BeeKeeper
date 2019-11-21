package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.CombServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManager.StimuliTile;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

@SuppressWarnings("serial")
public class CombDrawer extends JPanel{

	private ArrayList<Agent> agents;
	private ArrayList<CombCell> cells;

	private double zoom = 2;
	
	private int CELL_SIZE = 6;

	//private Color hungryLarvaePhColor = GraphicParams.hungryLarvaePhColor;
	//private Color foodPhColor = GraphicParams.foodPhColor;
	
	//private CombServices hostServices;
	
	private StimuliManagerServices stimuliManagerServices;
	
	private CombServices cs;
	
	public CombDrawer(CombServices c, StimuliManagerServices stimuliManagerServices)
	{
		this.setPreferredSize(new Dimension(400,400));
		this.setMinimumSize(new Dimension(350,350));
		
		this.cs = c;
		
		this.stimuliManagerServices = stimuliManagerServices;
	}


	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(GraphicParams.BACKGROUND);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		this.agents = cs.getBees();
		this.cells = cs.getCells();

		paintPheromones(g);
		paintCells(g);
		paintActors(g);

		g.dispose();
	}

	protected void paintPheromones(Graphics g)
	{
		for(StimuliTile st : stimuliManagerServices.getTiles())
		{
			double sA = st.stimuliMap.getAmount(Stimulus.StimulusA);
			double sFood = st.stimuliMap.getAmount(Stimulus.AskFood);
			
			Point p = fromLinearToHex(st.position);
			
			int cap = 4;
			int s = capColor(sA, cap);
			int f = capColor(sFood, cap);

			p.x -= CELL_SIZE/2;
			p.y -= CELL_SIZE/2; 
			
			g.setColor(new Color(s,0,f));
			g.fillOval((int)(p.x*zoom), (int)(p.y*zoom), 2*CELL_SIZE,2*CELL_SIZE);
			//g.drawString(String.valueOf(s), (int)(tileX*1.5), (int)(tileY*1.5));
			
			p.y *= 3;
			p.y += 300;
			p.x *= 3;
			
			g.setColor(Color.WHITE);
			g.drawString(String.valueOf((int)(10*sFood)), p.x, p.y);
		}
	}
	
	private int capColor(double value, int cap)
	{
		value = value > cap ? cap : value;
		return (int)(value * 255 / cap);
	}
	
	protected void paintCells(Graphics g)
	{
		g.setColor(Color.WHITE);
		
		boolean isOffset = true;
		double offset;
		
		for(CombCell c : cells)
		{
			if(c.x == 0)
				isOffset = !isOffset;
			
			//System.out.println(c.x + " " + c.y + " " + c.filled);
			
			offset = isOffset ? CELL_SIZE/2 : 0;
			g.drawOval((int)((10+c.x*CELL_SIZE-CELL_SIZE/2+offset)*zoom), (int)((10+c.y*CELL_SIZE-CELL_SIZE/2)*zoom), (int)(CELL_SIZE*zoom), (int)(CELL_SIZE*zoom));
			
			if(c.filled)
			{
				g.setColor(GraphicParams.hungryLarvaePhColor);
				g.fillOval((int)((10+c.x*CELL_SIZE-CELL_SIZE/3+offset)*zoom), (int)((10+c.y*CELL_SIZE-CELL_SIZE/3)*zoom), (int)(CELL_SIZE*2/3*zoom), (int)(CELL_SIZE*2/3*zoom));

				g.setColor(Color.WHITE);
			}
		}
		
		
	}

	protected void paintActors(Graphics g)
	{
		g.setColor(Color.RED);
		for(Agent a : agents)
		{			
			Point p = fromLinearToHex(a.getPosition());

			int x = p.x - CELL_SIZE/3;
			int y = p.y - CELL_SIZE/3;
			
			int e = (int)(a.getEnergy() * 255);
			g.setColor(new Color(255,255-e,255-e));
			
			g.fillOval((int)(x*zoom),(int)(y*zoom), (int)(CELL_SIZE*2/3*zoom), (int)(CELL_SIZE*2/3*zoom));
			//System.out.println(a.hostCell.x + " " + a.hostCell.y);
		}
	}
	
	
	protected Point fromLinearToHex(Point p)
	{
		int offset = p.y % 2 == 0 ? 0 : CELL_SIZE/2;

		int x = 10+p.x * CELL_SIZE + offset;
		int y = 10+p.y * CELL_SIZE;

		return new Point(x,y);
	}

}
