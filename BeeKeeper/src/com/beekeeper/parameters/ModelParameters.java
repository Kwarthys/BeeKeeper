package com.beekeeper.parameters;

public class ModelParameters
{
	public static final double TASK_LEARN_RATE = 1;
	public static final double TASK_FORGET_RATE = 0.1;


	public static final double TIME_DECAY_HungryLarvae = 0.1;
	public static final double TRANSMISSIBILITY_HungryLarvae = 0.5;
	public static final double SMELL_RANGE_HungryLarvae = 0.90;

	public static final double TIME_DECAY_FoodSmell = 0.1;
	public static final double TRANSMISSIBILITY_FoodSmell = 0.1;
	public static final double SMELL_RANGE_FoodSmell = 0.95;
	
	
	public static final double SMELL_THRESHOLD = 0.001;
	
	public static final double MAX_TASK_THRESHOLD = 100;
	public static final double HIVE_THERMAL_RESISTANCE = 15;
}
