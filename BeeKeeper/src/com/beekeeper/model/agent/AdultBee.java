package com.beekeeper.model.agent;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;

public class AdultBee extends EmptyBee
{
	protected ArrayList<Task> taskList = new ArrayList<>();	

	protected Task currentTask = null;

	public AdultBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		super(stimuliManagerServices);
		fillTaskList();
		this.controllerServices = controllerServices;	
		this.stimuliLoad = new StimuliLoad(this.position);
		this.type = BeeType.ADULT_BEE;		
	}

	@Override
	protected void fillTaskList()
	{
		Task randomMoveTask = new Task() {			
			@Override
			public void execute() {
				//System.out.println("random walk");
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

		randomMoveTask.energyCost = 0.01;

		taskList.add(randomMoveTask);


		Task idleTask = new Task() {			
			@Override
			public void execute() {
				//System.out.println("Resting");
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

			private BroodBee target = null;

			@Override
			public void execute() {				
				if(target == null)
				{
					Point2D.Double targetpos = AdultBee.this.stimuliManagerServices.getPosOfStrongestEmitter(getPosition(), Stimulus.HungryLarvae);
					target = controllerServices.getLarvaeByPos(targetpos);
				}

				if(target.getPosition().distance(getPosition()) < 0.1)
				{
					if(target.isHungry())
					{
						target.receiveFood(0.3);
					}
					else
					{
						this.interrupt();
					}
				}
				else
				{
					AdultBee.this.moveTowards(target.getPosition());
				}
			}

			@Override
			public double compute(StimuliMap load) {
				return load.getAmount(Stimulus.HungryLarvae);
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

	protected void moveTowards(Point2D.Double position)
	{
		double speed = 0.5;
		
		double dx = position.getX() - this.position.getX();
		double dy = position.getY() - this.position.getY();

		dx = dx > speed ? speed : dx < -speed ? -speed : dx;
		dy = dy > speed ? speed : dy < -speed ? -speed : dy;

		move(dx, dy);
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
		double taskScore = todo.checkInterrupt(load) ? 0 : todo.compute(load);

		//System.out.println(this.ID + " sensing " + load.getAmount(Stimuli.HungryLarvae) + ", energy at " + getEnergy());

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
