package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public abstract class EmptyBee extends Agent
{
	protected abstract void fillTaskList();
	private double energy;
	protected StimuliLoad stimuliLoad;
	protected StimuliManagerServices stimuliManagerServices;
	protected MainControllerServices controllerServices;
	protected double stomach;
	
	public Point2D.Double target = null;
	
	protected BeeType type;
	
	public EmptyBee(StimuliManagerServices stimuliManagerServices)
	{
		this(stimuliManagerServices, 100+Math.random()*300, 100+Math.random()*300);
	}
	
	public EmptyBee(StimuliManagerServices stimuliManagerServices, double x, double y)
	{
		super(x,y);
		this.stimuliManagerServices = stimuliManagerServices;
		setEnergy(Math.random()*0.8+0.2);
	}
	
	public BeeType getBeeType() {return this.type;}
	
	public void setEnergy(double amount)
	{
		this.energy = 0;
		addToEnergy(amount);
	}
	
	public void receiveFood(double amount)
	{
		this.addToEnergy(amount);
		//System.out.println(ID + " receiving food to " + energy);
	}
	
	public boolean isHungry()
	{
		return this.energy < 0.8;
	}
	
	public double getEnergy() {return this.energy;}
	public StimuliLoad getExternalStimuli() {return this.stimuliLoad;}
	
	protected void addToEnergy(double amount)
	{
		this.energy += amount;
		this.energy = this.energy < 0 ? 0 : this.energy > 1 ? 1 : this.energy; //Clamp energy between 0 and 1
	}
}
