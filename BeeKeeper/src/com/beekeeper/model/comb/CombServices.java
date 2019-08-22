package com.beekeeper.model.comb;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.cell.CombCell;

public interface CombServices
{
	public ArrayList<Agent> getBees();
	public ArrayList<CombCell> getCells();
	
	public int getID();	
}
