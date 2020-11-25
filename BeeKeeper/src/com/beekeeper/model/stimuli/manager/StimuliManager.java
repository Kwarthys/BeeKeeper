package com.beekeeper.model.stimuli.manager;


import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimulusFactory;

public class StimuliManager
{
	//private ArrayList<StimuliTile> stimuliTiles;

	private Dimension gridSize;
	
	private HashMap<Stimulus, double[]> map;

	private int smID;
	public int getID() {return smID;}

	//private int updateIndex = 0;

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
		public int getId() {
			return smID;
		}

		@Override
		public StimuliManagerServices createNewEqualAndGetServices() {
			StimuliManager s = new StimuliManager(StimuliManager.this, gridSize,-1);
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

		@Override
		public double[] getAmountsFor(Stimulus smell) {
			if(map.containsKey(smell))
			{
				return map.get(smell);
			}			
			return null;
		}

		@Override
		public Dimension getSize() {
			return new Dimension(gridSize);
		}
	};

	public StimuliManager(Dimension combSize, int id)
	{		
		map = new HashMap<>();
		smID = id;
		gridSize = new Dimension(combSize);
	}

	public StimuliManager(StimuliManager stimuliManagerToCopy, Dimension combSize, int id)
	{		
		smID = id;

		gridSize = new Dimension(combSize);
		
		map = new HashMap<>();
		
		for(Stimulus smell : stimuliManagerToCopy.map.keySet())
		{
			double amounts[] = new double[gridSize.width * gridSize.height];
			
			for(int i = 0; i < gridSize.width * gridSize.height; ++i)
			{
				amounts[i] = stimuliManagerToCopy.map.get(smell)[i];
			}
			
			map.put(smell, amounts);
		}
		
	}

	public HashMap<Stimulus,Double> getTotalAmounts()
	{
		HashMap<Stimulus,Double> amounts = new HashMap<>();

		for(Stimulus smell : map.keySet())
		{
			double total = 0;
			
			for(int i = 0; i < gridSize.height * gridSize.width; ++i)
			{
				total += map.get(smell)[i];
			}	
			
			amounts.put(smell, total);
		}		
		
		return amounts;
	}

	public void smellEmit(Stimulus s, double amount, Point position)
	{		
		if(!map.containsKey(s))
		{
			map.put(s, new double[gridSize.height * gridSize.width]);
		}
		
		map.get(s)[indexFromPos(position)] += amount;
	}

	public StimuliMap getAllStimuliAround(Point position)
	{
		StimuliMap s = new StimuliMap();		
		
		for(Stimulus smell : map.keySet())
		{
			s.addAmount(smell, map.get(smell)[indexFromPos(position)]);
		}		
		
		return s;
	}

	public void updateStimuli()
	{
		int size = gridSize.width * gridSize.height;
		

		for(Stimulus smell : map.keySet())
		{
			double propag = StimulusFactory.getPropag(smell);
			double evap = StimulusFactory.getEvapRate(smell);
			
			double[] amounts = map.get(smell);
			double[] doubleBuffer = new double[size];
			
			for(int i = 0; i < size; ++i)
			{
				int x = i%gridSize.width; 
				int y = i / gridSize.width;
				
				double voisins = 0;
				int nbVoisins = 0;
				
				if(x>0)
				{
					voisins+=amounts[i-1];
					++nbVoisins;
				}
				if(x<gridSize.width-1)
				{
					voisins+=amounts[i+1];
					++nbVoisins;
				}
				
				int offset = y%2 == 0 ? 0:1;
				
				if(y>0)
				{
					voisins+=amounts[i - gridSize.width + offset - 1];
					++nbVoisins;
					voisins+=amounts[i - gridSize.width + offset];
					++nbVoisins;
				}
				
				if(y<gridSize.height-1)
				{
					voisins+=amounts[i + gridSize.width + offset - 1];
					++nbVoisins;
					voisins+=amounts[i + gridSize.width + offset];
					++nbVoisins;
				}				
				
				doubleBuffer[i] = (amounts[i] * propag + (1-propag) * voisins / nbVoisins) * evap;
			}
			
			map.put(smell, doubleBuffer);
		}
	}
	
	public StimuliManagerServices getServices()
	{
		return services;
	}
	
	public void mergeWith(StimuliManagerServices stimuliManagerServices)
	{
		for(Stimulus smell : Stimulus.values())
		{
			double[] otherAmounts = stimuliManagerServices.getAmountsFor(smell);
			if(otherAmounts != null)
			{
				if(!map.containsKey(smell))
				{
					map.put(smell, new double[gridSize.width * gridSize.height]);
				}
				
				double[] amounts = map.get(smell);
				
				for(int i = 0; i < gridSize.width * gridSize.height; ++i)
				{
					amounts[i] += otherAmounts[i];
				}
			}
		}
	}
	
	private int indexFromPos(Point pos)
	{
		return pos.y * gridSize.width + pos.x;
	}
}
