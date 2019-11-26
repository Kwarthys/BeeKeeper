package com.beekeeper.model.tasks.beetasks;

import java.util.ArrayList;
import java.util.Collections;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;

public class GiveFoodTask extends Task {

	public GiveFoodTask(WorkingAgentServices agentServices) {
		super(agentServices, "Give Food");

		this.motivated = false;

		this.rootActivity.addTaskNode(new Action(2,0, agentServices) {

			@Override
			public Action execute() {
				WorkingAgent cooperativeInteractor = agentServices.getCoopInteractor();
				CombCell hostCell = agentServices.getHostCell();
				if(cooperativeInteractor == null)
				{
					ArrayList<WorkingAgent> neighs = hostCell.getNeighborBees();
					Collections.shuffle(neighs);

					if(neighs.size()==0)
					{
						agentServices.randomMove();
						//System.out.println("Not a single bee around");
						return this;
					}
					//System.out.println(ID + "-" + neighs.get(0).ID + " " + neighs.get(0).isHungry());
					if(neighs.get(0).isHungry())
					{
						cooperativeInteractor = neighs.get(0);
						//System.out.println("found a hungryman");
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

				return this;
			}

			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return this.thresholdSigmoid(smap.getAmount(Stimulus.AskFood));
	}

}
