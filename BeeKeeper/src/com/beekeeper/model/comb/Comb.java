package com.beekeeper.model.comb;

<<<<<<< HEAD
import java.awt.Dimension;
=======
import java.awt.geom.Point2D.Double;
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper
import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.parameters.ModelParameters;

public class Comb
{
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	
	private ArrayList<CombCell> cells = new ArrayList<>();
	
	public int ID;

	protected int combWidth = 60;
	protected int combHeight = 60;
	
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
	};
	
	public Comb()
	{
<<<<<<< HEAD
		cells = CombUtility.fillCells(size.width,size.height,ID);
=======
		cells = CombManager.fillCells(combWidth,combHeight,ID);
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper
		
<<<<<<< HEAD
		int azerrefgd = 59;
=======
		/*
		int azerrefgd = 180;
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper
		
		cells.get(azerrefgd).filled = true;
		
<<<<<<< HEAD
		for(Integer i : CombUtility.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, size))
=======
		for(Integer i : CombManager.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, combWidth, combHeight))
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper
		{
			cells.get(i).filled = true;
		}
<<<<<<< HEAD
=======
		*/
		
		this.agents = bees;
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper
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

<<<<<<< HEAD
	public boolean isCellVisitEmpty(int x, int y) {
		return this.cells.get(x + y * size.width).visiting == null;
	}

	public void setCellVisit(int x, int y, WorkingAgent bee) {
		this.cells.get(x + y * size.width).visiting = bee;
		this.agents.add(bee);
=======
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
>>>>>>> branch 'master' of https://github.com/Kwarthys/BeeKeeper
	}
}
