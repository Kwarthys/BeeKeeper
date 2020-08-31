package com.beekeeper.model.stimuli;

public abstract class AStimulus
{
	/**
	 * halflife in seconds of this stimulus
	 */
	protected double halfLife = 1.35;
	
	/**
	 * Act as a kind of halflife, but as its an exchange, it represents the time in second where 80% of the exchange will be completed.
	 */
	protected double transmissibility_halflifelike = 0.1;
	protected Stimulus type;
	
	protected boolean isVolatile = false;
	
	public boolean isVolatile() {return isVolatile;}
	
	public Stimulus getStimulusType() {return type;}
	
	public double getHalfLife() {return halfLife;}
	
	public double getTransmissibility() {return transmissibility_halflifelike;}
}
