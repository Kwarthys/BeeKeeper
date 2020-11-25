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

		do
		{
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
		long progTime = (System.nanoTime() - start)/1000000000;
		long simuTime = (long) (ModelParameters.SIMU_LENGTH / ModelParameters.secondToTimeStepCoef);
		System.out.println("Prog took " + progTime + "s to simulate " + simuTime + "s (x" + simuTime*1.0/progTime + "). " + index++);
	}
	
	public static void startMultipleExpeMode()
	{		
		ModelParameters.UI_ENABLED = false;
		ModelParameters.LOGGING = true;
		ModelParameters.SIMULATION_SLEEP_BY_TIMESTEP = 0;		
		ModelParameters.SIMU_LENGTH = 20 * ModelParameters.DAY;
		ModelParameters.NUMBER_BEES = 1000;
		ModelParameters.NUMBER_LARVAE = 1000;
		ModelParameters.NUMBER_FRAMES = 1;
		ModelParameters.LARVA_CAN_HATCH = false;
		ModelParameters.FORAGERS_DIE_SOONER = false;
		
		
		int charNumberA = 65;
		StartMode[] mode = {StartMode.Old, StartMode.Random, StartMode.NewBorn};
		//int[] nb = {0,50,500};
		/*double[] lem = new double[8];
		
		for(int i = 0; i < 3; ++i)
		{
			lem[i] = ModelParameters.LARVA_EO_TIMELY_EMMISION * Math.pow(2, i+1);
		}	*/	
/*
		for(int j = 0; j < 3; ++j)
		{
			ModelParameters.startMode = mode[j];
			
			for(int i = 0; i < 8; ++i)
			{
				ModelParameters.NUMBER_LARVAE = 100*i;
				
				ModelParameters.identifier = (char)(charNumberA + i + 8*j);
				startExpeAndMonitorTime(new MainController());	
			}
		}
*/	
		ModelParameters.NUMBER_LARVAE = 0;
		
		ModelParameters.startMode = StartMode.Random;
		ModelParameters.identifier = 'I';
		startExpeAndMonitorTime(new MainController());
		
		ModelParameters.startMode = StartMode.NewBorn;
		ModelParameters.identifier = 'Q';
		startExpeAndMonitorTime(new MainController());
/*
		for(int i = 0; i <3 ; ++i)
		{
			ModelParameters.startMode = StartMode.NewBorn;
			ModelParameters.identifier = (char)charNumberA;
			startExpeAndMonitorTime(new MainController());
		}
*/
		
		
		
		
		/*OLD CODE (just in case)
		int simuLength = 5000;
		int reps = 5;

		for(int i = 0; i<reps;++i)
		{			
			ModelParameters.paramExpe(150, 150, simuLength, StartMode.Random,i);
			new MainController();
		}


		for(int i = 0; i<reps;++i)
		{	
		ModelParameters.paramExpe(150, 150, simuLength, StartMode.Old,i);
		new MainController();
		}

		for(int i = 0; i<reps;++i)
		{	
		ModelParameters.paramExpe(150, 150, simuLength, StartMode.NewBorn,i);
		new MainController();
		}

		for(int i = 0; i<reps;++i)
		{	
		ModelParameters.paramExpe(150, 50, simuLength, StartMode.Random,i);
		new MainController();
		}

		for(int i = 0; i<reps;++i)
		{	
		ModelParameters.paramExpe(150, 300, simuLength, StartMode.Random,i);
		new MainController();
		}
		Toolkit.getDefaultToolkit().beep();
		 */

		Toolkit.getDefaultToolkit().beep();
		System.out.println("All Expes Done");
	}

}
