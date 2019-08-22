package com.beekeeper.model.comb.cell;

public class CombCell
{	
	protected int combID = -1;
	public boolean filled = false;
	
	public int x,y;
	
	public CombCell(int x, int y, int combID)
	{
		this.combID = combID;
		this.x = x;
		this.y = y;
	}
}
