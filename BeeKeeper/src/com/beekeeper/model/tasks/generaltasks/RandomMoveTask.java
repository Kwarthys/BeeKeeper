package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.tasks.Task;

public class RandomMoveTask extends Task {
	
	private WorkingAgent agent;
	
	public RandomMoveTask(WorkingAgent a) {
		agent = a;
		
		taskName = "Random Walk";
		energyCost = 0.00;
	}

	@Override
	public boolean checkInterrupt(StimuliMap load) {
		return agent.getEnergy() < 0.1;
	}

	@Override
	public double compute(StimuliMap load) {
		return agent.getEnergy() > 0.5 ? 0.05:0;
	}

	@Override
	public void execute() {
		//agent.randomMove();
		agent.emit(Stimulus.StimulusA, 1);
/*
		StimuliMap s = agent.getPercievedStimuli();
		Task detectedTask = agent.findATask(s);
		if(detectedTask.compute(s) > agent.getCurrentTask().compute(s))
		{
			agent.interruptTask();
		}
*/
	}

	@Override
	public void interrupt() {
		agent.interruptTask();
	}

}
