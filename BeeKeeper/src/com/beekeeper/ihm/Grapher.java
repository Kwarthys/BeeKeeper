package com.beekeeper.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.beekeeper.ihm.model.EmployementData;
import com.beekeeper.model.agent.EmptyBee;

@SuppressWarnings("serial")
public class Grapher extends JPanel{
	
	private ArrayList<EmptyBee> bees;
	
	private ArrayList<EmployementData> jobData = new ArrayList<>();
	
	private int borderMargin = 50;
	private int graphHeight = 200;
	private int graphWidth = 500;

	public Grapher(ArrayList<EmptyBee> bees)
	{
		this.bees = bees;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(GraphicParams.BACKGROUND);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		jobData.add(EmployementData.getDataFromList(bees));
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(borderMargin, borderMargin, borderMargin, borderMargin+graphHeight); // LEFT BAR
		g.drawLine(borderMargin, borderMargin+graphHeight, borderMargin+graphWidth, borderMargin+graphHeight); // BOT BAR
		
		for(int i = 1; i < jobData.size(); i++)
		{
			EmployementData d = jobData.get(i);
			EmployementData lastd = jobData.get(i-1);
			for(Entry<String, Integer> set : d.data.entrySet())
			{
				System.out.println(set.getKey() + " - " + set.getValue());
				g.drawLine(borderMargin + 5*(i-1), borderMargin+graphHeight-lastd.get(set.getKey()), borderMargin + 5*i, borderMargin+graphHeight-set.getValue());
			}
		}
		
		
		while(jobData.size() > graphWidth/5.1)
		{
			jobData.remove(0);
		}
		
	}
	
}
