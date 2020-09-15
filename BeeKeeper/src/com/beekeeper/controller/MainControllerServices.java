package com.beekeeper.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.model.agent.AgentStateSnapshot;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.tasks.Task;

public interface MainControllerServices
{	
	public int getCurrentTimeStep();
	
	public void logMyTaskSwitch(Task newTask, int beeID);

	public CombCell askLandingZone();

	public void notifyWindowClosed();

	public void notifyAgentContact(int id1, int id2, double amount);
	public HashMap<Integer, Integer> getAgentContacts();
	public void freeLockAgentContacts();
	
	public void switchFrames(int index1, int index2);
	public void reverseFrame(int index);
	public void liftFrame(int frameIndex);
	public void putFrame(int frameIndex, int pos, boolean reverse);

	public void layEgg(CombCell cell);

	public ArrayList<Comb> getCombs();
	public ArrayList<Integer> getForagers();
	
	public ArrayList<Integer> getTheDead();
	
	public ArrayList<AgentStateSnapshot> getAllAdults();
	
	public void setNumberOfSecondsToGoFast(int seconds);
}
