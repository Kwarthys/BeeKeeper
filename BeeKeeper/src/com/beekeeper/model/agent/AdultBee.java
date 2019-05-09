package com.beekeeper.model.agent;

import java.awt.geom.Point2D;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;

public class AdultBee extends EmptyBee
{
	private boolean randomWalking = false;
	
	public AdultBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		this(stimuliManagerServices, controllerServices, 200+Math.random()*100, 200+Math.random()*100);
	}
		
	public AdultBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, double x, double y)
	{
		super(stimuliManagerServices, x,y);
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
				randomWalking = true;
				//this.interrupt();
				StimuliMap s = stimuliManagerServices.getAllStimuliAround(position);
				Task detectedTask = forageForWork(s, false);
				if(detectedTask.compute(s) > currentTask.compute(s))
				{
					this.interrupt();
				}
			}

			@Override
			public double compute(StimuliMap load) {
				return AdultBee.this.getEnergy() > 0.5 ? 0.7:0;
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
		randomMoveTask.taskName = "Random Walk";

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

		idleTask.energyCost = -0.01;
		idleTask.taskName = "Rest";

		taskList.add(idleTask);

		Task feedLarvaeTask = new Task() {

			private BroodBee targetLarvae = null;
			private CombCell targetFood = null;
			
			private boolean gettingFood = true;

			@Override
			public void execute() {
				taskDuration++;
				if(gettingFood)
				{
					gettingFood = AdultBee.this.stomach < 0.9;					
				}
				else
				{
					gettingFood = AdultBee.this.stomach < 0.1;
				}
				
				if(!gettingFood)
				{

					Point2D.Double targetpos = AdultBee.this.stimuliManagerServices.getPosOfStrongestEmitter(getPosition(), Stimulus.HungryLarvae);
					if(targetpos == null)
					{
						this.interrupt();
						return;
					}
					AdultBee.this.target = targetpos;
					targetLarvae = controllerServices.getLarvaeByPos(targetpos, combID);
					if(targetLarvae == null)
					{
						System.err.println("Missing larvae");
						return;
					}

					if(targetpos.distance(getPosition()) < 0.1)
					{
						if(targetLarvae.isHungry())
						{
							targetLarvae.receiveFood(0.5);
							AdultBee.this.stomach -= 0.1;
						}
						else
						{
							this.interrupt();
						}
					}
					else
					{
						AdultBee.this.moveTowards(targetpos);
					}
				}
				else
				{
					Point2D.Double targetpos = AdultBee.this.stimuliManagerServices.getPosOfStrongestEmitter(getPosition(), Stimulus.FoodSmell);
					if(targetpos == null)
					{
						this.interrupt();
						return;
					}
					AdultBee.this.target = targetpos;
					targetFood = controllerServices.getCellByPos(targetpos, combID);

					if(targetpos.distance(getPosition()) < 0.1)
					{
						AdultBee.this.stomach += targetFood.takeFood(Math.min(1-AdultBee.this.stomach, 0.1));
						AdultBee.this.addToEnergy(1);
					}
					else
					{
						AdultBee.this.moveTowards(targetpos);
					}
				}

			}

			@Override
			public double compute(StimuliMap load) {
				return this.thresholdSigmoid(load.getAmount(Stimulus.HungryLarvae));
			}

			@Override
			public void interrupt() {
				targetLarvae = null;
				AdultBee.this.target = null;
				currentTask = null;
				this.learn(taskDuration);
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				if(gettingFood)
				{
					return AdultBee.this.getEnergy() < 0.3;
				}
				else
				{
					return AdultBee.this.getEnergy() < 0.3 || load.getAmount(Stimulus.HungryLarvae) < 1;					
				}
			}
		};	

		feedLarvaeTask.energyCost = 0.001;
		feedLarvaeTask.taskName = "Feed Larvae";

		taskList.add(feedLarvaeTask);
	}

	protected void moveTowards(Point2D.Double position)
	{
		double speed = 1;
		
		double dx = position.getX() - this.position.getX();
		double dy = position.getY() - this.position.getY();
		
		Point2D.Double vector = new Point2D.Double(dx,dy);
		double norme = vector.distance(new Point2D.Double(0,0));

		vector.x = vector.getX() / norme * Math.min(speed, norme);
		vector.y = vector.getY() / norme * Math.min(speed, norme);

		move(vector.x, vector.y);
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
	
	private Task forageForWork(StimuliMap load, boolean log)
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

		//System.out.println(this.ID + " chose task " + todo.taskName + " sensing " + load.getAmount(Stimulus.HungryLarvae) + "HL with energy at " + getEnergy());
		
		if(!(todo.taskName.equals("Random Walk") && randomWalking) && log)
			controllerServices.logMyTaskSwitch(todo, this.ID);
		
		randomWalking = todo.taskName.equals("Random Walk");
		
		taskDuration=0;
		
		return todo;
	}

	private Task forageForWork(StimuliMap load)
	{
		return forageForWork(load, true);
	}
}
