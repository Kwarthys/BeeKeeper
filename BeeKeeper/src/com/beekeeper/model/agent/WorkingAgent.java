package com.beekeeper.model.agent;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;

public abstract class WorkingAgent extends EmitterAgent
{
	protected ArrayList<Task> taskList = new ArrayList<>();
	protected abstract void fillTaskList();
	
	protected MainControllerServices controllerServices;
	protected double stomach;
	
	protected StimuliMap lastPercievedMap;
	
	protected Task currentTask = null;
	
	public Point2D.Double target = null;
	
	public Task getCurrentTask() {return currentTask;}
	
	public void interruptTask() {currentTask = null;}
	
	public WorkingAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		super(stimuliManagerServices);
		this.controllerServices = controllerServices;
		setEnergy(Math.random()*0.8+0.2);
	}
	
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
		
		StimuliMap s = stimuliManagerServices.getAllStimuliAround(new Point(hostCell.x, hostCell.y));
		//System.out.println(s.getDisplayString());
		lastPercievedMap = s;
		
		
		//System.out.println(ID + " living ! " + s.getAmount(Stimulus.HungryLarvae));

		chooseNewTask(s).execute();
	}

	public Task findATask(StimuliMap load)
	{		
		Task todo = taskList.get(0);
		double taskScore = todo.compute(load);
		
		for(int ti = 1; ti < taskList.size(); ++ti)
		{
			Task current = taskList.get(ti);
			double currentScore = current.compute(load);
			//if(currentScore != 0)
				//System.out.println(current.taskName + " " + currentScore + " t:" + current.threshold + "\n" + load.getDisplayString());
			if(currentScore > taskScore)
			{
				todo = current;
				taskScore = currentScore;
			}
		}
		
		return todo;
	}
	
	protected Task chooseNewTask(StimuliMap load)
	{
		currentTask = findATask(load);
		controllerServices.logMyTaskSwitch(currentTask, this.ID);
		
		return currentTask;
	}
	
	public void receiveFood(double amount)
	{
		this.addToEnergy(amount);
		//System.out.println(ID + " receiving food to " + energy);
	}
	
	public boolean isHungry()
	{
		return getEnergy() < 0.8;
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
	
	public HashMap<String, Double> getAllPrintableThresholds()
	{
		HashMap<String, Double> ts = new HashMap<String, Double>();
		
		for(Task t : taskList)
		{
			if(t.printLearning)
				ts.put(t.taskName, t.threshold);
		}
		
		return ts;
	}
	
	/*
	protected void learnTask(Task toLearn)
	{
		for(Task t : taskList)
		{
			if(t == toLearn)
			{
				t.learn();
				//System.out.println(this.ID + " Learning " + t.taskName);
			}
			else
			{
				t.forget();
			}
		}
	}
	*/

	public StimuliMap getPercievedStimuli() {
		return lastPercievedMap;
	}
}
