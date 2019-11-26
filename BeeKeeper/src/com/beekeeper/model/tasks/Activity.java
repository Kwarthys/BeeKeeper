package com.beekeeper.model.tasks;

import java.util.ArrayList;

public abstract class Activity implements TaskNode {
	
	private ArrayList<TaskNode> nodes = new ArrayList<>();

	@Override
	public Action execute() {
		for(TaskNode t : nodes)
		{
			if(t.check())
			{
				return t.execute();
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
