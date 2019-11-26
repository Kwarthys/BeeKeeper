package com.beekeeper.model.tasks.generaltasks;

import com.beekeeper.model.agent.WorkingAgentServices;
import com.beekeeper.model.tasks.Action;

public class RandomMoveAction extends Action {

	/**
	 * Condition always true and triggers a random move for the agent and a drop of motivation;
	 */
	public RandomMoveAction(WorkingAgentServices s) {
		super(0.5, 0.001, s);
	}

	@Override
	public void execute() {
		this.agentServices.randomMove();
		this.agentServices.dropMotivation();
	}

	@Override
	public boolean check() {
		return true;
	}

}
