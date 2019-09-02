package com.beekeeper.model.comb;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.parameters.ModelParameters;

public class Comb
{
	private ArrayList<Agent> agents;
	
	private ArrayList<CombCell> cells = new ArrayList<>();
	
	public int ID;

	protected int combWidth = 60;
	protected int combHeight = 60;
	
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
		cells = CombManager.fillCells(combWidth,combHeight,ID);
		
		/*
		int azerrefgd = 180;
		
		cells.get(azerrefgd).filled = true;
		
		for(Integer i : CombManager.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, combWidth, combHeight))
		{
			cells.get(i).filled = true;
		}
		*/
		
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

	public boolean isCellTaken(Double cellPos)
	{
		return getCellAt(cellPos).filled;
	}

	public CombCell getCellAt(Double cellPos) {
		System.out.println(cellPos);
		int y = (int)(cellPos.y / ModelParameters.COMBCELL_SIZE);
		double offset = y%2 == 0 ? 0 : -0.5;
		int x = (int)(offset + cellPos.x / ModelParameters.COMBCELL_SIZE);
		
		System.out.println(x + "|" + y + " offset:" + offset);
		System.out.println("Checking now");
		
		return cells.get(y * combWidth + x);
	}
}
