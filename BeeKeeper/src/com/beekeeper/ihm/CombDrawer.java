package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.agent.implem.AdultBee;
import com.beekeeper.model.agent.implem.BroodBee;
import com.beekeeper.model.comb.CombServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManager;
import com.beekeeper.model.stimuli.manager.StimuliManager.StimuliTile;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.parameters.ModelParameters;

@SuppressWarnings("serial")
public class CombDrawer extends JPanel{

	private ArrayList<Agent> agents;
	private ArrayList<CombCell> cells;

	private double zoom = 2;

	private Color hungryLarvaePhColor = GraphicParams.hungryLarvaePhColor;
	private Color foodPhColor = GraphicParams.foodPhColor;
	
	//private CombServices hostServices;
	
	private StimuliManagerServices stimuliManagerServices;
	
	public CombDrawer(CombServices c, StimuliManagerServices stimuliManagerServices)
	{
		this.setPreferredSize(new Dimension(400,400));
		this.setMinimumSize(new Dimension(350,350));
		
		//hostServices = c;

		this.agents = c.getBees();
		this.cells = c.getCells();
		
		this.stimuliManagerServices = stimuliManagerServices;
	}


	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(GraphicParams.BACKGROUND);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		paintPheromones(g);
		paintActors(g);
		//paintCells(g);

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
		int cellSize = 6;

		g.setColor(Color.WHITE);
		
		boolean isOffset = true;
		double offset;
		
		for(CombCell c : cells)
		{
			if(c.x == 0)
				isOffset = !isOffset;
			
			//System.out.println(c.x + " " + c.y + " " + c.filled);
			
			offset = isOffset ? cellSize/2 : 0;
			g.drawOval((int)((c.x*cellSize-cellSize/2+offset)*zoom), (int)((c.y*cellSize-cellSize/2)*zoom), (int)(cellSize*zoom), (int)(cellSize*zoom));
			
			if(c.filled)
			{
				g.setColor(GraphicParams.hungryLarvaePhColor);
				g.fillOval((int)((c.x*cellSize-cellSize/3+offset)*zoom), (int)((c.y*cellSize-cellSize/3)*zoom), (int)(cellSize*2/3*zoom), (int)(cellSize*2/3*zoom));

				g.setColor(Color.WHITE);
			}
		}
		
		
	}

	protected void paintActors(Graphics g)
	{
		for(Agent a : agents)
		{
			int x = (int)a.getPosition().x;
			int y = (int)a.getPosition().y;
			
			double r = a.getRotation();

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
				double rotX = -Math.cos(r);
				double rotY = -Math.sin(r);
				int headSize = 8;
				int thoraxSize = 4;
				int abdomenSize = 10;
				int thx = x + (int)((headSize/2 + thoraxSize/2)/2*rotX);
				int thy = y + (int)((headSize/2 + thoraxSize/2)/2*rotY);
				int abx = thx + (int)((abdomenSize/2 + thoraxSize/2)/2*rotX);
				int aby = thy + (int)((abdomenSize/2 + thoraxSize/2)/2*rotY);
				
				WorkingAgent wa = (WorkingAgent) a;
				HashMap<String, Double> ts = wa.getAllPrintableThresholds();

				String taskA = "Task StimulusA";
				String taskB = "Task StimulusB";
				String taskC = "Task StimulusC";

				int red = (int)(ts.containsKey(taskA) ? ModelParameters.getNormalisedThreshold(ts.get(taskA))*255 : 0);
				int green = (int)(ts.containsKey(taskB) ? ModelParameters.getNormalisedThreshold(ts.get(taskB))*255 : 0);
				int blue = (int)(ts.containsKey(taskC) ? ModelParameters.getNormalisedThreshold(ts.get(taskC))*255 : 0);
				

				g.setColor(new Color(255, 255-(int)(a.getEnergy()*255), 255-(int)(a.getEnergy()*255)));
				g.fillOval((int)(zoom*x-headSize/2), (int)(zoom*y-headSize/2), headSize, headSize);
				g.setColor(new Color(255-red, 255-green, 255-blue));
				g.fillOval((int)(zoom*abx-abdomenSize/2), (int)(zoom*aby-abdomenSize/2), abdomenSize, abdomenSize);
				g.setColor(Color.WHITE);
				g.fillOval((int)(zoom*thx-thoraxSize/2), (int)(zoom*thy-thoraxSize/2), thoraxSize, thoraxSize);

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

}
