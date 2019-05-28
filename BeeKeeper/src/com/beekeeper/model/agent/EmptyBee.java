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
	
	protected Task currentTask = null;
	
	public Point2D.Double target = null;
	
	protected BeeType type;
	
	public Task getCurrentTask() {return currentTask;}
	
	public EmptyBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		this(stimuliManagerServices, controllerServices, 150+Math.random()*200, 150+Math.random()*200);
	}
	
	public EmptyBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, double x, double y)
	{
		super(x,y);
		this.stimuliManagerServices = stimuliManagerServices;
		this.controllerServices = controllerServices;
		setEnergy(Math.random()*0.8+0.2);
	}
	
	public BeeType getBeeType() {return this.type;}
	
	public void live()
	{
		if(alive == false)
		{
			return;
		}
		
		if(getEnergy() == 0)
		{
			alive = false;
			return;
		}
		
		StimuliMap s = stimuliManagerServices.getAllStimuliAround(getPosition());		
		lastPercievedMap = s;

		if(currentTask != null)
		{
			if(currentTask.checkInterrupt(s))
			{
				currentTask.interrupt();
				this.addToEnergy(-0.01);
			}
			else
			{
				this.addToEnergy(-currentTask.energyCost);
				currentTask.execute();
			}
		}
		
		if(currentTask == null) //we need to do that even if we got in the previous if (after each execute current task might be null)
		{			
			chooseNewTask(s);
			this.addToEnergy(-0.01);
		}
	}
	
	protected Task findATask(StimuliMap load)
	{
		Task todo = taskList.get(0);
		double taskScore = todo.checkInterrupt(load) ? 0 : todo.compute(load);
		
		for(int ti = 1; ti < taskList.size(); ++ti)
		{
			Task current = taskList.get(ti);
			double currentScore = current.checkInterrupt(load) ? 0 : current.compute(load); //Cannot choose a task that fulfills its interrupt 
			if(currentScore > taskScore)
			{
				todo = current;
				taskScore = currentScore;
			}
		}
		
		return todo;
	}
	
	protected void chooseNewTask(StimuliMap load)
	{
		currentTask = findATask(load);
		controllerServices.logMyTaskSwitch(currentTask, this.ID);
	}
	
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
	public StimuliLoad getStimuliLoad() {return this.stimuliLoad;}
	
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
