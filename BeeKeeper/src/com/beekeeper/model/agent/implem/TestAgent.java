package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.generaltasks.RandomMoveTask;
import com.beekeeper.model.tasks.generaltasks.RestTask;

public class TestAgent extends WorkingAgent {

	public TestAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		super(stimuliManagerServices, controllerServices);
		this.type = AgentType.TEST_AGENT;
		if(this.getEnergy() < 0.5)
		{
			this.addToEnergy(0.5);
		}
	}

	@Override
	protected void fillTaskList()
	{
		taskList.add(new RandomMoveTask(this.ownServices));
		taskList.add(new RestTask(this.ownServices));
		/*
		taskList.add(new RestTask(this));
		taskList.add(new RandomMoveTask(this));
		
		HashMap<Stimulus, Double> inputsA = new HashMap<Stimulus, Double>();
		inputsA.put(Stimulus.StimulusA, 1.0);
		taskList.add(new TypicalTask(this, inputsA, AgentType.TEST_EMITTERAGENT, Stimulus.StimulusA));
		
		HashMap<Stimulus, Double> inputsB = new HashMap<Stimulus, Double>();
		//inputsB.put(Stimulus.StimulusA, -0.5);
		inputsB.put(Stimulus.StimulusB, 1.0);
		taskList.add(new TypicalTask(this, inputsB, AgentType.TEST_EMITTERAGENT, Stimulus.StimulusB));
		
		HashMap<Stimulus, Double> inputsC = new HashMap<Stimulus, Double>();
		inputsC.put(Stimulus.StimulusA, -1.0);
		inputsC.put(Stimulus.StimulusB, 1.0);
		inputsC.put(Stimulus.StimulusC, 1.0);
		taskList.add(new TypicalTask(this, inputsC, AgentType.TEST_EMITTERAGENT, Stimulus.StimulusC));
		*/
	}

	@Override
	protected void advanceMetabolism() {
		// TODO Auto-generated method stub
		
	}

}
