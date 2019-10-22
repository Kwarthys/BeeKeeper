package com.beekeeper.model.comb.cell;

import com.beekeeper.model.agent.Agent;

public class CombCell
{	
	protected int combID = -1;
	public boolean filled = false;
	
	public int x,y;
	
	public Agent visiting = null;
	public Agent inside = null;
	
	public CombCell(int x, int y, int combID)
	{
		this.combID = combID;
		this.x = x;
		this.y = y;
	}
}
