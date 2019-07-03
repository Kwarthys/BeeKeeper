package com.beekeeper.model.stimuli;

public abstract class AStimulus
{
	protected double timeDecay = 0.1;
	protected double transmissibility = 0.5;
	protected double smellRange = 0.95;
	protected Stimulus type;
	
	protected double amount;
	
	public double getSmellRange() {return smellRange;}
	
	public Stimulus getStimulusType() {return type;}
	
	public AStimulus(double d)
	{
		this.amount = d;
	}

	public void evaporate()
	{
		amount *= (1-timeDecay);
		if(amount < 0.01)
		{
			amount = 0;
		}
	}
	
	public void add(double amount)
	{
		//System.out.println("thisamount " + this.amount + " + " + amount);
		this.amount += amount;
	}
	
	public double getAmountOverDistance(double distance)
	{
		return amount * Math.pow(getSmellRange(), distance);
	}
	
	public double getAmount() {return this.amount;}
}
