package com.beekeeper.ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

import javax.swing.JPanel;

import com.beekeeper.ihm.model.EmployementData;
import com.beekeeper.model.agent.BeeType;
import com.beekeeper.model.agent.EmptyBee;

@SuppressWarnings("serial")
public class TaskGrapher extends JPanel{
	
	private ArrayList<EmptyBee> bees;

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

	public TaskGrapher(ArrayList<EmptyBee> bees)
	{
		this.bees = bees;
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
		
		g2d.setStroke(defaultStroke);
	}
	
	/**
	 * 
	 * @param bees list to compute from
	 * @return a double [0;1], 1 means evevry larvae has 100% hunger
	 */
	private double getTotalBroodHunger(ArrayList<EmptyBee> bees)
	{
		double total = 0;
		double max = 0; //double for operation purposes
		for(EmptyBee b : bees)
		{
			if(b.getBeeType() == BeeType.BROOD_BEE)
			{
				total += (1-b.getEnergy());
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
		colors.add(Color.GREEN);
		colors.add(new Color(150,150,255));		
		
		return colors;
	}
	
}
