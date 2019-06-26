package com.beekeeper.model.agent.implem;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.generaltasks.EmittingTask;

public class TestEmitterAgent extends WorkingAgent
{
	private static Stimulus[] stimuli = {Stimulus.StimulusA, Stimulus.StimulusB, Stimulus.StimulusC};
	
	private Stimulus smell;
	
	public TestEmitterAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, double x, double y) {
		super(stimuliManagerServices, controllerServices, x, y);
		
		this.type = AgentType.TEST_EMITTERAGENT;
		
		smell = stimuli[(int)(Math.random() * 4)];		
	}

	@Override
	protected void fillTaskList() {
		taskList.add(new EmittingTask(this, smell));
	}

}
