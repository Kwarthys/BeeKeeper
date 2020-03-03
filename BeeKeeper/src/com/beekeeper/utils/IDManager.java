package com.beekeeper.utils;

public class IDManager
{
	private static int id = 0;
	
	public static int getNextID()
	{
		System.out.println("Giving id " + id);
		return id++;
	}

	public static void resetIDCounter()
	{
		id = 0;
	}
}
