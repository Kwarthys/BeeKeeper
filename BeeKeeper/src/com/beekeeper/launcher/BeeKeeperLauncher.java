package com.beekeeper.launcher;

import com.beekeeper.controller.MainController;

public class BeeKeeperLauncher {

	public static void main(String[] args)
	{	
		System.setProperty("sun.java2d.opengl", "true");
		long start = System.nanoTime();
		new MainController();
		System.out.println("Prog took " + (System.nanoTime() - start)/1000000000 + "s.");
		
		/*
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
		System.out.println("All Expes Done");
	}

}
