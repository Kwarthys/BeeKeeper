package com.beekeeper.model.stimuli.manager;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.AStimulus;
import com.beekeeper.model.stimuli.external.ExternalStimuliLoad;

public interface StimuliManagerServices
{
	public ExternalStimuliLoad getAllStimuliAround(Point2D.Double position);
}
