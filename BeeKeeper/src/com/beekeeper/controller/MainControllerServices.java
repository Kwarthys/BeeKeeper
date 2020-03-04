package com.beekeeper.controller;

import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.tasks.Task;

public interface MainControllerServices
{	
	public void logMyTaskSwitch(Task newTask, int beeID);

	public CombCell askLandingZone();

	public void notifyWindowClosed();
	
	public void switchFrames(int index1, int index2);
	public void reverseFrame(int index);
}
