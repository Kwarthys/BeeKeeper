package com.beekeeper.model.agent.implem;

import java.awt.geom.Point2D;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.model.tasks.generaltasks.RandomMoveTask;
import com.beekeeper.model.tasks.generaltasks.RestTask;
import com.beekeeper.parameters.ModelParameters;

public class AdultBee extends WorkingAgent
{	
	public AdultBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		this(stimuliManagerServices, controllerServices, 200+Math.random()*100, 200+Math.random()*100);
	}
		
	public AdultBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, double x, double y)
	{
		super(stimuliManagerServices, controllerServices, x,y);
		fillTaskList();
		this.type = AgentType.ADULT_BEE;		
	}

	@Override
	protected void fillTaskList()
	{
		taskList.add(new RandomMoveTask(this));

		taskList.add(new RestTask(this));

		Task feedLarvaeTask = new Task() {

			private BroodBee targetLarvae = null;
			private FoodSource targetFood = null;
			
			private boolean gettingFood = true;

			@Override
			public void execute() {
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

					Point2D.Double targetpos = null;//TODO AdultBee.this.stimuliManagerServices.getPosOfStrongestEmitter(getPosition(), Stimulus.HungryLarvae);
					if(targetpos == null)
					{
						this.interrupt();
						return;
					}
					AdultBee.this.target = targetpos;

					if(targetpos.distance(getPosition()) < 0.1)
					{
						targetLarvae = controllerServices.getLarvaeByPos(targetpos, combID);
						
						if(targetLarvae == null)
						{
							//System.err.println("No larvae there");
							return;
						}
						
						if(targetLarvae.isHungry())
						{
							targetLarvae.receiveFood(0.5);
							AdultBee.this.stomach -= 0.1;
							
							//TODO stimuliLoad.contact(targetLarvae.getStimuliLoad());
						}
					}
					else
					{
						AdultBee.this.moveTowards(targetpos);
					}
				}
				else
				{
					Point2D.Double targetpos = null; //TODO AdultBee.this.stimuliManagerServices.getPosOfStrongestEmitter(getPosition(), Stimulus.FoodSmell);
					if(targetpos == null)
					{
						this.interrupt();
						System.err.println("Interrupt caused by Food Not Found");
						return;
					}
					AdultBee.this.target = targetpos;
					targetFood = controllerServices.getFoodSourceByPos(targetpos, combID);
					if(targetFood == null)
					{
						System.err.println("No food " + combID);
						return;
					}

					if(targetpos.distance(getPosition()) < 0.1)
					{
						AdultBee.this.stomach += targetFood.takeFood(Math.min(1-AdultBee.this.stomach, 0.5));
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
				gettingFood = false;
			}

			@Override
			public boolean checkInterrupt(StimuliMap load) {
				if(gettingFood)
				{
					return AdultBee.this.getEnergy() < 0.3 || load.getAmount(Stimulus.HungryLarvae) == 0;
				}
				else
				{
					return AdultBee.this.getEnergy() < 0.2 || load.getAmount(Stimulus.HungryLarvae) < 1;					
				}
			}
		};	

		feedLarvaeTask.energyCost = 0.001;
		feedLarvaeTask.taskName = "Feed Larvae";
		feedLarvaeTask.printLearning = true;
		feedLarvaeTask.threshold = Math.random() * ModelParameters.MAX_TASK_THRESHOLD;

		taskList.add(feedLarvaeTask);
	}
}
