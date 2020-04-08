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
		
		//Feeding
		this.rootActivity.addTaskNode(new Action(0.5,0,agentServices) {
			
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
					//System.out.println("FT " + cooperativeInteractor.getID() + " on C" + cooperativeInteractor.getCombId() + " and comb" + agentServices.getID() + " on C" + agentServices.getCombId());
					cooperativeInteractor.recieveFood();
					
					if(agentServices.getID() % 500 == 0)
					{
						System.out.println(agentServices.getID() + " fed hungryman ");
					}
				}
				else
				{					
					if(agentServices.getID() % 500 == 0)
					{
						System.out.println(agentServices.getID() + " hungryman not hungry ");
					}
					
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
				
				if(agentServices.getID() % 500 == 0)
				{
					System.out.println(agentServices.getID() + " looking for a hungryman ");
				}
				
				if(neighs.size()==0)
				{
					agentServices.randomMove();
					//System.out.println("Not a single bee around");
					return;
				}
				Collections.shuffle(neighs);
				//System.out.println(agentServices.getID() + " on C" + agentServices.getCombId() + ": " + neighs.size());
				boolean found = false;
				for(int i = 0; i<neighs.size() && !found; ++i)
				{
					WorkingAgent a = neighs.get(i);
					if(a.isHungry())
					{
						agentServices.setInteractorTo(a);
						found = true;
						
						//System.out.println("found a hungryman");
						
						if(agentServices.getID() % 500 == 0)
						{
							System.out.println(agentServices.getID() + " found a hungryman ");
						}
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
