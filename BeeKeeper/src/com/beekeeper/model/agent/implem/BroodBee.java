package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.beetasks.EggTask;
import com.beekeeper.model.tasks.beetasks.LarvaTask;
import com.beekeeper.model.tasks.beetasks.NympheaTask;
import com.beekeeper.parameters.ModelParameters;

public class BroodBee extends WorkingAgent
{	
	public static enum LarvalState{ Egg, Larva, Nymphea, None};
	
	@Override
	public LarvalState getLarvalState()
	{
		if(!ModelParameters.LARVA_CAN_HATCH)
		{
			return LarvalState.Larva;
		}
		
		if(age < ModelParameters.larvaEggUntilAge)
		{
			return LarvalState.Egg;
		}
		else if(age < ModelParameters.larvaLarvaUntilAge)
		{
			return LarvalState.Larva;
		}
		else
		{
			return LarvalState.Nymphea;
		}
	}
	
	private int cantBeFed = 0;
	
	private boolean requireFood = false;
	
	public BroodBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, boolean randomInit) {
		super(stimuliManagerServices, controllerServices, randomInit);
		this.type = AgentType.BROOD_BEE;
		if(this.getEnergy() < 0.5)
		{
			this.addToEnergy(0.5);
		}
	}
	
	public BroodBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices) {
		this(stimuliManagerServices, controllerServices, true);
	}


	@Override
	protected void initPhysiology(boolean randomInit)
	{
		if(randomInit)
		{
			double state = Math.random();
			if(state < .33)
			{
				age = (int) (Math.random() * ModelParameters.larvaEggUntilAge);
			}
			else if(state < .66)
			{
				age = ModelParameters.larvaEggUntilAge + (int) (Math.random() * (ModelParameters.larvaLarvaUntilAge-ModelParameters.larvaEggUntilAge));
			}
			else
			{
				age = ModelParameters.larvaLarvaUntilAge + (int) (Math.random() * (ModelParameters.larvaNympheaUntilAge - ModelParameters.larvaLarvaUntilAge));				
			}
		}
		else
		{
			age = 0;
		}
		
		this.bodySmell.addAmount(Stimulus.EthyleOleate, ModelParameters.LARVA_EO_TIMELY_EMMISION);
	}
	
	@Override
	public boolean isHungry() {
		return this.getEnergy() < 0.8 && cantBeFed == 0 && requireFood;
	}
	
	@Override
	public void recieveFood() {
		//if(getID() == 0)System.out.println(getStringName() + " got fed : " + getEnergy());
		this.addToEnergy(ModelParameters.LARVAE_FEEDING_QUANTITY);
		//System.out.println(" to " + getEnergy());
		cantBeFed = ModelParameters.MIN_DURATION_LARVAEFEDAGAIN;
	}

	@Override
	protected void fillTaskList() 
	{
		this.taskList.add(new LarvaTask(ownServices));
		this.taskList.add(new NympheaTask(ownServices));
		this.taskList.add(new EggTask(ownServices));
	}

	@Override
	protected void advanceMetabolism()
	{
		if(cantBeFed > 0)
		{
			cantBeFed--;
		}
		
		//if(getID() == 0)System.out.println(getStringName() + " " + getEnergy() + " - " + ModelParameters.LARVAE_HUNGER_INCREMENT + " + " + ModelParameters.LARVAE_FEEDING_INCREMENT);
		//this.bodySmell.addAmount(Stimulus.EthyleOleate, ModelParameters.LARVA_EO_TIMELY_EMMISION);//Moved in larva task
		
		if(getLarvalState() == LarvalState.Larva)
		{
			requireFood = true;
		}
		else
		{
			requireFood = false;
		}
		
		if(!ModelParameters.LARVA_CAN_HATCH)
		{
			age--; //brood not aging : preventing them to hatch while keeping the brood diversity
		}
		
		if(age > ModelParameters.timestepLarvaPop && ModelParameters.LARVA_CAN_HATCH)
		{
			//larva old enough to become a bee
			if(hostCell.visiting == null)
			{
				//room for hatching, a bee is born
				
				//kill larva
				this.alive = false;
				
				//spawn bee
				controllerServices.spawnBeeAt(hostCell.getCombID(), getPosition());
				
				//System.out.println("A BEE IS BORN " + age + " " + ModelParameters.timestepLarvaPop);
			}
		}
	}

	@Override
	public String getStringName() {
		return "BroodBee " + ID;
	}
}
