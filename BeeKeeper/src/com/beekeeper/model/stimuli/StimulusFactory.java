package com.beekeeper.model.stimuli;

import java.util.HashMap;

import com.beekeeper.model.stimuli.declarations.FoodSmellStimulus;
import com.beekeeper.model.stimuli.declarations.HungryLarvaeStimulus;
import com.beekeeper.model.stimuli.declarations.TestStimulus;

public class StimulusFactory
{
	private static HashMap<Stimulus, AStimulus> database = fillDataBase();
	
	private static HashMap<Stimulus, AStimulus> fillDataBase()
	{
		HashMap<Stimulus, AStimulus> database = new HashMap<Stimulus, AStimulus>();
		
		for(Stimulus smell : Stimulus.values())
		{
			database.put(smell, get(smell, 0));
		}
		
		return database;
	}
	
	public static AStimulus get(Stimulus key, double amount)
	{
		AStimulus s = null;
		TestStimulus t = null;
		
		switch(key)
		{
		case Dance:
			System.err.println("Stimulus Factory - " + key + " Not implemented");
			break;
		case Energy:
			System.err.println("Stimulus Factory - " + key + " Not implemented");
			break;
		case FoodSmell:
			s = new FoodSmellStimulus(amount);
			break;
		case HungerBee:
			System.err.println("Stimulus Factory - " + key + " Not implemented");
			break;
		case HungryLarvae:
			s = new HungryLarvaeStimulus(amount);
			break;
		case StimulusA:
			t = new TestStimulus(amount);
			t.setType(Stimulus.StimulusA);
			s = t;
			break;
		case StimulusB:
			t = new TestStimulus(amount);
			t.setType(Stimulus.StimulusB);
			s = t;
			break;
		case StimulusC:
			t = new TestStimulus(amount);
			t.setType(Stimulus.StimulusC);
			s = t;
			break;
		default:
			System.err.println("Stimulus Factory - default shouldn't happen");
			break;
		
		}
		
		return s;
	}
	
	public static double getEvapRate(Stimulus smell)
	{
		if(checkDataBaseWith(smell))
		{
			return database.get(smell).getDecay();			
		}
		else
		{
			return 0;
		}
	}
	
	public static double getPropag(Stimulus smell)
	{
		if(checkDataBaseWith(smell))
		{
			return database.get(smell).transmissibility;			
		}
		else
		{
			return 0;
		}
	}
	
	private static boolean checkDataBaseWith(Stimulus smell)
	{
		if(database.get(smell) == null)
		{
			//System.err.println(smell + " Not Implemented");
			return false;
		}
		
		return true;
	}
	
	
}
