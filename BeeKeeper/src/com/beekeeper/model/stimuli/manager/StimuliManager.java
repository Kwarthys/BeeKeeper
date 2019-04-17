package com.beekeeper.model.stimuli.manager;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.external.ExternalStimuliLoad;

public class StimuliManager
{


	public ExternalStimuliLoad getAllStimuliAround(Point2D.Double position)
	{
		//TODO StimuliManager getAllStimuliAround
		return null;
	}

	public void emitStimulus(AStimulus s)
	{
		// TODO StimuliManager emitStimulus(AStimulus s)
	}

	public StimuliManagerServices getNewServices()
	{
		return new StimuliManagerServices() {

			@Override
			public ExternalStimuliLoad getAllStimuliAround(Double position) {
				return StimuliManager.this.getAllStimuliAround(position);
			}

			@Override
			public void emitStimulus(AStimulus s) {
				StimuliManager.this.emitStimulus(s);

			}
		};
	}
}
