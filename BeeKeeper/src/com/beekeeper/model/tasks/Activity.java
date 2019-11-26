package com.beekeeper.model.tasks;

import java.util.ArrayList;

public abstract class Activity implements TaskNode {
	
	private ArrayList<TaskNode> nodes = new ArrayList<>();

	@Override
	public Action search() {
		for(TaskNode t : nodes)
		{
			if(t.check())
			{
				return t.search();
			}
		}
		
		System.err.println("No tasknode available");
		return null;
	}

	@Override
	public abstract boolean check();
	
	public void addTaskNode(TaskNode t)
	{
		nodes.add(t);
	}

}
