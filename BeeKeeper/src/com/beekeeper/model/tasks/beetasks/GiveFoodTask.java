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

		this.threshold = 3;
		
		this.motivated = false;
		
		
		this.rootActivity.addTaskNode(new Action(2,0,agentServices) {
			
			@Override
			public void execute() {
				WorkingAgent cooperativeInteractor = agentServices.getCoopInteractor();
				if(cooperativeInteractor.isHungry())
				{
					/*
					if(cooperativeInteractor.getCombId() != agentServices.getCombId())
					{
						System.out.println("Food Transfer between comb" + cooperativeInteractor.getCombId() + " and comb" + agentServices.getCombId());
					}
					*/
					cooperativeInteractor.recieveFood();
				}
				else
				{
					cooperativeInteractor = null;
				}
			}
			
			@Override
			public boolean check() {
				return agentServices.getCoopInteractor() != null;
			}
		});

		this.rootActivity.addTaskNode(new Action(0.2,0, agentServices) {

			@Override
			public void execute() {
				CombCell hostCell = agentServices.getHostCell();
				ArrayList<WorkingAgent> neighs = hostCell.getNeighborBees();
				Collections.shuffle(neighs);

				if(neighs.size()==0)
				{
					agentServices.randomMove();
					//System.out.println("Not a single bee around");
					return;
				}
				//System.out.println(ID + "-" + neighs.get(0).ID + " " + neighs.get(0).isHungry());
				boolean found = false;
				for(int i = 0; i<neighs.size() && !found; ++i)
				{
					WorkingAgent a = neighs.get(i);
					if(a.isHungry())
					{
						agentServices.setInteractorTo(a);
						found = true;
						//System.out.println("found a hungryman");
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
