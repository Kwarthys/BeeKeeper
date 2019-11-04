package com.beekeeper.parameters;

public class ModelParameters
{
	public static final double TASK_LEARN_RATE = 0.001;
	public static final double TASK_FORGET_RATE = 0.0001;
	
	public final static double COMBCELL_SIZE = 6.0;


	public static final double TIME_DECAY_HungryLarvae = 0.1;
	public static final double TRANSMISSIBILITY_HungryLarvae = 0.5;
	public static final double SMELL_RANGE_HungryLarvae = 0.90;

	public static final double TIME_DECAY_FoodSmell = 0.1;
	public static final double TRANSMISSIBILITY_FoodSmell = 0.1;
	public static final double SMELL_RANGE_FoodSmell = 0.95;
	
	
	public static final double SMELL_THRESHOLD = 0.02;

	public static final double MAX_TASK_THRESHOLD = 0.80;
	public static final double MIN_TASK_THRESHOLD = 0.10;
	
	public static double getNormalisedThreshold(double t)
	{
		return t/(MAX_TASK_THRESHOLD-MIN_TASK_THRESHOLD) - MIN_TASK_THRESHOLD/(MAX_TASK_THRESHOLD-MIN_TASK_THRESHOLD);
	}
	
	
	public static final double HIVE_THERMAL_RESISTANCE = 15;
	
	
	public static final double timeStepToSecondCoef = 0.1;
}
