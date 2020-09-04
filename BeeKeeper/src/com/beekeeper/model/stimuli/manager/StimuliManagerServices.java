package com.beekeeper.model.stimuli.manager;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;

public interface StimuliManagerServices
{
	public StimuliMap getAllStimuliAround(Point position);
	
	public void emit(Stimulus s, double amount, Point position);
	
	//public ArrayList<StimuliTile> getTiles();
	
	public double[] getAmountsFor(Stimulus smell);
	
	public int getId();
	
	public StimuliManagerServices createNewEqualAndGetServices();
	
	public void updateStimuli();

	public HashMap<Stimulus, Double> getTotalAmounts();
	
	public Dimension getSize();
}
