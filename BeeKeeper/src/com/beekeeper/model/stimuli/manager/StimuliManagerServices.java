package com.beekeeper.model.stimuli.manager;

import java.awt.Point;
import java.util.ArrayList;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManager.StimuliTile;

public interface StimuliManagerServices
{
	public StimuliMap getAllStimuliAround(Point position);
	
	public void emit(Stimulus s, double amount, Point position);
	
	public ArrayList<StimuliTile> getTiles();
	
	public int getId();
	
	public StimuliManagerServices createNewEqualAndGetServices();
}
