package com.beekeeper.model.comb;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.cell.CombCell;

public class Comb
{
	private ArrayList<Agent> agents;
	
	private ArrayList<CombCell> cells = new ArrayList<>();
	
	public int ID;
	
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
	};
	
	public Comb(ArrayList<Agent> bees)
	{
		cells = CombManager.fillCells(20,20,ID);
		
		int azerrefgd = 180;
		
		cells.get(azerrefgd).filled = true;
		
		for(Integer i : CombManager.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, 20, 20))
		{
			cells.get(i).filled = true;
		}
		
		this.agents = bees;
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
}
