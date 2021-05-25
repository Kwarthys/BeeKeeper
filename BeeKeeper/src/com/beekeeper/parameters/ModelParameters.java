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
	public static double SIMU_ACCELERATION = 1;
	
	public static boolean LOGGING = false;
	public static boolean BEELOGGING = false;
	public static int NB_BEE_LOGGING = 3;
	/**********************/
	
	
	/*** SIMULATION ALTERATION ***/
	public static boolean BYPASS_MOTIVATION = false;
	public static boolean BYPASS_PHYSIOLOGY = false;
	
	public static boolean UI_ENABLED = true;
	
	public static final double secondToTimeStepCoef = 1;

	public static long SIMULATION_SLEEP_BY_TIMESTEP = 1000;
	
	public static boolean LARVA_CAN_HATCH = true;
	public static boolean FORAGERS_DIE_SOONER = true;
	
	/*****************************/
	

	/*** CHEATSHEET ***/
	public static final int SECOND = (int) (secondToTimeStepCoef);
	public static final int MINUTE = (int) (60 * SECOND);
	public static final int HOUR = (int) (60 * MINUTE);
	public static final int DAY = (int) (24 * HOUR);
	/******************/
	
	//Normal age (nurse) go a year, foraging go 30days, at 20+10 rouglhy. 11months->11days while foraging (*30) (*30 was a bit too high so pushed back to *25)
	public static int foragingAgePenalty = 25;
	public static int maxTimestepAge;
	public static int timestepLarvaPop;
	
	public static double beesHJGeneticsVariation = 0.1; //10%

	public static int larvaEggUntilAge;
	public static int larvaLarvaUntilAge;
	public static int larvaNympheaUntilAge;
	
	public static int queenAgeBeforeLaying = 20 * DAY;
	public static double QUEEN_OVARDEV_INCREMENT;
	
	/**
	 * Total agent population should not be higher than that. This is used for the capacity of different buffers
	 */
	public static int colonyMajoredEstimatedMaxSize;

	//Free 1957 : Tranmission of food between worker -> Empty stomach after 8ish hours of starvation
	//Wang et al 2016: starved honey bees for 12hours -> 50%bee died
	public static double HUNGER_INCREMENT;
	
	public static double LARVA_FEEDING_MEANDURATION; //Biology of Honey bee p97.
	public static double WORKER_FEEDING_MEANDURATION; //Estimated
	
	//2 Days old larvae all dies in 7h of starvation (some earlier) while 4days old all survive that 7hour starvation (-> hunger doesn't start high)
	public static double LARVAE_HUNGER_INCREMENT;
	
	//Huang & Otis 1991 - Inspection and feeding ... fed every ~35min
	/**
	 * Represents the time that the larva takes to eat all that has been given to it
	 */
	public static int MIN_DURATION_LARVAEFEDAGAIN;
	
	/**
	 * Quantity of food (i.e energy) given at each feeding
	 */
	public static double LARVAE_FEEDING_QUANTITY;

	public static double LARVA_EO_TIMELY_EMMISION;
	public static double QUEEN_EO_TIMELY_EMMISION;
	public static double EGG_EO_TIMELY_EMMISION;
	public static double larvaEOtoQueenEOCoef = 2;
	public static double larvaEOtoEggEOCoef = 2;
	
	public static final double MOTIVATION_STEP = 0.01; //ACCELERATION ? maybe not
	
	public static final double SMELL_THRESHOLD = 0.000001;

	public static final double MAX_MOTIVATION = 0.9;
	
	public static double FORAGING_TIME;
	public static double FORAGING_ENERGYCOST;
	
	//Esters usually have 16hours so we'll say that for now
	public static double ETHYLE_OLEATE_HALFLIFE;
	//Experience this makes 50-50 when NEWBORN with 2-1 Larva/bees
	//public static double ETHYLE_OLEATE_TRANSMISSIBILITY = Math.max(1, 120.0 / SIMU_ACCELERATION); //1day's too much
	public static double ETHYLE_OLEATE_TRANSMISSIBILITY;

	/**
	 * Targeting a 50day full replenishement of the targeted colony size
	 */
	public static double LAYEGG_MEANDURATION;
	
	/* want it to be half tired after an hour */
	public static double QUEEN_TASKS_ENERGYCOSTS;

	public static double RESTTASK_RESTORATION;

	
	public static double HJ_EQUILIBRIUM = 0.8;
	public static double EO_EQUILIBRIUM = 1;//from1000 to 1
	
	//Le Comte : isolated bee go forage at 5 day old -> 430 000s, we aim EQUILIBRIUM at 5 days ////// compensate EO effects?
	public static double HJ_INCREMENT; //1.851e-6
	
	//public static double EOEmissionCoef = 0.000000004; //REVERSED WITH HJRed
	//public static double EOEmissionCoef = 0.01; //CHANGED TO 0.02
	//public static double EOEmissionCoef = 0.02;
	//public static double EOEmissionCoef = ((1-StimulusFactory.getEvapRate(Stimulus.EthyleOleate)) * EO_EQUILIBRIUM) / HJ_EQUILIBRIUM; //0.015
	public static double getEOEvapAtEOEQ()
	{		
		return (1-StimulusFactory.getEvapRate(Stimulus.EthyleOleate)) * EO_EQUILIBRIUM;
	}

	public static final double getEthyleOleateEmitedByHJ(double hjTiter)
	{
		//System.out.println("EVAP " + getEOEvapAtEOEQ() + " emits " + Math.pow((hjTiter/HJ_EQUILIBRIUM),5) * getEOEvapAtEOEQ() + " at " + hjTiter);
		//System.out.println(hjTiter/HJ_EQUILIBRIUM + " at " + hjTiter);
		return Math.pow((hjTiter/HJ_EQUILIBRIUM),1) * getEOEvapAtEOEQ();// * SIMU_ACCELERATION;
		
		//return hjTiter * 0.000000004 * SIMU_ACCELERATION; //Calculated with google sheet to match biological observation : https://docs.google.com/spreadsheets/d/1G8Npmpj3zvKJWzIT85aO0ulNe2J9UrzrpwM7wehnqgA/edit?usp=sharing
	}

	//public static double hjReduction = 0.01;//error
	//public static double hjReduction = 0.000000004;//old
	//public static double hjReduction = 0.0000000023;
	//public static double hjReduction = HJ_INCREMENT / EO_EQUILIBRIUM; //EO alteration compensates HJ timely emission at EO_Equilibrium EO Amount
	
	public static double EOEmissionPower = 1.0/3.0;
	
	public static final double getHJModifiedByEthyleOleate(double ethyleOleateAmount)
	{
		double reduction = Math.pow((ethyleOleateAmount / EO_EQUILIBRIUM),EOEmissionPower) * HJ_INCREMENT;
		
		//double reduction = ethyleOleateAmount * 0.01 * SIMU_ACCELERATION;  //Calculated with google sheet to match biological observation, see link above
		//System.out.println("EthyleOleate at " + ethyleOleateAmount + ", HJ down by " + reduction);
		return reduction;
	}
	
	public static double LARVA_EO_EMISSION_COEF = 2; //Found experimentally
	
	/**
	 * Re calculate all the parameters given potentially new fundamental parameters
	 */
	public static void applyPhysioParameters()
	{
		/*** PHEROMONES ***/		
		//Esters usually have 16hours so we'll say that for now
		ETHYLE_OLEATE_HALFLIFE = 16.0 * HOUR / SIMU_ACCELERATION;
		//Experience this makes 50-50 when NEWBORN with 2-1 Larva/bees
		ETHYLE_OLEATE_TRANSMISSIBILITY = 6.0 * HOUR / SIMU_ACCELERATION;		
		
		StimulusFactory.refreshDataBase();		
		
		LARVA_EO_TIMELY_EMMISION = getEOEvapAtEOEQ() * LARVA_EO_EMISSION_COEF;	
		QUEEN_EO_TIMELY_EMMISION = larvaEOtoQueenEOCoef * LARVA_EO_TIMELY_EMMISION;
		EGG_EO_TIMELY_EMMISION = larvaEOtoEggEOCoef * LARVA_EO_TIMELY_EMMISION;
		
		//hjReduction = HJ_INCREMENT / EO_EQUILIBRIUM;
		maxTimestepAge = (int) (/*1year*/ 364 * DAY / SIMU_ACCELERATION);
		
		larvaEggUntilAge = 3 * DAY;
		larvaLarvaUntilAge = larvaEggUntilAge + 6 * DAY;
		larvaNympheaUntilAge = larvaLarvaUntilAge + 12 * DAY;
		
		//queenAgeBeforeLaying = 28 * DAY;
		QUEEN_OVARDEV_INCREMENT = MAX_MOTIVATION / queenAgeBeforeLaying;
		
		timestepLarvaPop = larvaNympheaUntilAge;//(int)(/*21days*/ 21 * DAY / SIMU_ACCELERATION);
		HUNGER_INCREMENT = 1.0 / (13 * HOUR) * SIMU_ACCELERATION;
		
		LARVA_FEEDING_MEANDURATION = 70 * SECOND * SIMU_ACCELERATION;; //Biology of Honey bee p97.
		WORKER_FEEDING_MEANDURATION = 60 * SECOND * SIMU_ACCELERATION;; //Estimated
		
		//2 Days old larvae all dies in 7h of starvation (some earlier) while 4days old all survive that 7hour starvation (-> hunger doesn't start high)
		LARVAE_HUNGER_INCREMENT = 1.0 / (13 * HOUR) * SIMU_ACCELERATION;
		
		//Huang & Otis 1991 - Inspection and feeding ... fed every ~35min
		MIN_DURATION_LARVAEFEDAGAIN = (int) (30 * MINUTE / SIMU_ACCELERATION); //Represents the time that the larva takes to eat all that has been given to it
		
		LARVAE_FEEDING_QUANTITY = LARVAE_HUNGER_INCREMENT * 35 * MINUTE;// / SIMU_ACCELERATION;
		
		FORAGING_TIME = Math.max(20 * MINUTE / SIMU_ACCELERATION, 20);
		
		// 30% of the foraging TIME
		FORAGING_ENERGYCOST = 0.3 / FORAGING_TIME;

		RESTTASK_RESTORATION = FORAGING_ENERGYCOST; //50% coef stay at home
		
		colonyMajoredEstimatedMaxSize = 65000;//3 * (COLONY_TARGET_SIZE + NUMBER_BEES + NUMBER_LARVAE);
		
		/* wantED it to be tired after an hour */
		QUEEN_TASKS_ENERGYCOSTS = 0;//1.0/HOUR * SIMU_ACCELERATION;
		
		//Le Comte : isolated bee go forage at 5 day old -> 430 000s, we aim EQUILIBRIUM at 5 days //////compensate EO effects?
		HJ_INCREMENT = HJ_EQUILIBRIUM / (5 * DAY) * SIMU_ACCELERATION; //1.851e-6
	}
	
	public static double queenSpeedBySizeCoef = 1;
	
	public static double getQueenSpeedFromColonySize(int colonySize)
	{
		boolean debugSpeed = false;
		if(Math.random() > .99 && debugSpeed)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("pause : ");
			sb.append(timestepLarvaPop / queenSpeedBySizeCoef / colonySize);
			sb.append(" targeting ");
			sb.append(colonySize);
			sb.append(".");
			/*
			for(int i = 1; i < 5; i++)
			{
				sb.append(" ");
				sb.append(200*i);
				sb.append(":");
				sb.append(getQueenSpeedFromColonySize(200*i));				
			}
			*/
			sb.append(".\n");
			System.out.println(sb.toString());
		}
		return timestepLarvaPop / queenSpeedBySizeCoef / colonySize;
	}

	
	/*** SIMULATION (STARTING) CONDITIONS ***/
	
	public static enum StartMode{Old, NewBorn, Random, Random20, Random80;}
	
	public static int NUMBER_FRAMES = 1;
	public static int NUMBER_BEES = 1000;
	public static int NUMBER_LARVAE = 750;
	public static int SIMU_LENGTH = 50*DAY;
	public static StartMode startMode = StartMode.Random;
	public static boolean SPAWN_A_QUEEN = true;
	
	public static double getStartingBeeHJTiter()
	{
		switch(startMode)
		{
		case NewBorn:
			return Math.random() * 0.2;
		case Old:
			return Math.random() * 0.2 + 0.8;
		case Random80:
			return Math.random() < 0.80 ? Math.random() * 0.2 : (Math.random() * 0.2 + 0.8);
		case Random20:
			return Math.random() < 0.20 ? Math.random() * 0.2 : (Math.random() * 0.2 + 0.8);
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
		
		if(SPAWN_A_QUEEN && FORAGERS_DIE_SOONER && LARVA_CAN_HATCH && !BYPASS_MOTIVATION && !BYPASS_PHYSIOLOGY)
		{
			sb.append("Real");
		}
		else if(!BYPASS_MOTIVATION && !BYPASS_PHYSIOLOGY)
		{
			sb.append("Classic");
		}
		
		
		
		if(BYPASS_MOTIVATION)
		{
			sb.append("NoMot");
		}
		if(BYPASS_PHYSIOLOGY)
		{
			sb.append("NoPhysio");
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
	
	/*
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
	*/
	/********* EXPE PARAM MODIFICATION ***********/
}
