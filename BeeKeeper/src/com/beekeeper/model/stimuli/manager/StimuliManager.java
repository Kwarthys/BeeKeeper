package com.beekeeper.model.stimuli.manager;


import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.model.comb.CombUtility;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimulusFactory;

public class StimuliManager
{
	private ArrayList<StimuliTile> stimuliTiles;
	
	private Dimension gridSize;
	
	private int smID;
	public int getID() {return smID;}
	
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

		@Override
		public int getId() {
			return smID;
		}

		@Override
		public StimuliManagerServices createNewEqualAndGetServices() {
			StimuliManager s = new StimuliManager(StimuliManager.this, -1);
			return s.getServices();
		}

		@Override
		public void updateStimuli() {
			StimuliManager.this.updateStimuli();
		}

		@Override
		public HashMap<Stimulus, Double> getTotalAmounts() {
			return StimuliManager.this.getTotalAmounts();
		}
	};

	public StimuliManager(Dimension combSize, int id)
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
		
		smID = id;
	}
	
	public StimuliManager(StimuliManager stimuliManagerToCopy, int id)
	{
		stimuliTiles = new ArrayList<>();
		
		gridSize = new Dimension(stimuliManagerToCopy.gridSize);
		for(int j = 0; j < gridSize.height; j++)
		{
			for(int i = 0; i < gridSize.width; i++)
			{
				stimuliTiles.add(new StimuliTile(stimuliManagerToCopy.getTileAt(new Point(i,j))));
			}			
		}
		
		smID = id;
	}

	public HashMap<Stimulus,Double> getTotalAmounts()
	{
		HashMap<Stimulus,Double> amounts = new HashMap<>();
		
		for(StimuliTile t : stimuliTiles)
		{
			for(Stimulus smell : t.stimuliMap.keySet())
			{
				if(!amounts.containsKey(smell))
				{
					amounts.put(smell, 0.0);
				}
				amounts.put(smell, amounts.get(smell) + t.stimuliMap.getAmount(smell));
			}
		}
		
		return amounts;
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
			
			//System.out.println(smell + ": propag:" + propag + " |evap: " + evapRate);
			
			for(int i = 0; i < size; ++i)
			{
				StimuliTile st = stimuliTiles.get(i);
				double localAmount = st.stimuliMap.getAmount(smell);
				
				/*** NEW COMPUTING ***/
				if(localAmount > 0)
				{
					ArrayList<Integer> voisins = CombUtility.getCellNeighbors(i, gridSize);
					//System.out.println("LocalAmount : " + localAmount + " pushing " + localAmount * (1-propag) + " to " + voisins.size() + " cells. propag:" + propag);
					for(Integer stIndex : voisins)
					{
						stimuliTiles.get(stIndex).tmpAmount += localAmount * (1-propag);
						stimuliTiles.get(stIndex).tmpContributors += 1;
					}
				}
				
				
				
				
				/*********************/
				
				
				/*** OLD COMPUTING WAY ***				
				ArrayList<Integer> voisins = CombUtility.getCellNeighbors(i, gridSize);
				
				double totalAmount = 0;
				for(Integer stIndex : voisins)
				{
					totalAmount += stimuliTiles.get(stIndex).stimuliMap.getAmount(smell) * (1-propag) / voisins.size();
				}

				st.tmpAmount = (localAmount * propag + totalAmount) * evapRate;
				/*********************/
			}
			
			//double granTotalAmount = 0;
			for(StimuliTile st : stimuliTiles)
			{
				/*** NEW ***/
				double localAmount = st.stimuliMap.getAmount(smell);
				
				double incAmount = 0;
				if(st.tmpContributors > 0)
				{
					incAmount = st.tmpAmount/st.tmpContributors;
				}				
				
				localAmount = (localAmount * propag + incAmount) * evapRate;
				st.stimuliMap.setAmount(smell, localAmount);
				//granTotalAmount += st.tmpAmount;
				st.tmpAmount = 0;
				st.tmpContributors = 0;
				
				/***********/
				
				
				/*** OLD ***
				if(st.tmpAmount != 0)
				{
					st.stimuliMap.setAmount(smell, st.tmpAmount);
					//granTotalAmount += st.tmpAmount;
					st.tmpAmount = 0;		
				}
				***********/
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

	public void mergeWith(StimuliManagerServices stimuliManagerServices)
	{
		stimuliManagerServices.getTiles().forEach((StimuliTile st) -> {
			boolean found = false;
			for(int i = 0; i < StimuliManager.this.stimuliTiles.size() && !found; ++i)
			{
				StimuliTile ownSt = StimuliManager.this.stimuliTiles.get(i);
				if(ownSt.position.equals(st.position))
				{
					found = true;
					ownSt.stimuliMap.addAllAmounts(st.stimuliMap);
				}
			}
			
			if(!found)
			{
				StimuliManager.this.stimuliTiles.add(new StimuliTile(st));
			}
		});
	}

	public class StimuliTile
	{
		public Point position;
		public StimuliMap stimuliMap = new StimuliMap();

		double tmpAmount = 0;
		int tmpContributors = 0;
		
		public StimuliTile(int x, int y)
		{
			position = new Point(x,y);
		}
		
		public StimuliTile(StimuliTile st) {
			position = new Point(st.position);
			stimuliMap = new StimuliMap(st.stimuliMap);
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
