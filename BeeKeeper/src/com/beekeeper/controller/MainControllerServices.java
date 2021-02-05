package com.beekeeper.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.model.agent.Agent;
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
	
	public void askRestart();
	
	public void notifyDeath(Agent a);

	public void notifyAgentContact(int id1, int id2, double amount);
	public HashMap<Integer, Double> getAgentContacts();
	public void freeLockAgentContacts();
	
	public void switchFrames(int index1, int index2);
	public void reverseFrame(int index);
	public void liftFrame(int frameIndex);
	public void hitFrame(int frameIndex);
	public void putFrame(int frameIndex, int pos, boolean reverse);
	
	public void rebase(int[] frameIds, boolean keepForagers);

	public void layEgg(CombCell cell);

	public ArrayList<Comb> getCombs();
	public ArrayList<Integer> getForagers();
	
	public ArrayList<Integer> getTheDead();
	
	public ArrayList<AgentStateSnapshot> getAllAdults();
	
	public void setNumberOfSecondsToGoFast(int seconds);
	
	public boolean isFastForward();

	public void spawnBeeAt(int combID, Point position);

	public void notifyLiftoff(Agent agent);
	public void notifyLanding(Agent agent);
	
	/**
	 * Blocking call that will be released when next timestep is finished. Will not work if multiple client are requesting
	 */
	public void waitForTimeStep();
}
