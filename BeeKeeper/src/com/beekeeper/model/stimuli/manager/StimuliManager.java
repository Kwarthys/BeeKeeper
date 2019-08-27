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
		System.out.println("Emitting " + amount + " of " + s);

		StimuliTile toEdit = getTileAt(position);

		if(toEdit == null)
		{
			toEdit = getNewTileAT(position);			
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
		//System.out.println("tiles list size = " + stimuliTiles.size());
		//TODO SPREAD
		//System.out.println("Updating smells");
		
		int listSizeAtStart = stimuliTiles.size();

		for(Stimulus smell : Stimulus.values())
		{
			ArrayList<StimuliTile> newTiles = new ArrayList<>();

			for(int i = 0; i < listSizeAtStart; ++i)
			{
				//System.out.println("tiles list size = " + stimuliTiles.size() + " now at " + i);
				StimuliTile st = stimuliTiles.get(i);
				//System.out.println("Working on " + st.position.x + "|" + st.position.y);
				
				boolean initiator = st.stimuliMap.getAmount(smell) != 0;
				
				Point2D.Double neighborPoint = new Point2D.Double(st.position.x, st.position.y);
				double totalAmount = 0;

				neighborPoint.x += atomSize;			//RIGHT
				//System.out.println("1");
				totalAmount += manageASpread(neighborPoint, smell, newTiles, initiator);


				neighborPoint.x -= 2*atomSize;			//LEFT
				//System.out.println("2");
				totalAmount += manageASpread(neighborPoint, smell, newTiles, initiator);

				neighborPoint.x += atomSize;			//BOT
				neighborPoint.y += atomSize;
				//System.out.println("3");
				totalAmount += manageASpread(neighborPoint, smell, newTiles, initiator);

				neighborPoint.y -= 2*atomSize;			//TOP
				//System.out.println("4");
				totalAmount += manageASpread(neighborPoint, smell, newTiles, initiator);

				st.tmpAmount = (totalAmount + st.stimuliMap.getAmount(smell)) / 5;
			}
			
			for(int i = 0; i < listSizeAtStart; ++i)
			{
				StimuliTile st = stimuliTiles.get(i);
				if(st.tmpAmount != 0)
				{
					//System.out.println("Setting Amount of " + smell + " to " + st.tmpAmount);
					st.stimuliMap.setAmount(smell, st.tmpAmount);

					st.tmpAmount = 0;					
				}

			}

			stimuliTiles.addAll(newTiles);
		}

		//System.err.println("smellTurnEnds");
		//printAllTheTiles();

		//EVAPORATE
		for(StimuliTile st : stimuliTiles)
		{
			st.stimuliMap.evaporate();
		}
	}

	/*
	 * Can create new StimuliTiles, as a smell spreads to new tiles. Iterator is required to append those new tiles.
	 */
	private double manageASpread(Point2D.Double pos, Stimulus smell, ArrayList<StimuliTile> newTiles, boolean initiator)
	{
		StimuliTile st = getTileAt(pos);
		
		if(st == null)
		{
			if(initiator)
			{
				st = getNewTileAT(pos);
				addIfNotInside(st, newTiles);				
			}
			else
			{
				return 0;
			}
		}

		return st.stimuliMap.getAmount(smell);
	}


	private void addIfNotInside(StimuliTile stToAdd, ArrayList<StimuliTile> newTiles)
	{
		for(StimuliTile st : newTiles)
		{
			if(st.position.equals(stToAdd.position))
			{
				//System.err.println("Removing " + st.position.x + "|" + st.position.y);
				return;
			}
		}
		
		newTiles.add(stToAdd);
		
	}

	private StimuliTile getNewTileAT(Double position) {
		StimuliTile st = new StimuliTile();
		
		double x = position.x - position.x % atomSize;
		double y = position.y - position.y % atomSize;
		
		st.position = new Point2D.Double(x,y);

		//System.out.println("creating " + st.position.x + "|" + st.position.y);

		return st;
	}

	protected StimuliTile getTileAt(Point2D.Double pos)
	{
		for(int i = 0; i < stimuliTiles.size(); ++i)
		{
			StimuliTile st = stimuliTiles.get(i);
			
			if(MyUtils.distance(pos, st.position) < atomSize && pos.x >= st.position.x && pos.y >= st.position.y)
			{
				//System.out.println(pos.x + "|" + pos.y + " found inside " + st.position.x + "|" + st.position.y);
				return st;
			}
			//System.out.println(pos.x + "|" + pos.y + " not inside " + st.position.x + "|" + st.position.y);
		}

		return null;
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
				return new ArrayList<StimuliTile>(stimuliTiles);
			}
		};
	}	

	private void printAllTheTiles()
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
		public Point2D.Double position;
		public StimuliMap stimuliMap = new StimuliMap();

		double tmpAmount = 0;
		
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
	}
}
