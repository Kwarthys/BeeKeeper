package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CombCell;

public class Comb
{
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	
	private ArrayList<CombCell> cells = new ArrayList<>();
	
	public int ID;
	
	private Dimension size = new Dimension(10,10);
	
	private CombServices services = new CombServices() {		
		@Override
		public int getID() {
			return ID;
		}
		
		@Override
		public ArrayList<CombCell> getCells() {
			return cells;
		}	
		
		@Override
		public ArrayList<Agent> getBees() {
			return agents;
		}

		@Override
		public ArrayList<Integer> getNeighbors(int x, int y){			
			return CombUtility.getCellNeighbors(x, y, size);
		}

		@Override
		public void askMoveToCell(Agent who, Integer where) {
			CombCell newCell = cells.get(where);
			if(isCellVisitEmpty(newCell.x, newCell.y))
			{				
				who.hostCell.visiting = null;
				who.hostCell = newCell;
				//TODO FINISH HERE MOVE
			}
		}
	};
	
	public Comb()
	{
		cells = CombUtility.fillCells(size.width,size.height,ID, services);
		/*
		int azerrefgd = 59;
		
		cells.get(azerrefgd).filled = true;
		
		for(Integer i : CombUtility.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, size))
		{
			cells.get(i).filled = true;
		}
		*/
	}

	public ArrayList<Agent> getAgents(){return agents;}

	public void setID(int id)
	{
		ID = id;
		for(Agent b : agents)
		{
			b.setCombID(id);
		}
	}
	
	public CombServices getServices()
	{
		return services;
	}

	public void removeTheDead()
	{	
		agents.removeIf(new Predicate<Agent>() {
			@Override
			public boolean test(Agent t) {
				return !t.alive;
			}
		});
	}

	public boolean isCellVisitEmpty(int x, int y) {
		return this.cells.get(x + y * size.width).visiting == null;
	}

	public boolean isCellTaken(int x, int y) {
		return !isCellVisitEmpty(x, y);
	}

	public void setCellVisit(int x, int y, WorkingAgent bee) {
		CombCell cell = this.cells.get(x + y * size.width); 
		cell.visiting = bee;
		bee.hostCell = cell;
		this.agents.add(bee);
	}
}
