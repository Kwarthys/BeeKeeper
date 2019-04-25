package com.beekeeper.model.comb.cell;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.declarations.FoodSmellStimulus;

public class CombCell
{
	protected Point2D.Double position;
	protected StimuliLoad stimuliLoad;
	
	protected double foodAmount = 0;

	public CombCell()
	{
		this.position = new Point2D.Double(100+Math.random()*300, 100+Math.random()*300);
		this.stimuliLoad = new StimuliLoad(this.position);
	}
	
	public void live()
	{
		foodAmount += 0.01;
		foodAmount = foodAmount > 3 ? 3 : foodAmount;
		this.stimuliLoad.emit(new FoodSmellStimulus(foodAmount));
	}
	
	public double takeFood(double max)
	{
		if(this.foodAmount < max)
		{
			double value = this.foodAmount;
			this.foodAmount -= max;
			return value;
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
}
