package com.beekeeper.model.agent.implem;

import java.util.HashMap;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.generaltasks.RandomMoveTask;
import com.beekeeper.model.tasks.generaltasks.RestTask;
import com.beekeeper.model.tasks.generaltasks.TypicalTask;

public class TestAgent extends WorkingAgent {

	public TestAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, double x, double y)
	{
		super(stimuliManagerServices, controllerServices, x, y);
		this.type = AgentType.TEST_AGENT;
		if(this.getEnergy() < 0.5)
		{
			this.addToEnergy(0.5);
		}
		
		fillTaskList();
	}

	@Override
	protected void fillTaskList()
	{
		taskList.add(new RestTask(this));
		taskList.add(new RandomMoveTask(this));
		
		HashMap<Stimulus, Double> inputs = new HashMap<Stimulus, Double>();
		inputs.put(Stimulus.StimulusA, 1.0);
		inputs.put(Stimulus.StimulusB, -0.5);
		taskList.add(new TypicalTask(this, inputs, AgentType.TEST_EMITTERAGENT, Stimulus.StimulusA));
		
		inputs.clear();
		inputs.put(Stimulus.StimulusB, 1.0);
		taskList.add(new TypicalTask(this, inputs, AgentType.TEST_EMITTERAGENT, Stimulus.StimulusB));
		
		inputs.clear();
		inputs.put(Stimulus.StimulusA, -1.0);
		inputs.put(Stimulus.StimulusB, 1.0);
		inputs.put(Stimulus.StimulusC, 1.0);
		taskList.add(new TypicalTask(this, inputs, AgentType.TEST_EMITTERAGENT, Stimulus.StimulusC));
	}

}
