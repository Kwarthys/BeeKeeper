package com.beekeeper.model.stimuli.manager;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.stimuli.external.ExternalStimuliLoad;

public class StimuliManager
{
	private ArrayList<EmptyBee> agents;
	
	public StimuliManager(ArrayList<EmptyBee> agents)
	{
		this.agents = agents;
	}

	public ExternalStimuliLoad getAllStimuliAround(Point2D.Double position)
	{
		//TODO StimuliManager getAllStimuliAround
		return new ExternalStimuliLoad(null);
	}
	
	public void updateStimuli()
	{
		for(EmptyBee bee : agents)
		{
			ExternalStimuliLoad load = bee.getExternalStimuli();
			load.evaporate();
		}
		
		/*
		for(AStimulus s : stimuli)
		{
			s.evaporate();
		}
		for(int i = 0; i < stimuli.size(); ++i)
		{
			if(stimuli.get(i).getAmount() == 0)
			{
				stimuli.remove(i);
				--i;
			}
		}
		*/
	}

	public StimuliManagerServices getNewServices()
	{
		return new StimuliManagerServices() {

			@Override
			public ExternalStimuliLoad getAllStimuliAround(Double position) {
				return StimuliManager.this.getAllStimuliAround(position);
			}
		};
	}
}
