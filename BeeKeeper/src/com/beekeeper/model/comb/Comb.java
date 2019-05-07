package com.beekeeper.model.comb;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.comb.cell.CombCell;

public class Comb
{
	public ArrayList<EmptyBee> agents;
	private ArrayList<CombCell> cells;
	
	public Comb(ArrayList<EmptyBee> agents, ArrayList<CombCell> cells)
	{
		this.agents = agents;
		this.cells = cells;
	}
	
	public void liveAgents()
	{
		for(EmptyBee b : agents)
		{
			b.live();
		}
				
		agents.removeIf(new Predicate<EmptyBee>() {
			@Override
			public boolean test(EmptyBee t) {
				return !t.alive;
			}
		});
	}
	
	public void liveCells()
	{
		for(CombCell c : cells)
		{
			c.live();
		}
	}
}
