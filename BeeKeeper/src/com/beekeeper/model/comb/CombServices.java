package com.beekeeper.model.comb;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CombCell;

public interface CombServices
{
	public ArrayList<Agent> getBees();
	public ArrayList<CombCell> getCells();
	
	public ArrayList<Integer> getNeighbors(int x, int y);
	public void askMoveToCell(Agent who, Integer where);
	
	public int getID();
	public ArrayList<WorkingAgent> getNeighborBees(int x, int y);	
}
