package com.beekeeper.model.stimuli.manager;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.StimuliMap;

public interface StimuliManagerServices
{
	public StimuliMap getAllStimuliAround(Point2D.Double position);
}
