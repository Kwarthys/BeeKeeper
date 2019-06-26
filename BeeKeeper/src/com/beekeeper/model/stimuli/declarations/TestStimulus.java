package com.beekeeper.model.stimuli.declarations;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.Stimulus;

public class TestStimulus extends AStimulus
{	
	public TestStimulus(double d)
	{
		super(d);
	}
	
	public void setType(Stimulus type)
	{		
		this.type = type;
	}
}