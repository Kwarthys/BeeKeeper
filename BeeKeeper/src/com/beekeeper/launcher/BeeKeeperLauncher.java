package com.beekeeper.launcher;

import java.awt.Toolkit;

import com.beekeeper.controller.MainController;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.parameters.ModelParameters.StartMode;

public class BeeKeeperLauncher {

	public static void main(String[] args)
	{	
		System.setProperty("sun.java2d.opengl", "true");

		int simuLength = 1000;
		int reps = 2;

		
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
/*
		for(int i = 0; i<reps;++i)
		{	
			ModelParameters.paramExpe(150, 50, simuLength, StartMode.Random,i);
			new MainController();
		}

		for(int i = 0; i<reps;++i)
		{	
			ModelParameters.paramExpe(150, 300, simuLength, StartMode.Random,i);
			new MainController();
		}*/
		Toolkit.getDefaultToolkit().beep();
		System.out.println("All Expes Done");
	}

}
