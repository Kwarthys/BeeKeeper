package com.beekeeper.model.comb.cell;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.declarations.FoodSmellStimulus;

public class CombCell
{
	protected Point2D.Double position;
	protected StimuliLoad stimuliLoad;
	
	protected double maxCellLoad = 1;
	
	protected double foodAmount = 0;
	
	protected int combID = -1;

	public CombCell()
	{
		this(100+Math.random()*300, 100+Math.random()*300);
	}

	public CombCell(double x, double y)
	{
		this.position = new Point2D.Double(x,y);
		this.stimuliLoad = new StimuliLoad(this.position);
	}
	
	public void live()
	{
		foodAmount += 0.01;
		foodAmount = foodAmount > maxCellLoad ? maxCellLoad : foodAmount;
		this.stimuliLoad.emit(new FoodSmellStimulus(foodAmount));
	}
	
	public double takeFood(double max)
	{
		//System.out.println("FoodTaken : " + this.foodAmount + " - " + max);
		if(this.foodAmount > max)
		{
			this.foodAmount -= max;
			return max;
		}
		else
		{
			double value = this.foodAmount;
			this.foodAmount = 0;
			return value;
		}
	}
	
	public Point2D.Double getPosition(){return this.position;}
	public StimuliLoad getExternalStimuli() {return this.stimuliLoad;}

	public void setCombID(int id)
	{
		this.combID = id;
	}
}
