package com.beekeeper.model.agent.implem;

import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public class FoodSource extends EmitterAgent
{
	protected double maxCellLoad = 2;
	
	protected double foodAmount = 0;

	public FoodSource(StimuliManagerServices stimuliManagerServices)
	{
		super(stimuliManagerServices);
		this.type = AgentType.FOOD_SOURCE;
	}


	@Override
	public void live()
	{
		foodAmount += 0.1;
		foodAmount = foodAmount > maxCellLoad ? maxCellLoad : foodAmount;
		this.emit(Stimulus.FoodSmell, foodAmount);
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
}
