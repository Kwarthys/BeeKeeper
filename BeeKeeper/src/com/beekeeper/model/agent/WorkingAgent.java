package com.beekeeper.model.agent;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public abstract class WorkingAgent extends EmitterAgent
{
	protected ArrayList<Task> taskList = new ArrayList<>();
	protected abstract void fillTaskList();
	
	protected MainControllerServices controllerServices;
	protected double hunger = Math.random() * 0.5;
	private boolean receivingFood = false;
	
	protected StimuliMap lastPercievedMap;
	
	protected Task currentTask = null;
	protected Action currentAction = null;
	
	protected WorkingAgent cooperativeInteractor = null;
	
	public Point2D.Double target = null;
	
	public Task getCurrentTask() {return currentTask;}
	
	public void interruptTask() {currentTask = null;}
	
	protected double motivation = 1;
	public double getMotivation() {return motivation;}
	
	protected WorkingAgentServices ownServices = new WorkingAgentServices() {		
		@Override
		public double getEnergy() {
			return WorkingAgent.this.getEnergy();
		}
		
		@Override
		public void emit(Stimulus smell, double amount) {
			WorkingAgent.this.emit(smell, amount);
		}
		
		@Override
		public void addToEnergy(double amount) {
			WorkingAgent.this.addToEnergy(amount);
		}

		@Override
		public void randomMove() {
			WorkingAgent.this.randomMove();			
		}

		@Override
		public void dropMotivation() {
			WorkingAgent.this.motivation = Math.max(0, WorkingAgent.this.motivation - ModelParameters.MOTIVATION_STEP);
		}

		@Override
		public int getID() {
			return WorkingAgent.this.getID();
		}

		@Override
		public StimuliMap getLastPerception() {
			return lastPercievedMap;
		}

		@Override
		public double getHunger() {
			return WorkingAgent.this.hunger;
		}

		@Override
		public void giveFoodToClosestHungry() {
			if(cooperativeInteractor == null)
			{
				ArrayList<WorkingAgent> neighs = hostCell.getNeighborBees();
				Collections.shuffle(neighs);
				
				if(neighs.size()==0)
				{
					randomMove();
					System.out.println("Not a single bee around");
					return;
				}
				//System.out.println(ID + "-" + neighs.get(0).ID + " " + neighs.get(0).isHungry());
				if(neighs.get(0).isHungry())
				{
					cooperativeInteractor = neighs.get(0);
					System.out.println("found a hungryman");
				}
			}
			
			if(cooperativeInteractor != null)
			{
				if(cooperativeInteractor.isHungry())
				{
					cooperativeInteractor.recieveFood();					
				}
				else
				{
					cooperativeInteractor = null;
				}
			}
		}

		@Override
		public boolean isReceivingFood() {
			return WorkingAgent.this.receivingFood;
		}
	};
	
	public WorkingAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		super(stimuliManagerServices);
		this.controllerServices = controllerServices;
		setEnergy(Math.random()*0.8+0.2);
		fillTaskList();
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
		
		hunger += 0.001;
		
		StimuliMap s = stimuliManagerServices.getAllStimuliAround(new Point(hostCell.x, hostCell.y));
		//System.out.println(s.getDisplayString());
		lastPercievedMap = s;
		
		
		//System.out.println(ID + " living ! " + s.getAmount(Stimulus.HungryLarvae));

		if(currentAction == null)
		{
			currentAction = chooseNewTask(s).execute();		
			//System.out.println("Executing new Action");
		}
		else
		{
			currentAction.run();			
		}
		
		
		//If action is over, remove it
		if(currentAction.isOver())
		{
			//System.out.println("Action done");
			currentAction = null;
		}
	}
	
	
	public boolean isHungry() {return hunger > 0.5 && currentTask.taskName == "Asking Food";}
	
	public void recieveFood()
	{
		hunger-= 0.4;
		hunger = Math.max(0, hunger);
		
		receivingFood = true;
		
		System.out.println(ID + " receiving food, hunger: " + hunger);
	}

	public Task findATask(StimuliMap load)
	{		
		Task todo = null;
		double taskScore = -1;
		
		for(int ti = 0; ti < taskList.size(); ++ti)
		{
			Task current = taskList.get(ti);
			double currentScore = 0;
			
			if(current == currentTask)
			{
				currentScore = motivation;
			}
			else
			{				
				currentScore = current.compute(load);
			}
			
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
		Task newTask = findATask(load);
		if(newTask != currentTask)
		{
			motivation = 1.0;
		}
		currentTask = newTask;
		controllerServices.logMyTaskSwitch(currentTask, this.ID);
		
		return currentTask;
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
	
	public ArrayList<Double> getAllTaskScores()
	{
		ArrayList<Double> d = new ArrayList<>();
		for(Task t : taskList)
		{
			d.add(t.compute(lastPercievedMap));
		}
		return d;
	}

	public StimuliMap getPercievedStimuli() {
		return lastPercievedMap;
	}
}
