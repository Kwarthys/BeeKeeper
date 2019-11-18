package com.beekeeper.model.agent;

import com.beekeeper.model.stimuli.Stimulus;

public interface WorkingAgentServices {
	public void emit(Stimulus smell, double amount);
	public void addToEnergy(double amount);
	public double getEnergy();
	
	public void dropMotivation();
	
	public void randomMove();
}