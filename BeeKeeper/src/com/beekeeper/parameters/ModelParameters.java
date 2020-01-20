package com.beekeeper.parameters;

import com.beekeeper.utils.IDManager;
import com.beekeeper.utils.MyUtils;

public class ModelParameters
{
	public static final double TASK_LEARN_RATE = 0.001;
	public static final double TASK_FORGET_RATE = 0.0001;
	
	public final static double COMBCELL_SIZE = 6.0;


	public static final double TIME_DECAY_HungryLarvae = 0.1;
	public static final double TRANSMISSIBILITY_HungryLarvae = 0.5;

	public static final double TIME_DECAY_FoodSmell = 0.1;
	public static final double TRANSMISSIBILITY_FoodSmell = 0.1;
	
	public static final double getOcimeneEmitedByHJ(double hjTiter)
	{
		return hjTiter * hjTiter * hjTiter;
	}
	
	public static final double getHJModifiedByOcimene(double ocimeneAmount)
	{
		double reduction = MyUtils.sigmoid(ocimeneAmount, 40);
		//System.out.println("Ocimene at " + ocimeneAmount + ", HJ down by " + reduction);
		return reduction;
	}
	
	public static final double HJ_INCREMENT = 0.0001;
	public static final double OCIMENE_EVAPRATE = 0.8;
	public static final double OCIMENE_TRANSMISSIBILITY = 0.3;
	
	
	public static final double SMELL_THRESHOLD = 0.02;

	public static final double MAX_TASK_THRESHOLD = 0.80;
	public static final double MIN_TASK_THRESHOLD = 0.10;
	
	public static double getNormalisedThreshold(double t)
	{
		return t/(MAX_TASK_THRESHOLD-MIN_TASK_THRESHOLD) - MIN_TASK_THRESHOLD/(MAX_TASK_THRESHOLD-MIN_TASK_THRESHOLD);
	}
	
	
	public static final double HIVE_THERMAL_RESISTANCE = 15;
	
	
	public static final double secondToTimeStepCoef = 10;
	
	public static final double MOTIVATION_STEP = 0.01;
	
	public static enum StartMode{Old, NewBorn, Random;}
	
	public static int NUMBER_BEES = 150;
	public static int NUMBER_LARVAE = 150;
	public static int SIMU_LENGTH = 8000;
	public static StartMode startMode = StartMode.Random;
	

	public static double getStartingBeeHJTiter() {
		switch(startMode)
		{
		case NewBorn:
			return 0.0;
		case Old:
			return 1.0;
		case Random:
		default:
			return Math.random();
		}
		
	}
	
	
	/********* EXPE PARAM MODIFICATION ***********/
	
	public static void paramExpe(int numberOfBees, int numberOfLarvae, int simuLength, StartMode startMode)
	{
		ModelParameters.NUMBER_BEES = numberOfBees;
		ModelParameters.NUMBER_LARVAE = numberOfLarvae;
		ModelParameters.SIMU_LENGTH = simuLength;
		ModelParameters.startMode = startMode;
		IDManager.resetIDCounter();
	}
	
	/********* EXPE PARAM MODIFICATION ***********/
}
