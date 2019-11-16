package com.beekeeper.model.tasks;

import java.util.ArrayList;

public class Activity implements TaskNode {
	
	private ArrayList<TaskNode> nodes = new ArrayList<>();

	@Override
	public void execute() {
		for(TaskNode t : nodes)
		{
			if(t.check())
			{
				t.execute();
				return;
			}
		}
	}

	@Override
	public boolean check() {
		return true;
	}
	
	public void addTaskNode(TaskNode t)
	{
		nodes.add(t);
	}

}
