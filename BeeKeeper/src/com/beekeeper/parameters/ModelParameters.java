package com.beekeeper.parameters;

import com.beekeeper.utils.IDManager;

public class ModelParameters
{	
	/** OLD OR FUTURE PARAMETERS **/

	//public static final double TASK_LEARN_RATE = 0.001;
	//public static final double TASK_FORGET_RATE = 0.0001;

	//public static final double HIVE_THERMAL_RESISTANCE = 15;
	
	//public static final double MAX_TASK_THRESHOLD = 0.80;
	//public static final double MIN_TASK_THRESHOLD = 0.10;	
	//public static double getNormalisedThreshold(double t)
	//{
	//	return t/(MAX_TASK_THRESHOLD-MIN_TASK_THRESHOLD) - MIN_TASK_THRESHOLD/(MAX_TASK_THRESHOLD-MIN_TASK_THRESHOLD);
	//}	

	//public static final double FoodSmell_HALFLIFE = 0.03; //ACCELERATION ?
	//public static final double TRANSMISSIBILITY_FoodSmell = 0.03; //ACCELERATION ?

	//public static final double HungryLarvae_HALFLIFE = 0.03; //ACCELERATION ?
	//public static final double TRANSMISSIBILITY_HungryLarvae = 0.1; //ACCELERATION ?
	/******************************/	
	
	/** META-PARAMS **/
	public static final double SIMU_ACCELERATION = 1;
	/**********************/
	
	
	/*** SIMULATION ALTERATION ***/
	public static boolean BYPASS_MOTIVATION = false;
	public static boolean BYPASS_PHYSIOLOGY = false;
	
	public static boolean UI_ENABLED = false;
	
	public static final double secondToTimeStepCoef = 1;

	public static long SIMULATION_SLEEP_BY_TIMESTEP = 500;//1/secondToTimeStepCoef//100
	/*****************************/
	

	//Free 1957 : Tranmission of food between worker -> Empty stomach after 8ish hours of starvation
	public static final double HUNGER_INCREMENT = 1 / (60 * 60 * 8 * secondToTimeStepCoef) * SIMU_ACCELERATION;
	
	//Huang & Otis 1991 - Inspection and feeding ... fed every ~35min
	public static final double LARVAE_HUNGER_INCREMENT = 0.5 / (35 * 60 * secondToTimeStepCoef) * SIMU_ACCELERATION;
	
	public static final double MOTIVATION_STEP = 0.01; //ACCELERATION ? maybe not
	
	public static final double SMELL_THRESHOLD = 0.01;

	public static final double MAX_MOTIVATION = 0.8;

	public static final double LARVA_FEEDING_MEANDURATION = Math.max(1, 70 / SIMU_ACCELERATION); //Biology of Honey bee p97. //Don't wanna go below the second no matter the acceleration
	public static final double WORKER_FEEDING_MEANDURATION = Math.max(1, 60 / SIMU_ACCELERATION); //Estimated
	
	//Le Comte : isolated bee go forage at 5 day old -> 430 000s, we aim 0.8 at 5 days to compensate EO effects
	public static final double HJ_INCREMENT = 0.8 / (5 * 24 * 60 * 60 * secondToTimeStepCoef) * SIMU_ACCELERATION;
	
	//Esters usually have 16hours so we'll say that for now
	public static final double ETHYLE_OLEATE_HALFLIFE = 16 * 60 * 60 / SIMU_ACCELERATION;
	//No idea of even what to ask google or biologists for that
	public static final double ETHYLE_OLEATE_TRANSMISSIBILITY = Math.max(1, 60 / SIMU_ACCELERATION);
	
	public static final double getEthyleOleateEmitedByHJ(double hjTiter)
	{
		return hjTiter * 0.000000004 * SIMU_ACCELERATION; //Calculated with google sheet to match biological observation : https://docs.google.com/spreadsheets/d/1G8Npmpj3zvKJWzIT85aO0ulNe2J9UrzrpwM7wehnqgA/edit?usp=sharing
	}
	
	public static final double getHJModifiedByEthyleOleate(double ethyleOleateAmount)
	{
		double reduction = ethyleOleateAmount * 0.01 * SIMU_ACCELERATION;  //Calculated with google sheet to match biological observation, see link above
		//System.out.println("EthyleOleate at " + ethyleOleateAmount + ", HJ down by " + reduction);
		return reduction;
	}
	
	
	/*** SIMULATION (STARTING) CONDITIONS ***/
	
	public static enum StartMode{Old, NewBorn, Random;}
	
	public static int NUMBER_FRAMES = 3;
	public static int NUMBER_BEES = 6000;
	public static int NUMBER_LARVAE = 500;
	public static int SIMU_LENGTH = (int) (24 * 60 * 60 * secondToTimeStepCoef);
	public static StartMode startMode = StartMode.Random;
	
	public static double getStartingBeeHJTiter()
	{
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
	
	/*****************************/
	
	public static int expeIndex = 0;
	
	public static String getModelState()
	{
		StringBuffer sb = new StringBuffer();
		if(BYPASS_MOTIVATION)
		{
			sb.append("NoMot");
		}
		if(BYPASS_PHYSIOLOGY)
		{
			sb.append("NoPhysio");
		}
		
		if(!BYPASS_MOTIVATION && !BYPASS_PHYSIOLOGY)
		{
			sb.append("Classic");
		}
		return sb.toString();
	}
	
	/********* EXPE PARAM MODIFICATION ***********/
	
	public static void paramExpe(int numberOfBees, int numberOfLarvae, int simuLength, StartMode startMode, int expeIndex)
	{
		ModelParameters.NUMBER_BEES = numberOfBees;
		ModelParameters.NUMBER_LARVAE = numberOfLarvae;
		ModelParameters.SIMU_LENGTH = simuLength;
		ModelParameters.startMode = startMode;
		ModelParameters.expeIndex = expeIndex;
		IDManager.resetIDCounter();
	}
	
	/********* EXPE PARAM MODIFICATION ***********/
}
