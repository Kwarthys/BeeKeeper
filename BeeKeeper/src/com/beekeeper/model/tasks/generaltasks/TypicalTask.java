package com.beekeeper.model.tasks.generaltasks;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map.Entry;

import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.utils.MyUtils;

public class TypicalTask extends Task {
	
	protected WorkingAgent agent;
	protected HashMap<Stimulus, Double> inputs;
	protected AgentType targetType;
	protected Stimulus targetSmell;
	
	protected EmitterAgent target;
	
	public TypicalTask(WorkingAgent agent, HashMap<Stimulus, Double> inputs, AgentType target, Stimulus targetSmell)
	{
		this.agent = agent;
		this.inputs = inputs;
		this.targetType = target;
		this.targetSmell = targetSmell;
		
		this.taskName = "Task " + targetSmell;
		this.energyCost = 0.01;
		this.printLearning = true;
	}

	@Override
	public boolean checkInterrupt(StimuliMap load) {
		return load.getAmount(targetSmell) < 0.1 || agent.getEnergy() < 0.1 || agent.findATask(load) != this;
	}

	@Override
	public double compute(StimuliMap load) {
		double score = 0;
		
		for(Entry<Stimulus, Double> e : inputs.entrySet())
		{
			score += e.getValue() * load.getAmount(e.getKey());
		}
		
		return MyUtils.sigmoid(score, threshold);
	}

	@Override
	public void execute()
	{
		Point2D.Double targetpos = null; //TODO agent.getPosOfStrongestEmitter(targetSmell);
		if(targetpos == null)
		{
			System.err.println("Emitter Not Found");
			return;
		}
		agent.target = targetpos;
		target = agent.getAgentByTypeNPos(targetType, targetpos);
		if(target == null)
		{
			System.err.println("got an error here sir");
			return;
		}

		if(targetpos.distance(agent.getPosition()) < 0.1)
		{
			double targetNeeds = 1-target.getEnergy();
			double transfer = targetNeeds > 0.1 ? 0.1 : targetNeeds;
			
			target.addToEnergy(transfer);
			agent.addToEnergy(-0.7*threshold*transfer);
			

			this.energyCost = 0;
		}
		else
		{
			this.energyCost = 0.0001;
			agent.moveTowards(targetpos);
		}
	}
	

	@Override
	public void interrupt() {
		agent.interruptTask();
	}

}
