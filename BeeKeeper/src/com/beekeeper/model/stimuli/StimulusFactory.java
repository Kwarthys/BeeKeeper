package com.beekeeper.model.stimuli;

import java.util.HashMap;

import com.beekeeper.model.stimuli.declarations.AskFoodStimulus;
import com.beekeeper.model.stimuli.declarations.FoodSmellStimulus;
import com.beekeeper.model.stimuli.declarations.HungryLarvaeStimulus;
import com.beekeeper.model.stimuli.declarations.Ocimene;
import com.beekeeper.model.stimuli.declarations.TestStimulus;
import com.beekeeper.parameters.ModelParameters;

public class StimulusFactory
{
	private static HashMap<Stimulus, AStimulus> database = fillDataBase();
	
	private static HashMap<Stimulus, AStimulus> fillDataBase()
	{
		HashMap<Stimulus, AStimulus> database = new HashMap<Stimulus, AStimulus>();
		
		for(Stimulus smell : Stimulus.values())
		{
			database.put(smell, get(smell));
		}
		
		return database;
	}
	
	public static AStimulus get(Stimulus key)
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
			s = new FoodSmellStimulus();
			break;
		case HungerBee:
			System.err.println("Stimulus Factory - " + key + " Not implemented");
			break;
		case HungryLarvae:
			s = new HungryLarvaeStimulus();
			break;
		case StimulusA:
			t = new TestStimulus();
			t.setType(Stimulus.StimulusA);
			s = t;
			break;
		case StimulusB:
			t = new TestStimulus();
			t.setType(Stimulus.StimulusB);
			s = t;
			break;
		case StimulusC:
			t = new TestStimulus();
			t.setType(Stimulus.StimulusC);
			s = t;
			break;
		case AskFood:
			s = new AskFoodStimulus();
			break;
		case Ocimene:
			s = new Ocimene();
			break;
		default:
			System.err.println("Stimulus Factory - default instead of " + key + ". Shouldn't happen.");
			break;
		
		}
		
		return s;
	}
	
	/**
	 * lambda = e^(-ln(2) / halflife * Param)
	 * @param smell to evaporate
	 * @return the coefficient to apply to the substance to match the half life evaporation time.
	 */
	public static double getEvapRate(Stimulus smell)
	{
		if(checkDataBaseWith(smell))
		{
			return Math.exp(-Math.log(2)/database.get(smell).getHalfLife() * ModelParameters.secondToTimeStepCoef);	
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
			return  Math.exp(-Math.log(2)/database.get(smell).getTransmissibility() * ModelParameters.secondToTimeStepCoef);				
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
