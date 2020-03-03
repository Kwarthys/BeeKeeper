package com.beekeeper.model.stimuli.manager;


import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import com.beekeeper.model.comb.CombUtility;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimulusFactory;

public class StimuliManager
{
	private ArrayList<StimuliTile> stimuliTiles;
	
	private Dimension gridSize;
	
	private StimuliManagerServices services = new StimuliManagerServices() {

		@Override
		public StimuliMap getAllStimuliAround(Point position) {
			return StimuliManager.this.getAllStimuliAround(position);
		}

		@Override
		public void emit(Stimulus s, double amount, Point position) {
			smellEmit(s, amount, position);
		}

		@Override
		public ArrayList<StimuliTile> getTiles() {
			return new ArrayList<StimuliTile>(stimuliTiles);
		}
	};

	public StimuliManager(Dimension combSize)
	{
		stimuliTiles = new ArrayList<>();
		
		gridSize = new Dimension(combSize);
		for(int j = 0; j < gridSize.height; j++)
		{
			for(int i = 0; i < gridSize.width; i++)
			{
				stimuliTiles.add(new StimuliTile(i,j));
			}			
		}
	}

	public void smellEmit(Stimulus s, double amount, Point position)
	{
		getTileAt(position).stimuliMap.addAmount(s, amount);
	}

	public StimuliMap getAllStimuliAround(Point position)
	{
		if(getTileAt(position) != null)
		{
			return new StimuliMap(getTileAt(position).stimuliMap);
		}

		return new StimuliMap();
	}

	public void updateStimuli()
	{		
		int size = gridSize.width * gridSize.height;

		for(Stimulus smell : Stimulus.values())
		{
			double propag = StimulusFactory.getPropag(smell);
			double evapRate = StimulusFactory.getEvapRate(smell);
			
			for(int i = 0; i < size; ++i)
			{
				StimuliTile st = stimuliTiles.get(i);
				double localAmount = st.stimuliMap.getAmount(smell);
				
				ArrayList<Integer> voisins = CombUtility.getCellNeighbors(i, gridSize);
				
				double totalAmount = 0;
				for(Integer stIndex : voisins)
				{
					totalAmount += stimuliTiles.get(stIndex).stimuliMap.getAmount(smell) * (1-propag) / voisins.size();
				}

				st.tmpAmount = (localAmount * propag + totalAmount) * evapRate;
			}
			
			//double granTotalAmount = 0;
			for(StimuliTile st : stimuliTiles)
			{
				if(st.tmpAmount != 0)
				{
					st.stimuliMap.setAmount(smell, st.tmpAmount);
					//granTotalAmount += st.tmpAmount;
					st.tmpAmount = 0;					
				}

			}
			//if(smell == Stimulus.StimulusA)
				//System.out.println("GranTotalAmount = " + granTotalAmount);
		}
		
		//printAllTheTiles();
	}

	protected StimuliTile getTileAt(Point pos)
	{
		return stimuliTiles.get(pos.y * gridSize.width + pos.x);
	}

	public StimuliManagerServices getServices()
	{
		return services;
	}	

	protected void printAllTheTiles()
	{
		System.out.println("\n\nTiles :");
		for(StimuliTile st : stimuliTiles)
		{
			System.out.println(st);
		}
		System.out.println("---------");
	}

	public class StimuliTile
	{
		public Point position;
		public StimuliMap stimuliMap = new StimuliMap();

		double tmpAmount = 0;
		
		public StimuliTile(int x, int y)
		{
			position = new Point(x,y);
		}
		
		@Override
		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			
			sb.append(position.x);
			sb.append("|");
			sb.append(position.y);
			sb.append("\n");
			sb.append(stimuliMap.getDisplayString());	
			
			return sb.toString();
		}
		
		public boolean isEmpty()
		{
			return stimuliMap.isEmpty();
		}
	}
}
