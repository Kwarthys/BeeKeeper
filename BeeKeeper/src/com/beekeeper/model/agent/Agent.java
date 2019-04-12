package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

public abstract class Agent
{
	protected Point2D.Double position;

	public abstract void live();
	public abstract void move(double dx, double dy);
	
	public Point2D.Double getPosition() {return position;}
}
