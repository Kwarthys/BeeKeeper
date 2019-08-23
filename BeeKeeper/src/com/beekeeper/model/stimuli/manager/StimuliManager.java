package com.beekeeper.model.stimuli.manager;


import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.utils.MyUtils;

public class StimuliManager
{	
	public static final int atomSize = 10;
	
	private ArrayList<StimuliTile> stimuliTiles = new ArrayList<>();
	
	public void smellEmit(Stimulus s, double amount, Point2D.Double position)
	{
		System.out.println("Emitting " + s);
		
		StimuliTile toEdit = getTileAt(position);
		
		if(toEdit == null)
		{
			toEdit = AddNewTileAt(position);
			stimuliTiles.add(toEdit);
		}
		
		toEdit.stimuliMap.addAmount(s, amount);
	}

	public StimuliMap getAllStimuliAround(Point2D.Double position)
	{
		if(getTileAt(position) != null)
		{
			return getTileAt(position).stimuliMap;
		}
		
		return new StimuliMap();
	}
	
	public void updateStimuli()
	{
		//TODO SPREAD
		for(Stimulus smell : Stimulus.values())
		{
			for(StimuliTile st : stimuliTiles)
			{
				if(st.stimuliMap.getAmount(smell) != 0)
				{
					Point2D.Double next = new Point2D.Double(st.position.x, st.position.y);
					StimuliTile currentTile;
					double totalAmount = 0;

					next.x += atomSize;			//RIGHT
					currentTile = getTileAt(next, true);
					totalAmount += currentTile.stimuliMap.getAmount(smell);
					
					
					next.x -= 2*atomSize;		//LEFT
					currentTile = getTileAt(next, true);
					totalAmount += currentTile.stimuliMap.getAmount(smell);
					
					next.x += atomSize;			//BOT
					next.y += atomSize;
					currentTile = getTileAt(next, true);
					totalAmount += currentTile.stimuliMap.getAmount(smell);
					
					next.y -= 2*atomSize;		//TOP
					currentTile = getTileAt(next, true);
					totalAmount += currentTile.stimuliMap.getAmount(smell);
					
					st.tmpAmount = (totalAmount + st.stimuliMap.getAmount(smell)) / 5;
				}
			}
			
			for(StimuliTile st : stimuliTiles)
			{
				System.out.println("Setting Amount of " + smell);
				st.stimuliMap.setAmount(smell, st.tmpAmount);
				
				st.tmpAmount = 0;
			}
		}
		
		//EVAPORATE
		for(StimuliTile st : stimuliTiles)
		{
			st.stimuliMap.evaporate();
		}
	}

	private StimuliTile AddNewTileAt(Double position) {
		StimuliTile st = new StimuliTile();
		st.position = position;

		st.position.x -= st.position.x % atomSize;
		st.position.y -= st.position.y % atomSize;
		
		return st;
	}
	
	protected StimuliTile getTileAt(Point2D.Double pos, boolean createIfNull)
	{		
		for(StimuliTile st : stimuliTiles)
		{
			if(MyUtils.distance(pos, st.position) <= atomSize && pos.x >= st.position.x && pos.y >= st.position.y)
			{
				return st;
			}
		}
		
		if(createIfNull)
		{
			return AddNewTileAt(pos);
		}
		
		return null;
	}
	
	protected StimuliTile getTileAt(Point2D.Double pos)
	{		
		return getTileAt(pos, false);
	}

	public StimuliManagerServices getNewServices()
	{
		return new StimuliManagerServices() {

			@Override
			public StimuliMap getAllStimuliAround(Point2D.Double position) {
				return StimuliManager.this.getAllStimuliAround(position);
			}

			@Override
			public void emit(Stimulus s, double amount, Point2D.Double position) {
				smellEmit(s, amount, position);
			}

			@Override
			public ArrayList<StimuliTile> getTiles() {
				return stimuliTiles;
			}
		};
	}
	
	public class StimuliTile
	{
		public Point2D.Double position;
		public StimuliMap stimuliMap = new StimuliMap();
		
		double tmpAmount = 0;
	}
}
