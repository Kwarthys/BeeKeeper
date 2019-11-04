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

<<<<<<< HEAD
		this.agents = cs.getBees();
		this.cells = cs.getCells();

		//paintPheromones(g);
		paintCells(g);
		//paintActors(g);
=======
		//paintPheromones(g);
		paintCells(g);
		paintActors(g);
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper

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
		int cellSize = (int)ModelParameters.COMBCELL_SIZE;

		g.setColor(Color.GRAY);
		
		boolean isOffset = true;
		double offset;
		
		for(CombCell c : cells)
		{
			if(c.x == 0)
				isOffset = !isOffset;
			
			//System.out.println(c.x + " " + c.y + " " + c.filled);
			
			offset = isOffset ? cellSize/2 : 0;
<<<<<<< HEAD
			g.drawOval((int)((10+c.x*cellSize-cellSize/2+offset)*zoom), (int)((10+c.y*cellSize-cellSize/2)*zoom), (int)(cellSize*zoom), (int)(cellSize*zoom));
=======
			g.drawOval((int)((c.x*cellSize+offset)*zoom), (int)((c.y*cellSize)*zoom), (int)(cellSize*zoom), (int)(cellSize*zoom));
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper
			
			if(c.filled)
			{
				g.setColor(GraphicParams.hungryLarvaePhColor);
<<<<<<< HEAD
				g.fillOval((int)((10+c.x*cellSize-cellSize/3+offset)*zoom), (int)((10+c.y*cellSize-cellSize/3)*zoom), (int)(cellSize*2/3*zoom), (int)(cellSize*2/3*zoom));
=======
				g.fillOval((int)((c.x*cellSize+offset)*zoom), (int)((c.y*cellSize)*zoom), (int)(cellSize*zoom), (int)(cellSize*zoom));
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper

				g.setColor(Color.GRAY);
			}
		}
		
		
	}

	protected void paintActors(Graphics g)
	{
		for(Agent a : agents)
		{
			
		}
	}

}
