package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.utils.IDManager;
import com.beekeeper.utils.MyUtils;

public abstract class Agent
{	
	private double energy;
	
	protected int ID;
	
	protected int combID = -1;
	
	protected AgentType type;	
	public AgentType getBeeType() {return this.type;}

	public abstract void live();
	
	public int getID() {return ID;}
	
	public boolean alive = true;
	
	public CombCell hostCell;
	
	
	public Agent()
	{
		this.ID = IDManager.getNextID();
	}
	
	public void setCombID(int id)
	{
		this.combID = id;
	}

/*
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
		this.rotation += (Math.random() - 0.5) * 0.8;
		this.position.setLocation(this.position.x + speed * Math.cos(rotation), this.position.y + speed * Math.sin(rotation));
	}

	protected void move(double dx, double dy) {
		this.position.setLocation(this.position.getX() + dx, this.position.getY() + dy);
		this.rotation = MyUtils.getRotFromDir(dx, dy);
	}
*/
	
	public double getEnergy() {return this.energy;}
	
	public void addToEnergy(double amount)
	{
		this.energy += amount;
		this.energy = this.energy < 0 ? 0 : this.energy > 1 ? 1 : this.energy; //Clamp energy between 0 and 1
	}
	
	public void setEnergy(double amount)
	{
		this.energy = 0;
		addToEnergy(amount); //ensuring energy stays in [0;1]
	}
}
