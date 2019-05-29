package com.beekeeper.controller;

import java.awt.geom.Point2D;

import com.beekeeper.model.agent.implem.BroodBee;
import com.beekeeper.model.agent.implem.FoodSource;
import com.beekeeper.model.tasks.Task;

public interface MainControllerServices
{
	public BroodBee getLarvaeByPos(Point2D.Double larvaePos, int combID);
	
	public FoodSource getFoodSourceByPos(Point2D.Double pos, int combID);
	
	public void logMyTaskSwitch(Task newTask, int beeID);
}
