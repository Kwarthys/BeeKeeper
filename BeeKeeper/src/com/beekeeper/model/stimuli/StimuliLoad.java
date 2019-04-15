package com.beekeeper.model.stimuli;

import com.beekeeper.model.stimuli.external.ExternalStimuliLoad;

public class StimuliLoad
{
	public ExternalStimuliLoad phs;
	public double beeEnergy;
	
	public StimuliLoad(ExternalStimuliLoad exts, double beeEnergy)
	{
		this.phs = exts;
		this.beeEnergy = beeEnergy;
	}
}
