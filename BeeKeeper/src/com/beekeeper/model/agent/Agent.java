package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

import com.beekeeper.utils.IDManager;

public abstract class Agent
{	
	protected Point2D.Double position;
	
	protected int ID;

	public abstract void live();
	public abstract void move(double dx, double dy);
	
	public Point2D.Double getPosition() {return position;}
	public int getID() {return ID;}
	
	public boolean alive = true;
	
	
	public Agent()
	{
		this.ID = IDManager.getNextID();
	}
	
	public Agent(Double x, Double y)
	{
		this();
		this.position = new Point2D.Double(x,y);
	}
}
