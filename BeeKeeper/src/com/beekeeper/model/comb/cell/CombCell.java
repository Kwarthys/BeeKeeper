package com.beekeeper.model.comb.cell;

import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.CombServices;

public class CombCell
{	
	protected int combID = -1;
	
	public int x,y;
	public int number;
	
	public Agent visiting = null;
	public WorkingAgent inside = null;
	
	public CellContent content = CellContent.empty;
	
	protected ArrayList<CombCell> neighbors;
	
	private CombServices cs;
	
	public CombCell(int x, int y, int number, int combID, CombServices cs)
	{
		this.combID = combID;
		this.x = x;
		this.y = y;
		this.cs = cs;
		this.number = number;
	}

	public ArrayList<Integer> getNeighbors() {
		return cs.getNeighbors(x, y);
	}

	public boolean askMoveToCell(Agent who, Integer where) {
		return cs.askMoveToCell(who, where);
	}

	public ArrayList<WorkingAgent> getNeighborBees() {
		return cs.getNeighborBees(x,y);		
	}

	public ArrayList<Integer> getUpCells() {
		return cs.getUpNeighbors(x,y);
	}

	public ArrayList<Integer> getDownCells() {
		return cs.getDownNeighbors(x,y);		
	}

	public WorkingAgent getAgentInside() {
		return inside;
	}
	
	public int getCombID() {return combID;}

	public void freeCell() {
		cs.notifyTakeOff(visiting);
		visiting = null;
	}

	public void notifyLanding(EmitterAgent a) {
		cs.notifyLanding(a);
		visiting = a;		
	}

	public boolean isFacingAnotherCell() {
		return cs.isFacingAnotherComb();
	}

	public boolean askMoveToFacingCell(Agent who) {
		return cs.askMoveToFacingCell(who, number);		
	}
}
