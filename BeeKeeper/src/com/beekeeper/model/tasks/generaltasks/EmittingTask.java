package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimulusFactory;
import com.beekeeper.model.tasks.Task;

public class EmittingTask extends Task
{
	private EmitterAgent agent;
	private Stimulus smell;
	
	public EmittingTask(EmitterAgent a, Stimulus toEmit)
	{
		agent = a;
		smell = toEmit;
		
		this.energyCost = 0.001;
		this.taskName = "Emitting " + toEmit;
	}

	@Override
	public boolean checkInterrupt(StimuliMap load) {
		return false;
	}

	@Override
	public double compute(StimuliMap load) {
		return 1;
	}

	@Override
	public void execute(){
		agent.getStimuliLoad().emit(StimulusFactory.get(smell, 1-agent.getEnergy()));
	}

	@Override
	public void interrupt(){}

}