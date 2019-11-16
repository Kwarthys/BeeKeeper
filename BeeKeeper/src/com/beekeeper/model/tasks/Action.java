package com.beekeeper.model.tasks;

public abstract class Action implements TaskNode {

	@Override
	public abstract void execute();

	@Override
	public abstract boolean check();

}
