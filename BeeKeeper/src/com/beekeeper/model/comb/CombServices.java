package com.beekeeper.model.comb;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public interface CombServices
{
	public ArrayList<Agent> getBees();
	public ArrayList<CombCell> getCells();

	public ArrayList<Integer> getNeighbors(int x, int y);
	public ArrayList<Integer> getDownNeighbors(int x, int y);
	public ArrayList<Integer> getUpNeighbors(int x, int y);
	public boolean askMoveToCell(Agent who, Integer where);
	public boolean askMoveToFacingCell(Agent who, int cellNumber);
	
	public CombCell getCellAt(int x, int y);
	
	public int getID();
	public ArrayList<WorkingAgent> getNeighborBees(int x, int y);
	public void notifyTakeOff(Agent a);
	public void notifyLanding(Agent a);
	public void swap(int cellIndexSwap1, int cellIndexSwap2);
	
	public StimuliManagerServices getCurrentSManagerServices();
	public boolean isFacingAnotherComb();
}
