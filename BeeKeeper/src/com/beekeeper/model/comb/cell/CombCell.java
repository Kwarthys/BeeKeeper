package com.beekeeper.model.comb.cell;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.CombServices;

public class CombCell
{	
	protected int combID = -1;
	public boolean filled = false;
	
	public int x,y;
	
	public Agent visiting = null;
	public Agent inside = null;
	
	protected ArrayList<CombCell> neighbors;
	
	private CombServices cs;
	
	public CombCell(int x, int y, int combID, CombServices cs)
	{
		this.combID = combID;
		this.x = x;
		this.y = y;
		this.cs = cs;
	}

	public ArrayList<Integer> getNeighbors() {
		return cs.getNeighbors(x, y);
	}

	public void askMoveToCell(Agent who, Integer where) {
		cs.askMoveToCell(who, where);	
	}

	public ArrayList<WorkingAgent> getNeighborBees() {
		return cs.getNeighborBees(x,y);
		
	}
}
