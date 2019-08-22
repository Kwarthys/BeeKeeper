package com.beekeeper.model.comb;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.Agent;

public class Comb
{
	private ArrayList<Agent> agents;
	
	public int ID;
	
	public Comb(ArrayList<Agent> bees)
	{
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

	public void removeTheDead()
	{	
		agents.removeIf(new Predicate<Agent>() {
			@Override
			public boolean test(Agent t) {
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

	public void setID(int id)
	{
		ID = id;
		for(CombCell c : cells)
		{
			c.setCombID(id);
		}
		for(EmptyBee b : agents)
		{
			b.setCombID(id);
		}
	}
}
