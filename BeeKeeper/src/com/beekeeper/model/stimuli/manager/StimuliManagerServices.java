package com.beekeeper.model.stimuli.manager;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManager.StimuliTile;

public interface StimuliManagerServices
{
	public StimuliMap getAllStimuliAround(Point2D.Double position);
	
	public void emit(Stimulus s, double amount, Point2D.Double position);
	
	public ArrayList<StimuliTile> getTiles();
}
