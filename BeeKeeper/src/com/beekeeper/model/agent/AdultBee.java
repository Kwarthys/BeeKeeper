package com.beekeeper.model.agent;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.stimuli.Stimuli;
import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.StimuliMap;
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
		this.position = new Point2D.Double(Math.random()*6, Math.random()*6);
		
		this.type = BeeType.ADULT_BEE;
		
		this.stimuliLoad = new StimuliLoad(this.position);
	}

	@Override
	protected void fillTaskList()
	{
		Task randomMoveTask = new Task() {			
			@Override
			public void execute() {
				System.out.println("random walk");
				AdultBee.this.randomMove();
			}

			@Override
			public double compute(StimuliMap load) {
				return AdultBee.this.getEnergy() > 0.8 ? 1:0;
			}

			@Override
			public void interrupt() {
				currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				return AdultBee.this.getEnergy() < 0.1;
			}
		};	

		randomMoveTask.energyCost = 0.02;

		taskList.add(randomMoveTask);


		Task idleTask = new Task() {			
			@Override
			public void execute() {
				System.out.println("Resting");
			}

			@Override
			public double compute(StimuliMap load) {
				return AdultBee.this.getEnergy() < 0.2 ? 1:0;
			}

			@Override
			public void interrupt() {
				currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				return AdultBee.this.getEnergy() > 0.9;
			}
		};	

		idleTask.energyCost = -0.05;

		taskList.add(idleTask);
		
		Task feedLarvaeTask = new Task() {			
			@Override
			public void execute() {
				//TODO run towards larvae and feed it
				System.out.println("Executing LarvaeFeeding");
			}

			@Override
			public double compute(StimuliMap load) {
				return load.getAmount(Stimuli.HungryLarvae);
			}

			@Override
			public void interrupt() {
				currentTask = null;
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				return AdultBee.this.getEnergy() < 0.2;
			}
		};	

		feedLarvaeTask.energyCost = 0.05;

		taskList.add(feedLarvaeTask);
	}

	public void randomMove()
	{
		double speed = 1.0;
		this.move((Math.random()*2-1*speed), (Math.random()*2-1)*speed);
	}

	@Override
	public void live() {
		
		StimuliMap s = stimuliManagerServices.getAllStimuliAround(getPosition());
		
		
		if(currentTask == null)
		{
			currentTask = forageForWork(s);
			this.addToEnergy(-0.01);
		}
		else if(currentTask.checkInterrupt(s))
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

	@Override
	public void move(double dx, double dy) {
		this.position.setLocation(this.position.getX() + dx, this.position.getY() + dy);	
	}

	private Task forageForWork(StimuliMap load)
	{
		Task todo = taskList.get(0);
		double taskScore = todo.compute(load);
		
		System.out.println(this.ID + " sensing " + load.getAmount(Stimuli.HungryLarvae) + ", energy at " + getEnergy());

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
}
