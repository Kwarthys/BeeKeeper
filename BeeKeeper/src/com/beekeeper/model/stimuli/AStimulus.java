package com.beekeeper.model.stimuli;

public abstract class AStimulus
{
	protected double timeDecay = 0.95;
	protected double transmissibility = 0.5;
	protected Stimulus type;
	
	public Stimulus getStimulusType() {return type;}
	
	public double getDecay() {return timeDecay;}
	
	public double getTransmissibility() {return transmissibility;}
}
