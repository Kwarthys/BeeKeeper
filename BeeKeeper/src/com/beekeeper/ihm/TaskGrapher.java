package com.beekeeper.ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.beekeeper.ihm.model.EmployementData;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

@SuppressWarnings("serial")
public class TaskGrapher extends JPanel{

	private ArrayList<Agent> bees;

	private ArrayList<EmployementData> jobData = new ArrayList<>();
	private ArrayList<Double> hungerHistory = new ArrayList<>();

	private HashMap<String, Color> colorMap = new HashMap<>();
	private int colorIndex = 0;

	private static ArrayList<Color> colors = buildColorArray();

	private Color hungerColor = GraphicParams.hungryLarvaePhColor;

	private int borderMargin = 50;
	private int graphHeight = 300;
	private int graphWidth = 500;

	private int step = 2;

	public TaskGrapher(ArrayList<Agent> allAgents)
	{
		//this.bees = new ArrayList<EmptyBee>(bees);
		this.bees = allAgents;
		Dimension size = new Dimension((int)(borderMargin * 2 + graphWidth * 1.2), (int)(borderMargin*2 + graphHeight*1.6));
		this.setPreferredSize(size);
		this.setMinimumSize(size);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;

		Stroke defaultStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(3));

		g2d.setColor(GraphicParams.BACKGROUND);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

		jobData.add(EmployementData.getDataFromList(bees));		
		hungerHistory.add(getTotalBroodHunger(bees));

		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(borderMargin, borderMargin, borderMargin, borderMargin+graphHeight); // LEFT BAR
		g2d.drawLine(borderMargin, borderMargin+graphHeight, borderMargin+graphWidth, borderMargin+graphHeight); // BOT BAR		

		int nbBees = bees.size();

		for(int i = 1; i < jobData.size(); i++)
		{
			EmployementData d = jobData.get(i);
			EmployementData lastd = jobData.get(i-1);

			g2d.setColor(hungerColor);
			int barHeight = (int) (graphHeight*hungerHistory.get(i));
			g2d.fillRect(borderMargin+step*i-step/4, borderMargin+graphHeight-barHeight, step/2, barHeight);

			for(Entry<String, Integer> set : d.data.entrySet())
			{
				g2d.setColor(getColorFor(set.getKey()));
				//System.out.println(set.getKey() + " - " + set.getValue());
				g2d.drawLine(borderMargin + step*(i-1), borderMargin+graphHeight-(int)(lastd.get(set.getKey()) * 1.0 / nbBees * graphHeight), borderMargin + step*i, borderMargin+graphHeight-(int)(set.getValue() * 1.0 / nbBees * graphHeight));
			}
		}

		int offset = 0;

		g2d.setColor(hungerColor);
		g2d.drawString(String.format("%.3f",(hungerHistory.get(hungerHistory.size()-1))) + " - TotalHunger", borderMargin + 30, borderMargin*2 + graphHeight + 30*offset++);

		for(Entry<String, Color> set : colorMap.entrySet())
		{
			g2d.setColor(set.getValue());
			g2d.drawString(jobData.get(jobData.size()-1).get(set.getKey()) + " - " + set.getKey(), borderMargin + 30, borderMargin*2 + graphHeight + 30*offset++);
		}


		while(jobData.size() > graphWidth/step)
		{
			jobData.remove(0);
		}
		while(hungerHistory.size() > graphWidth/step)
		{
			hungerHistory.remove(0);
		}

		drawTaskSpeGraph(g2d);

		g2d.setStroke(defaultStroke);
	}

	private void drawTaskSpeGraph(Graphics2D g)
	{
		int graphStartX = borderMargin + graphWidth / 2;
		int graphStartY = 2*borderMargin + graphHeight;

		int baseLineY = graphStartY + graphHeight/2;

		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(graphStartX, graphStartY, graphStartX, baseLineY); //Left Bar
		g.drawLine(graphStartX, baseLineY, graphStartX + graphWidth/2, baseLineY); //Bot Bar

		ArrayList<WorkingAgent> wlist = new ArrayList<WorkingAgent>();


		for(int i = 0; i < bees.size(); ++i)
		{
			if(bees.get(i).getBeeType() == AgentType.ADULT_BEE || bees.get(i).getBeeType() == AgentType.TEST_AGENT)
			{
				wlist.add((WorkingAgent)bees.get(i));
			}
		}

		for(int i = 0; i < wlist.size(); ++i)
		{
			WorkingAgent b = wlist.get(i);
			HashMap<String, Double> allTs = b.getAllPrintableThresholds();

			g.setColor(GraphicParams.hungryLarvaePhColor);

			int amount = 0;
			StimuliMap map = b.getPercievedStimuli();
			if(map != null)
			{
				amount = (int) map.getAmount(Stimulus.StimulusA);
				//amount += (int) map.getAmount(Stimulus.StimulusB);
				//amount += (int) map.getAmount(Stimulus.StimulusC);
			}

			g.fillRect((int) (graphStartX + i * graphWidth / 2.0 / wlist.size()), baseLineY, 1, amount);

			for(Entry<String, Double> set : allTs.entrySet())
			{
				g.setColor(getColorFor(set.getKey()));
				g.fillOval((int) (graphStartX + i * graphWidth / 2.0 / wlist.size()), (int) (baseLineY - set.getValue() * graphHeight / 2.0 / ModelParameters.MAX_TASK_THRESHOLD), 5, 5);				
			}

		}
	}

	/**
	 * 
	 * @param bees list to compute from
	 * @return a double [0;1], 1 means every larvae has 100% hunger
	 */
	private double getTotalBroodHunger(ArrayList<Agent> bees)
	{
		double total = 0;
		double max = 0; //double for operation purposes
		for(Agent b : bees)
		{
			if(b.getBeeType() == AgentType.BROOD_BEE || b.getBeeType() == AgentType.TEST_EMITTERAGENT)
			{
				total += (1-((EmitterAgent) b).getEnergy());
				max += 1;
			}
		}
		return total/max;
	}

	private Color getColorFor(String key)
	{
		if(!colorMap.containsKey(key))
		{
			colorMap.put(key, colors.get(colorIndex%colors.size()));
			++colorIndex;
		}

		return colorMap.get(key);
	}

	private static ArrayList<Color> buildColorArray()
	{
		ArrayList<Color> colors = new ArrayList<Color>();

		colors.add(Color.WHITE);
		colors.add(Color.LIGHT_GRAY);
		colors.add(Color.YELLOW);
		colors.add(new Color(150,150,255));		
		colors.add(Color.GREEN);

		return colors;
	}

}