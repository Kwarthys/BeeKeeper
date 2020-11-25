package com.beekeeper.parameters;

import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimulusFactory;
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
	
	public static boolean LOGGING = false;
	/**********************/
	
	
	/*** SIMULATION ALTERATION ***/
	public static boolean BYPASS_MOTIVATION = false;
	public static boolean BYPASS_PHYSIOLOGY = false;
	
	public static boolean UI_ENABLED = true;
	
	public static final double secondToTimeStepCoef = 1;

	public static long SIMULATION_SLEEP_BY_TIMESTEP = 1000;//500
	
	public static boolean LARVA_CAN_HATCH = true;
	public static boolean FORAGERS_DIE_SOONER = true;
	/*****************************/
	

	/*** CHEATSHEET ***/
	public static final int SECOND = (int) (secondToTimeStepCoef);
	public static final int MINUTE = (int) (60 * SECOND);
	public static final int HOUR = (int) (60 * MINUTE);
	public static final int DAY = (int) (24 * HOUR);
	/******************/
	
	//Normal age (nurse) go a year, foraging go 30days, at 20+10 rouglhy. 11months->11days while foraging (*30)
	public static int foragingAgePenalty = 30;
	public static int maxTimestepAge = (int) (/*1year*/ 364 * DAY / SIMU_ACCELERATION);
	public static int timestepLarvaPop = (int)(/*20days*/ 20 * DAY / SIMU_ACCELERATION);

	//Free 1957 : Tranmission of food between worker -> Empty stomach after 8ish hours of starvation
	public static final double HUNGER_INCREMENT = 1 / (8 * HOUR) * SIMU_ACCELERATION;
	
	//Huang & Otis 1991 - Inspection and feeding ... fed every ~35min
	public static final double LARVAE_HUNGER_INCREMENT = 0.25 / (35 * MINUTE) * SIMU_ACCELERATION;
	
	public static final double MOTIVATION_STEP = 0.01; //ACCELERATION ? maybe not
	
	public static final double SMELL_THRESHOLD = 0.01;

	public static final double MAX_MOTIVATION = 0.9;

	public static final double LARVA_FEEDING_MEANDURATION = Math.max(1, 70 / SIMU_ACCELERATION); //Biology of Honey bee p97. //Don't wanna go below the second no matter the acceleration
	public static final double WORKER_FEEDING_MEANDURATION = Math.max(1, 60 / SIMU_ACCELERATION); //Estimated
	
	public static final double FORAGING_TIME_SEC = 20*60;//20 minutes, in sec not in timesteps;
	
	//Esters usually have 16hours so we'll say that for now
	public static final double ETHYLE_OLEATE_HALFLIFE = 16 * HOUR / SIMU_ACCELERATION;
	//No idea of even what to ask google or biologists for that
	public static final double ETHYLE_OLEATE_TRANSMISSIBILITY = Math.max(1, 60 / SIMU_ACCELERATION);

	/* 1 egg per minut */
	public static final double LAYEGG_MEANDURATION = 30*MINUTE / SIMU_ACCELERATION;
	
	/* want it to be half tired after an hour */
	public static final double QUEEN_TASKS_ENERGYCOSTS = 1.0/2.0/HOUR;
	
	//Le Comte : isolated bee go forage at 5 day old -> 430 000s, we aim 0.8 at 5 days to compensate EO effects
	public static double HJ_INCREMENT = 0.8 / (5 * DAY) * SIMU_ACCELERATION; //1.851e-6

	public static double LARVA_EO_TIMELY_EMMISION = 0.15;
	
	public static double HJ_EQUILIBRIUM = 0.8;
	public static double EO_EQUILIBRIUM = 1000;
	
	//public static double EOEmissionCoef = 0.000000004; //REVERSED WITH HJRed
	//public static double EOEmissionCoef = 0.01; //TODO CHANGED TO 0.02
	//public static double EOEmissionCoef = 0.02;
	public static double EOEmissionCoef = ((1-StimulusFactory.getEvapRate(Stimulus.EthyleOleate)) * EO_EQUILIBRIUM) / HJ_EQUILIBRIUM;

	public static final double getEthyleOleateEmitedByHJ(double hjTiter)
	{
		return hjTiter * EOEmissionCoef * SIMU_ACCELERATION;//CHANGING
		
		//return hjTiter * 0.000000004 * SIMU_ACCELERATION; //Calculated with google sheet to match biological observation : https://docs.google.com/spreadsheets/d/1G8Npmpj3zvKJWzIT85aO0ulNe2J9UrzrpwM7wehnqgA/edit?usp=sharing
	}

	//public static double hjReduction = 0.01;//error
	//public static double hjReduction = 0.000000004;//old
	//public static double hjReduction = 0.0000000023;
	public static double hjReduction = HJ_INCREMENT / EO_EQUILIBRIUM; //EO alteration compensates HJ timely emission at EO_Equilibrium EO Amount
	
	public static final double getHJModifiedByEthyleOleate(double ethyleOleateAmount)
	{
		double reduction = ethyleOleateAmount * hjReduction * SIMU_ACCELERATION;//Changing
		
		//double reduction = ethyleOleateAmount * 0.01 * SIMU_ACCELERATION;  //Calculated with google sheet to match biological observation, see link above
		//System.out.println("EthyleOleate at " + ethyleOleateAmount + ", HJ down by " + reduction);
		return reduction;
	}
	
	
	/*** SIMULATION (STARTING) CONDITIONS ***/
	
	public static enum StartMode{Old, NewBorn, Random;}
	
	public static int NUMBER_FRAMES = 1;
	public static int NUMBER_BEES = 1000;
	public static int NUMBER_LARVAE = 50;
	public static int SIMU_LENGTH = 5*DAY;
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

	public static int getAgeFromStartingHJ(double hjTiter) {
		return (int) (hjTiter * 20 * 24 * 60 * 60 * secondToTimeStepCoef);
	}
	
	/*****************************/
	
	public static int expeIndex = 0;
	public static char identifier = '-';
	
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
	
	public static void paramExpe(int numberOfBees, int numberOfLarvae, int simuLength, StartMode startMode, int expeIndex, double hjRed)
	{
		ModelParameters.NUMBER_BEES = numberOfBees;
		ModelParameters.NUMBER_LARVAE = numberOfLarvae;
		ModelParameters.SIMU_LENGTH = simuLength;
		ModelParameters.startMode = startMode;
		ModelParameters.expeIndex = expeIndex;
		ModelParameters.hjReduction = hjRed;
		IDManager.resetIDCounter();
	}
	
	/********* EXPE PARAM MODIFICATION ***********/
}
