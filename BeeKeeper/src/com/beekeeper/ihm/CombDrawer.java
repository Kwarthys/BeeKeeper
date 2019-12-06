package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.CombServices;
import com.beekeeper.model.comb.cell.CellContent;
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

	public Stimulus drawnStimulus = Stimulus.Ocimene;

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

		this.agents = new ArrayList<>(cs.getBees());
		this.cells = new ArrayList<>(cs.getCells());

		paintPheromones(g);
		paintCells(g);
		paintActors(g);

		g.dispose();
	}

	protected void paintPheromones(Graphics g)
	{
		for(StimuliTile st : stimuliManagerServices.getTiles())
		{
			double sA = st.stimuliMap.getAmount(drawnStimulus);

			if(drawnStimulus == Stimulus.Ocimene)
			{
				CombCell cell = cs.getCellAt(st.position.x, st.position.y);

				if(cell.visiting != null)
				{
					WorkingAgent b = (WorkingAgent)cell.visiting;
					sA += b.getBodySmells().getAmount(Stimulus.Ocimene);
				}

				if(cell.inside != null)
				{
					WorkingAgent b = (WorkingAgent)cell.inside;
					sA += b.getBodySmells().getAmount(Stimulus.Ocimene);
				}
			}



			Point p = fromLinearToHex(st.position);

			int cap = 4;
			int s = capColor(sA, cap);

			p.x -= CELL_SIZE/2;
			p.y -= CELL_SIZE/2; 

			g.setColor(new Color(s,s,s));
			g.fillOval((int)(p.x*zoom), (int)(p.y*zoom), 2*CELL_SIZE,2*CELL_SIZE);
			//g.drawString(String.valueOf(s), (int)(tileX*1.5), (int)(tileY*1.5));

			p.y *= 3;
			p.y += 300;
			p.x *= 3;

			g.setColor(Color.WHITE);
			g.drawString(String.valueOf((int)(10*s)), p.x, p.y);
		}
	}

	private int capColor(double value, int cap)
	{
		value = value > cap ? cap : value;
		return (int)(value * 255 / cap);
	}

	protected void paintCells(Graphics g)
	{		
		boolean isOffset = true;
		double offset;

		for(CombCell c : cells)
		{
			if(c.x == 0)
				isOffset = !isOffset;

			//System.out.println(c.x + " " + c.y + " " + c.filled);

			g.setColor(Color.GRAY);

			offset = isOffset ? CELL_SIZE/2 : 0;
			g.drawOval((int)((10+c.x*CELL_SIZE-CELL_SIZE/2+offset)*zoom), (int)((10+c.y*CELL_SIZE-CELL_SIZE/2)*zoom), (int)(CELL_SIZE*zoom), (int)(CELL_SIZE*zoom));

			if(c.content == CellContent.food)
			{
				g.setColor(GraphicParams.hungryLarvaePhColor);

			}
			else if(c.content == CellContent.brood)
			{
				int e = (int)(c.inside.getEnergy()*255);
				g.setColor(new Color(255-e,255-e,255));

			}

			if(c.content != CellContent.empty)
				g.fillOval((int)((10+c.x*CELL_SIZE-CELL_SIZE/3+offset)*zoom), (int)((10+c.y*CELL_SIZE-CELL_SIZE/3)*zoom), (int)(CELL_SIZE*2/3*zoom), (int)(CELL_SIZE*2/3*zoom));
		}


	}

	protected void paintActors(Graphics g)
	{
		g.setColor(Color.RED);
		for(Agent a : agents)
		{
			Point p = a.getPosition();
			if(p!=null)
			{

				p = fromLinearToHex(p);

				int x = p.x;
				int y = p.y;

				int e = (int)(a.getEnergy() * 255);
				g.setColor(new Color(255,255-e,255-e));
				g.fillOval((int)((x-CELL_SIZE/3)*zoom),(int)((y-CELL_SIZE/3)*zoom), (int)(CELL_SIZE*2/3*zoom), (int)(CELL_SIZE*2/3*zoom));
			}
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
