package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.beetasks.AskFoodTask;
import com.beekeeper.model.tasks.beetasks.FeedLarva;
import com.beekeeper.model.tasks.beetasks.ForagerTask;
import com.beekeeper.model.tasks.beetasks.GiveFoodTask;
import com.beekeeper.model.tasks.generaltasks.RandomMoveTask;
import com.beekeeper.model.tasks.generaltasks.RestTask;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.utils.MyUtils;

public class AdultBee extends WorkingAgent
{		
	public AdultBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		super(stimuliManagerServices, controllerServices, true);
		this.type = AgentType.ADULT_BEE;
		
		this.currentTask = taskList.get(0);
	}

	public AdultBee(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, boolean randomInit)
	{
		super(stimuliManagerServices, controllerServices, randomInit);
	}

	@Override
	protected void initPhysiology(boolean randomInit)
	{
		if(randomInit)
		{			
			hjTiter = ModelParameters.getStartingBeeHJTiter();//Math.random() * Math.random() * Math.random();
			age = ModelParameters.getAgeFromStartingHJ(hjTiter);
		}
		else
		{
			hjTiter = 0;
			age = 0;
		}
		
		this.bodySmell.addAmount(Stimulus.EthyleOleate, ModelParameters.EO_EQUILIBRIUM);
	}

	@Override
	protected void fillTaskList()
	{
		taskList.add(new RestTask(this.ownServices));
		taskList.add(new AskFoodTask(this.ownServices));
		taskList.add(new GiveFoodTask(this.ownServices));
		taskList.add(new ForagerTask(this.ownServices));
		taskList.add(new FeedLarva(this.ownServices));
		taskList.add(new RandomMoveTask(this.ownServices));
	}

	@Override
	protected void advanceMetabolism()
	{		
		if(currentTask != null)
		{
			if(currentTask.taskName == ForagerTask.foragingTaskName && ModelParameters.FORAGERS_DIE_SOONER)
			{
				activeAge += ModelParameters.foragingAgePenalty;
			}
			else
			{
				activeAge++;				
			}
		}
		
		if(activeAge > ModelParameters.maxTimestepAge)
		{
			alive = false; //death from old age
			//System.out.println("DEATH FROM OLD AGE at " + age + " s.");
		}
		
		if(isInside())
		{
			if(hostCell.content == CellContent.brood)
			{
				//System.out.println("Contact by proximity with larvae");
				spreadByContact(hostCell.inside);
			}
		}
		else
		{
			//Outside bees consume resource they gather
			hunger = 0;
		}
		
		hunger = MyUtils.clamp(hunger + ModelParameters.HUNGER_INCREMENT);
		
		if(!ModelParameters.BYPASS_PHYSIOLOGY)
		{
			hjTiter += ModelParameters.HJ_INCREMENT;
			
			
			hjTiter -= ModelParameters.getHJModifiedByEthyleOleate(this.lastPercievedMap.getAmount(Stimulus.EthyleOleate));
			hjTiter = MyUtils.clamp(hjTiter);
			
			this.bodySmell.addAmount(Stimulus.EthyleOleate, ModelParameters.getEthyleOleateEmitedByHJ(hjTiter));
		}
		
		receivingFood = false;		
		
		maxHungerFelt = Math.max(maxHungerFelt, lastPercievedMap.getAmount(Stimulus.AskFood));
		
		//if(getID() == 1000)
		//{
		//	System.out.println("maxHungerFelt: " + maxHungerFelt + " percepted AskFood:" + lastPercievedMap.getAmount(Stimulus.AskFood) + " own Hunger:" + getHunger());
		//	System.out.println(currentTask.taskName + " " + (double)((long)(motivation*100))/100);
		//}
	}
	
	public double maxHungerFelt = 0; //tracking DEBUG

	@Override
	public String getStringName() {
		return "AdultBee " + ID;
	}
}
