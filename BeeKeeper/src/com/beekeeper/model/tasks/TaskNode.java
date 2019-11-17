package com.beekeeper.model.tasks;

public interface TaskNode {

	public Action execute();
	
	public boolean check();
}
