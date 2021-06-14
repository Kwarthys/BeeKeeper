package com.beekeeper.launcher;

import java.awt.Toolkit;

import com.beekeeper.controller.MainController;
import com.beekeeper.network.NetworkManager;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.parameters.ModelParameters.StartMode;
import com.beekeeper.utils.IDManager;

public class BeeKeeperLauncher
{
	public static void main(String[] args)
	{	
		System.setProperty("sun.java2d.opengl", "true");
		//long start = System.nanoTime();


		startNetworkMode();
		//startMultipleExpeMode();

		//long progTime = (System.nanoTime() - start)/1000000000;
		//long simuTime = (long) (ModelParameters.SIMU_LENGTH / ModelParameters.secondToTimeStepCoef);
		//System.out.println("Prog took " + progTime + "s to simulate " + simuTime + "s (x" + simuTime*1.0/progTime + ").");

	}

	public static void startNetworkMode()
	{
		NetworkManager nm = new NetworkManager();

		MainController mc;
		
		//ModelParameters.UI_ENABLED = false;
		ModelParameters.SIMU_LENGTH = 80 * ModelParameters.DAY;
		ModelParameters.NUMBER_BEES = 500*7;
		ModelParameters.NUMBER_LARVAE = 500*5;//750;
		ModelParameters.NUMBER_FRAMES = 8; //MAX IS 8
		ModelParameters.LARVA_CAN_HATCH = false;
		ModelParameters.FORAGERS_DIE_SOONER = false;
		ModelParameters.startMode = StartMode.Random80;
		
		//ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP = 0; // always max speed
		
		int expeID = 0;

		do
		{
			setExpeCharId(expeID++);
			IDManager.resetIDCounter();
			mc = new MainController();
			nm.registerControllerServices(mc.getServices());
		}while(mc.start());

		nm.closing();
	}
	
	private static void setExpeCharId(int expeID)
	{
		int charNumberA = 65;
	
		int charnumber = charNumberA + expeID;
		if(charnumber > 90)
		{
			charnumber += 6;
		}
		
		ModelParameters.identifier = (char)charnumber;	
	}
	
	private static void startExpeAndMonitorTime(int expeID)
	{
		setExpeCharId(expeID);
		
		MainController mc = new MainController();
		
		long start = System.nanoTime();
		System.out.println("Running simulation " + (expeID+1));
		IDManager.resetIDCounter();
		mc.start();
		long progTime = (System.nanoTime() - start)/1000000;
		long simuTime = (long) (ModelParameters.SIMU_LENGTH / ModelParameters.secondToTimeStepCoef);
		System.out.println("Prog took " + progTime + "ms to simulate " + simuTime + "s (x" + simuTime*1000.0/progTime + ") " + (expeID+1) +".\n");
	}
	
	public static void startMultipleExpeMode()
	{				
		ModelParameters.UI_ENABLED = false;
		ModelParameters.LOGGING = true;
		//ModelParameters.BEELOGGING = true;
		//ModelParameters.NB_BEE_LOGGING = 24;
		
		ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP = 0;
		ModelParameters.SIMU_LENGTH = 80 * ModelParameters.DAY;
		ModelParameters.NUMBER_BEES = 500;
		ModelParameters.NUMBER_LARVAE = 500;
		ModelParameters.NUMBER_FRAMES = 1;
		
		ModelParameters.LARVA_CAN_HATCH = true;
		ModelParameters.FORAGERS_DIE_SOONER = true;
		ModelParameters.SPAWN_A_QUEEN = true;

		ModelParameters.LARVA_EO_EMISSION_COEF = 2;		
		ModelParameters.EOEmissionPower = 0.33;		
		
		//10000 - 5000 - 8 - 10D : Prog took 24711954ms to simulate 864000s (x34.96283620469672).
		//500 - 700 - 8 - 10D : Prog took 1713176ms to simulate 864000s (x504.326467333187).
		//500 - 500 - 1 - 70D : Prog took 10558079ms to simulate 6048000s (x572.8314781505234).
		
		
		//StartMode[] mode = {StartMode.Old, StartMode.Random, StartMode.NewBorn};
		//int[] nb = {350,500,650};
		//int[] c = {17,20,25};
		//double[] c = {1/2.0,3,5,10,50,100,200,500,1000};
/*
		ModelParameters.startMode = StartMode.Random80;
		ModelParameters.queenAgeBeforeLaying = 0;
		for(int j = 0; j < 3; ++j)
		{
			ModelParameters.LARVA_EO_EMISSION_COEF = c[j];
			for(int i = 0; i < 2; ++i)
			{
				ModelParameters.NUMBER_LARVAE = nb[i];
				startExpeAndMonitorTime(i + 2*j);	
			}
		}
*/
/*
		ModelParameters.queenAgeBeforeLaying = 20 * ModelParameters.DAY;
		
		for(int i = 0; i < 3 ; ++i)
		{
			ModelParameters.startMode = mode[i];
			//ModelParameters.NUMBER_LARVAE = nb[i];
			startExpeAndMonitorTime(i);
		}
*/
		
		ModelParameters.startMode = StartMode.NewBorn;
		ModelParameters.queenAgeBeforeLaying = 17 * ModelParameters.DAY;
		startExpeAndMonitorTime(3);
		ModelParameters.queenAgeBeforeLaying = 28 * ModelParameters.DAY;
		startExpeAndMonitorTime(4);
		

/*
		ModelParameters.startMode = StartMode.Old;
		//ModelParameters.queenAgeBeforeLaying = 0;
		ModelParameters.NUMBER_BEES = 40;
		ModelParameters.NUMBER_LARVAE = 0;
		startExpeAndMonitorTime(23);
*/
		Toolkit.getDefaultToolkit().beep();
		System.out.println("All Expes Done");
	}

}
