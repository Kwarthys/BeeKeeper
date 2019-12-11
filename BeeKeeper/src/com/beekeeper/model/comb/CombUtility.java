package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.util.ArrayList;

import com.beekeeper.model.comb.cell.CombCell;

public class CombUtility
{
	public static ArrayList<CombCell> fillCells(Dimension size, int combID, CombServices services)
	{
		ArrayList<CombCell> cells = new ArrayList<>();
		
		for(int j = 0; j < size.height; j++)
		{
			for(int i = 0; i < size.width; i++)	
			{
				CombCell cb = new CombCell(i,j,j*size.width+i,combID, services);
				cells.add(cb);
			}
		}		
		
		return cells;
	}
	
	public static ArrayList<Integer> getCellNeighbors(int index, Dimension size)
	{
		int x = index % size.width;
		int y = (index - x) / size.width;
		return getCellNeighbors(x, y, size);
	}

	public static ArrayList<Integer> getCellNeighborsUp(int cellX, int cellY, Dimension size)
	{
		ArrayList<Integer> cells = new ArrayList<>();
		int sizeX = size.width;
		int sizeY = size.height; 
		
		int cellIndex = cellY * sizeX + cellX;
		 
		int maxIndex = sizeX * sizeY;
		
		int offset = cellY%2 == 0 ? 0:1;

		if(cellX + offset != 0)
		{
			int index = cellIndex - sizeX -1 + offset;
			if(index >= 0 && index < maxIndex)
				cells.add(index);
		}
		if(cellX + offset != sizeX)
		{
			int index = cellIndex - sizeX + offset;
			if(index >= 0 && index < maxIndex)
				cells.add(index);
		}
		
		return cells;
	}
	
	public static ArrayList<Integer> getCellNeighborsDown(int cellX, int cellY, Dimension size)
	{
		ArrayList<Integer> cells = new ArrayList<>();
		int sizeX = size.width;
		int sizeY = size.height; 
		
		int cellIndex = cellY * sizeX + cellX;
		 
		int maxIndex = sizeX * sizeY;
		
		int offset = cellY%2 == 0 ? 0:1;

		if(cellX + offset != 0)
		{
			int index = cellIndex + sizeX -1 + offset;
			if(index >= 0 && index < maxIndex)
				cells.add(index);
		}
		if(cellX + offset != sizeX)
		{
			int index = cellIndex + sizeX + offset;
			if(index >= 0 && index < maxIndex)
				cells.add(index);
		}
		
		return cells;
	}
	
	public static ArrayList<Integer> getCellNeighbors(int cellX, int cellY, Dimension size)
	{
		ArrayList<Integer> cells = new ArrayList<>();
		int sizeX = size.width;

		int cellIndex = cellY * sizeX + cellX;
		if(cellX != 0)
			cells.add(cellIndex -1);
		if(cellX + 1 != sizeX)
			cells.add(cellIndex +1);

		cells.addAll(getCellNeighborsUp(cellX, cellY, size));
		cells.addAll(getCellNeighborsDown(cellX, cellY, size));
		
		return cells;
	}
}
