package com.beekeeper.model.stimuli;

import com.beekeeper.model.stimuli.declarations.FoodSmellStimulus;
import com.beekeeper.model.stimuli.declarations.HungryLarvaeStimulus;

public class StimulusFactory
{
	public static AStimulus get(Stimulus key, double amount)
	{
		AStimulus s = null;
		
		switch(key)
		{
		case Dance:
			break;
		case Energy:
			break;
		case FoodSmell:
			s = new FoodSmellStimulus(amount);
			break;
		case HungerBee:
			break;
		case HungryLarvae:
			s = new HungryLarvaeStimulus(amount);
			break;
		default:
			break;
		
		}
		
		return s;
	}
}
