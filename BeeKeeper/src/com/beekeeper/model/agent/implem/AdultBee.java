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
		super(stimuliManagerServices, controllerServices);
		this.type = AgentType.ADULT_BEE;		
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
		if(isInside())
		{
			if(hostCell.content == CellContent.brood)
			{
				//System.out.println("Contact by proximity with larvae");
				spreadByContact(hostCell.inside);
			}
		}		
		
		hjTiter += 0.0005;
		hjTiter -= ModelParameters.getHJModifiedByOcimene(this.bodySmell.getAmount(Stimulus.Ocimene));
		hjTiter = MyUtils.clamp(hjTiter);
		
		hunger = Math.min(1, hunger + 0.001);
		
		
		
		receivingFood = false;
		
		this.bodySmell.addAmount(Stimulus.Ocimene, ModelParameters.getOcimeneEmitedByHJ(hjTiter));
		
	}

	@Override
	public String getStringName() {
		return "AdultBee " + ID;
	}
}
