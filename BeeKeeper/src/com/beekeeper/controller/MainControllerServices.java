package com.beekeeper.controller;

import com.beekeeper.model.tasks.Task;

public interface MainControllerServices
{	
	public void logMyTaskSwitch(Task newTask, int beeID);
	
	public double getHiveTemperature();
}
