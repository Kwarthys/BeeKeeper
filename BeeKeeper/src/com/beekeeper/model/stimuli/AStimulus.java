package com.beekeeper.model.stimuli;

public abstract class AStimulus
{
	protected double timeDecay;
	protected double transmissibility;
	protected double smellRange;
	protected Stimuli type;
	
	protected double amount;
	
	public double getSmellRange() {return smellRange;}
	
	public Stimuli getStimulusType() {return type;}
	
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
	
	public double getAmount() {return this.amount;}
}
