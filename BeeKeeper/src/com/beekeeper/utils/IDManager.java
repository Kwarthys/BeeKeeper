package com.beekeeper.utils;

public class IDManager
{
	private static int id = 0;
	
	public static int getNextID()
	{
		return id++;
	}
}
