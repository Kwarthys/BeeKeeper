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
	
	private Dimension size;
	
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
			if(newCell.visiting == null)
			{				
				who.hostCell.visiting = null;
				who.hostCell = newCell;
				newCell.visiting = who;
			}
		}
	};
	
	public Comb(Dimension combSize)
	{
		this.size = new Dimension(combSize);
		cells = CombUtility.fillCells(size,ID, services);
	}
	
	protected void testNeighborhood() //Not private to avoid "not used" warning
	{
		int azerrefgd = 1;
		
		cells.get(azerrefgd).filled = true;
		
		for(Integer i : CombUtility.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, size))
		{
			System.out.print(i + " ");
			cells.get(i).filled = true;
		}
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

	public Dimension getDimension() {
		return size;
	}
}
