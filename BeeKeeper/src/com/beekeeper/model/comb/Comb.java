package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.StimuliMap;

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
			else
			{
				//Contact
				WorkingAgent a = (WorkingAgent) who;
				WorkingAgent b = (WorkingAgent) newCell.visiting;
				//System.out.println("Contact by movement " + a.getID() + " " + b.getID());
				StimuliMap.contactBetween(a.getBodySmells(), b.getBodySmells());
			}
		}

		@Override
		public ArrayList<WorkingAgent> getNeighborBees(int x, int y) {
			ArrayList<Integer> c = getNeighbors(x, y);
			ArrayList<WorkingAgent> agents = new ArrayList<>();
			for(Integer i : c)
			{
				WorkingAgent a = (WorkingAgent)cells.get(i).visiting;
				if(a!=null)
				{
					agents.add(a);
				}
			}			
			
			return agents;			
		}

		@Override
		public ArrayList<Integer> getDownNeighbors(int x, int y) {
			return CombUtility.getCellNeighborsDown(x, y, getDimension());
		}

		@Override
		public void notifyTakeOff(Agent a) {
			agents.remove(a);
		}

		@Override
		public void notifyLanding(Agent a) {
			agents.add(a);
		}

		@Override
		public CombCell getCellAt(int x, int y) {
			return cells.get(y*size.width + x);
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
		
		cells.get(azerrefgd).content = CellContent.food;
		
		for(Integer i : CombUtility.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, size))
		{
			System.out.print(i + " ");
			cells.get(i).content = CellContent.food;
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
		
		for(CombCell c : cells)
		{
			if(c.inside != null)
			{
				if(!c.inside.alive)
				{
					c.inside = null;
					c.content = CellContent.empty;
				}
			}
		}
	}
	
	public boolean isCellContentEmpty(int x, int y)
	{
		return this.cells.get(x + y * size.width).content == CellContent.empty;
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
	
	public void setCellAgentContent(int x, int y, WorkingAgent brood)
	{
		CombCell cell = this.cells.get(x + y * size.width); 
		cell.inside = brood;
		cell.content = CellContent.brood;
		brood.hostCell = cell;
	}

	public Dimension getDimension() {
		return size;
	}

	public ArrayList<CombCell> getLandingZone() {
		ArrayList<CombCell> lastRow = new ArrayList<>();
		for(int x = 0; x < size.width; x++)
		{
			lastRow.add(cells.get((size.height-1) * size.width + x));
		}
		return lastRow;
	}
}
