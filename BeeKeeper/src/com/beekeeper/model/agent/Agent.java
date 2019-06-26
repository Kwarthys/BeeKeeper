package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

import com.beekeeper.utils.IDManager;

public abstract class Agent
{	
	protected Point2D.Double position;
	
	protected int ID;
	
	protected int combID = -1;
	
	protected AgentType type;	
	public AgentType getBeeType() {return this.type;}

	public abstract void live();
	
	public Point2D.Double getPosition() {return position;}
	public int getID() {return ID;}
	
	public boolean alive = true;
	
	
	public Agent()
	{
		this.ID = IDManager.getNextID();
	}
	
	public void setCombID(int id)
	{
		this.combID = id;
	}
	
	public Agent(Double x, Double y)
	{
		this();
		this.position = new Point2D.Double(x,y);
	}


	public void moveTowards(Point2D.Double position)
	{
		double speed = 1;
		
		double dx = position.getX() - this.position.getX();
		double dy = position.getY() - this.position.getY();
		
		Point2D.Double vector = new Point2D.Double(dx,dy);
		double norme = vector.distance(new Point2D.Double(0,0));

		vector.x = vector.getX() / norme * Math.min(speed, norme);
		vector.y = vector.getY() / norme * Math.min(speed, norme);

		move(vector.x, vector.y);
	}

	public void randomMove()
	{
		double speed = 1.0;
		this.move((Math.random()*2-1*speed), (Math.random()*2-1)*speed);
	}

	protected void move(double dx, double dy) {
		this.position.setLocation(this.position.getX() + dx, this.position.getY() + dy);	
	}
}
