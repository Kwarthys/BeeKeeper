package com.beekeeper.utils;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class MyLockedList<T> extends ArrayList<T>{

	private volatile boolean lock = false;
	
	public ArrayList<T> waitGetFullCopyAndEmpty()
	{
		while(lock) {};
		
		lock = true;
		ArrayList<T> list = new ArrayList<>();
		this.forEach((T t) -> list.add(t));
		this.clear();
		lock = false;
		return list;
	}
	
	public void waitAndPost(T item)
	{
		while(lock) {};
		
		lock = true;
		add(item);
		lock = false;
		return;
	}
	
	public boolean tryPost(T item)
	{
		if(lock)
		{
			return false;
		}
		else
		{
			waitAndPost(item);
			return true;
		}
	}
}
