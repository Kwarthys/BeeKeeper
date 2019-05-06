package com.beekeeper.controller;

import java.awt.geom.Point2D;

import com.beekeeper.model.agent.BroodBee;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.tasks.Task;

public interface MainControllerServices
{
	public BroodBee getLarvaeByPos(Point2D.Double larvaePos);

	public CombCell getCellByPos(Point2D.Double targetpos);
	
	public void logMyTaskSwitch(Task newTask, int beeID);
}
