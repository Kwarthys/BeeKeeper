package com.beekeeper.model.stimuli.manager;


import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.utils.MyUtils;

public class StimuliManager
{	
	private int atomSize = 10;
	
	private ArrayList<StimuliTile> stimuliTiles = new ArrayList<>();
	
	public void smellEmit(Stimulus s, double amount, Point2D.Double position)
	{
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
		//TODO
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
		};
	}

	private StimuliTile AddNewTileAt(Double position) {
		StimuliTile st = new StimuliTile();
		st.position = position;

		st.position.x -= st.position.x % atomSize;
		st.position.y -= st.position.y % atomSize;
		
		return st;
	}
	
	protected StimuliTile getTileAt(Point2D.Double pos)
	{		
		for(StimuliTile st : stimuliTiles)
		{
			if(MyUtils.distance(pos, st.position) <= atomSize && pos.x >= st.position.x && pos.y >= st.position.y)
			{
				return st;
			}
		}
		
		return null;
	}
	
	public class StimuliTile
	{
		Point2D.Double position;
		StimuliMap stimuliMap = new StimuliMap();
	}
}
