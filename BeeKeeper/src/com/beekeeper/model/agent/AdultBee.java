package com.beekeeper.model.agent;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;

public class AdultBee extends EmptyBee
{
	protected ArrayList<Task> taskList = new ArrayList<>();	

	protected Task currentTask = null;

	public AdultBee(StimuliManagerServices stimuliManagerServices)
	{
		super(stimuliManagerServices);
		fillTaskList();
		this.position = new Point2D.Double(Math.random()*600, Math.random()*600);
		
		this.type = BeeType.ADULT_BEE;
	}

	@Override
	protected void fillTaskList()
	{
		Task randomMoveTask = new Task() {			
			@Override
			public void execute() {
				AdultBee.this.randomMove();
			}

			@Override
			public double compute(StimuliLoad load) {
				return load.beeEnergy;
			}

			@Override
			public void interrupt() {
				currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliLoad load) {
				return load.beeEnergy < 0.1;
			}
		};	

		randomMoveTask.energyCost = 0.02;

		taskList.add(randomMoveTask);


		Task idleTask = new Task() {			
			@Override
			public void execute() {
				//do nothing
			}

			@Override
			public double compute(StimuliLoad load) {
				return 1-load.beeEnergy;
			}

			@Override
			public void interrupt() {
				currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliLoad load) {
				return load.beeEnergy > 0.7;
			}
		};	

		idleTask.energyCost = -0.05;

		taskList.add(idleTask);
	}

	public void randomMove()
	{
		double speed = 1.0;
		this.move((Math.random()*2-1*speed), (Math.random()*2-1)*speed);
	}

	@Override
	public void live() {
		
		if(currentTask == null)
		{
			currentTask = forageForWork();
		}
		else if(currentTask.checkInterrupt(getStimuliLoad()))
		{
			currentTask.interrupt();
		}
		else
		{
			this.addToEnergy(-currentTask.energyCost);
			currentTask.execute();
		}
	}

	@Override
	public void move(double dx, double dy) {
		this.position.setLocation(this.position.getX() + dx, this.position.getY() + dy);	
	}

	private StimuliLoad getStimuliLoad()
	{
		return new StimuliLoad(this.pheromoneLoad, this.getEnergy());
	}

	private Task forageForWork()
	{
		StimuliLoad load = getStimuliLoad();

		Task todo = taskList.get(0);
		double taskScore = todo.compute(load);

		for(int ti = 1; ti < taskList.size(); ++ti)
		{
			Task current = taskList.get(ti);
			double currentScore = current.compute(load); 
			if(currentScore > taskScore)
			{
				todo = current;
				taskScore = currentScore;
			}
		}

		return todo;
	}
}
