package com.beekeeper.model.agent;

import java.awt.Point;
import java.util.ArrayList;

import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.utils.IDManager;

public abstract class Agent
{	
	protected int age = 0;
	protected int activeAge = 0;
	
	private double energy = 1;
	
	protected int ID;
	
	protected double hunger = 0;
	public double getHunger() {return hunger;}	
	protected boolean receivingFood = false;
	
	protected AgentType type;	
	public AgentType getBeeType() {return this.type;}

	public abstract void live();
	
	public int getID() {return ID;}
	
	public boolean alive = true;
	
	public CombCell hostCell;
	public boolean isInside() {return hostCell!=null;}
	
	protected int lastVisitedCellNumber = -1;
	
	public abstract void registerNewStimuliManagerServices(StimuliManagerServices stimuliManagerServices);
	
	public Agent()
	{
		this.ID = IDManager.getNextID();
	}
	
	public Point getPosition()
	{
		if(isInside())
		{
			return new Point(hostCell.x, hostCell.y);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		return getStringName();
	}
	
	public abstract String getStringName();

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
*/
	
	public void randomMove()
	{
		if(Math.random() > 0.9)
		{
			//CombSwitch
			if(hostCell.isFacingAnotherCell())
			{
				hostCell.askMoveToFacingCell(this);
				lastVisitedCellNumber = -1;
				return;
			}
		}
		
		ArrayList<Integer> cells = hostCell.getNeighbors();
		int r = (int) (Math.random() * cells.size());
		//Bee can't randomly go back where it was
		if(lastVisitedCellNumber != -1)
		{
			while(cells.get(r) == lastVisitedCellNumber)
			{
				r = (int) (Math.random() * cells.size()); 
			}
		}
		lastVisitedCellNumber = hostCell.number;
		hostCell.askMoveToCell(this, cells.get(r));
	}
	
	public double getEnergy() {return this.energy;}
	
	protected void addToEnergy(double amount)
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
