package com.beekeeper.model.stimuli;

import com.beekeeper.model.stimuli.declarations.FoodSmellStimulus;
import com.beekeeper.model.stimuli.declarations.HungryLarvaeStimulus;
import com.beekeeper.model.stimuli.declarations.TestStimulus;

public class StimulusFactory
{
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
}
