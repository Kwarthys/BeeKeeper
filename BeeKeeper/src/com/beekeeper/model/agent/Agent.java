package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

public abstract class Agent
{
	private static int indexCounter = 1;
	private static int getNextID() {return indexCounter++;}
	
	protected Point2D.Double position;
	
	protected int ID;

	public abstract void live();
	public abstract void move(double dx, double dy);
	
	public Point2D.Double getPosition() {return position;}
	public int getID() {return ID;}
	
	public boolean alive = true;
	
	
	public Agent()
	{
		this.ID = getNextID();
	}
}
