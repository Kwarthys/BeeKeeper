package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.CombServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.model.stimuli.manager.StimuliManager.StimuliTile;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

@SuppressWarnings("serial")
public class CombDrawer extends JPanel{

	private ArrayList<Agent> agents;
	private ArrayList<CombCell> cells;

	private double zoom = 2;
	
	private int CELL_SIZE = 6;

	private Color hungryLarvaePhColor = GraphicParams.hungryLarvaePhColor;
	private Color foodPhColor = GraphicParams.foodPhColor;
	
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

		//paintPheromones(g);
		paintCells(g);
		paintActors(g);

		g.dispose();
	}

	protected void paintPheromones(Graphics g)
	{
		//double max = 0;
		for(StimuliTile st : stimuliManagerServices.getTiles())
		{
			double sA = st.stimuliMap.getAmount(Stimulus.StimulusA);
			
			//if(max < sA)max = sA;
			
			int cap = 4;
			sA = sA > cap ? cap : sA;
			int s = (int)(sA * 255 / cap);
			
			int tileX = (int) (st.position.x * zoom);
			int tileY = (int) (st.position.y * zoom);
			g.setColor(new Color(s,s,s));
			g.fillRect(tileX, tileY, (int)(StimuliManager.atomSize*zoom), (int)(StimuliManager.atomSize*zoom));
			//g.drawString(String.valueOf(s), (int)(tileX*1.5), (int)(tileY*1.5));
		}
		
		//if(max!=0)System.out.println("MAX: " + max);
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
			int offset = a.hostCell.y % 2 == 0 ? 0 : CELL_SIZE/2;

			int x = 10+a.hostCell.x * CELL_SIZE - CELL_SIZE/3 + offset;
			int y = 10+a.hostCell.y * CELL_SIZE - CELL_SIZE/3;
			
			g.fillOval((int)(x*zoom),(int)(y*zoom), (int)(CELL_SIZE*2/3*zoom), (int)(CELL_SIZE*2/3*zoom));
			System.out.println(a.hostCell.x + " " + a.hostCell.y);
		}
	}

}
