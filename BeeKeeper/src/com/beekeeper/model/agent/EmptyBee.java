package com.beekeeper.model.agent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;

public abstract class EmptyBee extends Agent
{
	protected ArrayList<Task> taskList = new ArrayList<>();
	protected abstract void fillTaskList();
	
	private double energy;
	protected StimuliLoad stimuliLoad;
	protected StimuliManagerServices stimuliManagerServices;
	protected MainControllerServices controllerServices;
	protected double stomach;
	
	protected StimuliMap lastPercievedMap;
	
	protected int combID = -1;
	
	protected Task currentTask;
	
	public Point2D.Double target = null;
	
	protected BeeType type;
	
	public Task getCurrentTask() {return currentTask;}
	
	public EmptyBee(StimuliManagerServices stimuliManagerServices)
	{
		this(stimuliManagerServices, 150+Math.random()*200, 150+Math.random()*200);
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

	public void setCombID(int id)
	{
		this.combID = id;
	}
	
	public HashMap<String, Double> getAllThresholds()
	{
		HashMap<String, Double> ts = new HashMap<String, Double>();
		
		for(Task t : taskList)
		{
			ts.put(t.taskName, t.threshold);
		}
		
		return ts;
	}

	public StimuliMap getPercievedStimuli() {
		return lastPercievedMap;
	}
}
