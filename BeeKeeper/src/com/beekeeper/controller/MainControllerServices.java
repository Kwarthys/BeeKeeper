package com.beekeeper.controller;

import java.awt.geom.Point2D;

import com.beekeeper.model.agent.BroodBee;

public interface MainControllerServices
{
	public BroodBee getLarvaeByPos(Point2D.Double larvaePos);
}
