package com.beekeeper.launcher;

import java.awt.Toolkit;

import com.beekeeper.controller.MainController;
import com.beekeeper.network.NetworkManager;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.parameters.ModelParameters.StartMode;
import com.beekeeper.utils.IDManager;

public class BeeKeeperLauncher {
	
	public static int index = 1;

	public static void main(String[] args)
	{	
		System.setProperty("sun.java2d.opengl", "true");
		//long start = System.nanoTime();


		//startNetworkMode();
		startMultipleExpeMode();

		//long progTime = (System.nanoTime() - start)/1000000000;
		//long simuTime = (long) (ModelParameters.SIMU_LENGTH / ModelParameters.secondToTimeStepCoef);
		//System.out.println("Prog took " + progTime + "s to simulate " + simuTime + "s (x" + simuTime*1.0/progTime + ").");

	}

	public static void startNetworkMode()
	{
		NetworkManager nm = new NetworkManager();

		MainController mc;
		
		//ModelParameters.UI_ENABLED = false;
		ModelParameters.SIMU_LENGTH = 50 * ModelParameters.DAY;
		ModelParameters.NUMBER_BEES = 20000;
		ModelParameters.NUMBER_LARVAE = 700;//750;
		ModelParameters.NUMBER_FRAMES = 8; //MAX IS 8
		ModelParameters.LARVA_CAN_HATCH = false;
		ModelParameters.FORAGERS_DIE_SOONER = false;
		ModelParameters.startMode = StartMode.Random80;
		
		//ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP = 0; // always max speed

		do
		{
			IDManager.resetIDCounter();
			mc = new MainController();
			nm.registerControllerServices(mc.getServices());
		}while(mc.start());

		nm.closing();
	}
	
	private static void startExpeAndMonitorTime(MainController mc)
	{
		long start = System.nanoTime();
		System.out.println("Running simulation " + index );
		IDManager.resetIDCounter();
		mc.start();
		long progTime = (System.nanoTime() - start)/1000000;
		long simuTime = (long) (ModelParameters.SIMU_LENGTH / ModelParameters.secondToTimeStepCoef);
		System.out.println("Prog took " + progTime + "ms to simulate " + simuTime + "s (x" + simuTime*1000.0/progTime + "). " + index++);
	}
	
	public static void startMultipleExpeMode()
	{				
		ModelParameters.UI_ENABLED = false;
		ModelParameters.LOGGING = true;
		//ModelParameters.BEELOGGING = true;
		//ModelParameters.NB_BEE_LOGGING = 24;
		ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP = 0;
		ModelParameters.SIMU_LENGTH = 10 * ModelParameters.DAY;
		ModelParameters.NUMBER_BEES = 500;
		ModelParameters.NUMBER_LARVAE = 700;
		ModelParameters.NUMBER_FRAMES = 2;
		//ModelParameters.LARVA_CAN_HATCH = false;
		//ModelParameters.FORAGERS_DIE_SOONER = false;
		//ModelParameters.SPAWN_A_QUEEN = false;
		
		
		//10000 - 5000 - 8 - 10D : Prog took 24711954ms to simulate 864000s (x34.96283620469672).
		//500 - 700 - 8 - 10D : Prog took 1713176ms to simulate 864000s (x504.326467333187).
		
		
		int charNumberA = 65;
		//StartMode[] mode = {StartMode.Random20, StartMode.Random, StartMode.Random80};
		//int[] nb = {1,2,4,10,100,200,1000,2000,5000};
		int[] nb = {2,3,5,10,50,100,200,500,1000};
/*
		for(int j = 0; j < 3; ++j)
		{
			ModelParameters.startMode = mode[j];

			for(int i = 0; i < 3; ++i)
			{
				ModelParameters.identifier = (char)(charNumberA + i + 3*j);
				ModelParameters.NUMBER_LARVAE = nb[i];
				startExpeAndMonitorTime(new MainController());	
			}
		}
*/

		ModelParameters.startMode = StartMode.Random80;
		for(int i = 0; i < 9 ; ++i)
		{
			ModelParameters.LARVA_EO_EMISSION_COEF = nb[i];
			ModelParameters.identifier = (char)(charNumberA + i);
			startExpeAndMonitorTime(new MainController());
		}


/*
		ModelParameters.startMode = StartMode.Random20;
		ModelParameters.identifier = 'A';
		startExpeAndMonitorTime(new MainController());
*/

		Toolkit.getDefaultToolkit().beep();
		System.out.println("All Expes Done");
	}

}
