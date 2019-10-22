package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.util.ArrayList;

import com.beekeeper.model.comb.cell.CombCell;

public class CombUtility
{
	public static ArrayList<CombCell> fillCells(int x, int y, int combID)
	{
		ArrayList<CombCell> cells = new ArrayList<>();
		
		for(int j = 0; j < y; j++)
		{
			for(int i = 0; i < x; i++)	
			{
				CombCell cb = new CombCell(i,j,combID);
				cells.add(cb);
			}
		}		
		
		return cells;
	}
	
	public static ArrayList<Integer> getCellNeighbors(int cellX, int cellY, Dimension size)
	{
		ArrayList<Integer> cells = new ArrayList<>();
		 int sizeX = size.width;
		 int sizeY = size.height; 

		int cellIndex = cellY * sizeY + cellX;
		if(cellX != 0)
			cells.add(cellIndex -1);
		if(cellX + 1 != sizeX)
			cells.add(cellIndex +1);
		
		int offset = cellY%2 == 0 ? 0:1;
		
		if(cellX + offset != 0)
			cells.add(cellIndex - sizeY -1 + offset);
		if(cellX + offset != sizeX)
			cells.add(cellIndex - sizeY + offset);

		if(cellX + offset != 0)
			cells.add(cellIndex + sizeY -1 + offset);
		if(cellX + offset != sizeX)
			cells.add(cellIndex + sizeY + offset);				
		
		return cells;
	}
}
