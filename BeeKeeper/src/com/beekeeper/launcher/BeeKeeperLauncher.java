package com.beekeeper.launcher;

import java.awt.Toolkit;

import com.beekeeper.controller.MainController;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.parameters.ModelParameters.StartMode;

public class BeeKeeperLauncher {

	private static int simuLength = 8000;
	private static int reps = 5;

	public static void main(String[] args)
	{	
		System.setProperty("sun.java2d.opengl", "true");

		ModelParameters.BYPASS_MOTIVATION = false;
		ModelParameters.BYPASS_PHYSIOLOGY = false;
		allScenarios();
		
		Toolkit.getDefaultToolkit().beep();
		System.out.println("All Expes Done");
	}
	
	
	public static void sameScenarAllMods(int nbBees, int nbLarvae, int simuLength, StartMode mode)
	{
		for(int i = 0; i<reps;++i)
		{	
			ModelParameters.BYPASS_MOTIVATION = false;
			ModelParameters.BYPASS_PHYSIOLOGY = false;
			ModelParameters.paramExpe(nbBees, nbLarvae, simuLength, mode,i);
			new MainController();
		}
		for(int i = 0; i<reps;++i)
		{	
			ModelParameters.BYPASS_MOTIVATION = false;
			ModelParameters.BYPASS_PHYSIOLOGY = true;
			ModelParameters.paramExpe(nbBees, nbLarvae, simuLength, mode,i);
			new MainController();
		}
		for(int i = 0; i<reps;++i)
		{	
			ModelParameters.BYPASS_MOTIVATION = true;
			ModelParameters.BYPASS_PHYSIOLOGY = true;
			ModelParameters.paramExpe(nbBees, nbLarvae, simuLength, mode,i);
			new MainController();
		}
	}
	
	
	public static void allScenarios()
	{
		for(int i = 0; i<reps;++i)
		{			
			ModelParameters.paramExpe(150, 150, simuLength, StartMode.Random,i);
			new MainController();
		}
		for(int i = 0; i<reps;++i)
		{			
			ModelParameters.paramExpe(150, 150, simuLength, StartMode.NewBorn,i);
			new MainController();
		}
		for(int i = 0; i<reps;++i)
		{			
			ModelParameters.paramExpe(150, 150, simuLength, StartMode.Old,i);
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
	}

}
