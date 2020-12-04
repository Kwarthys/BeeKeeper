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
import com.beekeeper.parameters.ModelParameters;

public class GiveFoodTask extends Task {
	
	public static final String giveFoodTaskName = "GiveFood";

	public GiveFoodTask(WorkingAgentServices agentServices) {
		super(agentServices, giveFoodTaskName);

		this.threshold = 3;
		
		this.motivated = false;
		
		//Feeding
		this.rootActivity.addTaskNode(new Action(ModelParameters.WORKER_FEEDING_MEANDURATION,0,agentServices) {
			
			@Override
			public void execute() {
				WorkingAgent cooperativeInteractor = agentServices.getCoopInteractor();
				if(cooperativeInteractor.isHungry())
				{
					cooperativeInteractor.recieveFood();
				}
				else
				{					
					agentServices.resetCoopInteractor();
				}
			}
			
			@Override
			public boolean check() {
				return agentServices.getCoopInteractor() != null;
			}
		});

		//Finding someone to feed
		this.rootActivity.addTaskNode(new Action(0.2,0, agentServices) {
			@Override
			public void execute() {
				CombCell hostCell = agentServices.getHostCell();
				ArrayList<WorkingAgent> neighs = hostCell.getNeighborBees();
				
				if(neighs.size()==0)
				{
					agentServices.randomMove();
					//System.out.println("Not a single bee around");
					return;
				}
				Collections.shuffle(neighs);
				boolean found = false;
				for(int i = 0; i<neighs.size() && !found; ++i)
				{
					WorkingAgent a = neighs.get(i);
					if(a.isHungry())
					{
						agentServices.setInteractorTo(a);
						found = true;
					}					
				}
				
				if(agentServices.getCoopInteractor() == null)
				{
					agentServices.dropMotivation();
				}
				
			}

			@Override
			public boolean check() {
				return true;
			}
		});
	}

	@Override
	public double compute(StimuliMap smap) {
		return this.agentServices.isHungry() ? 0 : this.thresholdSigmoid(smap.getAmount(Stimulus.AskFood));
	}

}
