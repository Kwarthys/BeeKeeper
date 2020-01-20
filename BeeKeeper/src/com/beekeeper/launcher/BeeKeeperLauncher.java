package com.beekeeper.launcher;

import com.beekeeper.controller.MainController;
import com.beekeeper.parameters.ModelParameters;
import com.beekeeper.parameters.ModelParameters.StartMode;

public class BeeKeeperLauncher {

	public static void main(String[] args)
	{	
		System.setProperty("sun.java2d.opengl", "true");
		
		int simuLength = 5000;
		
		ModelParameters.paramExpe(150, 150, simuLength, StartMode.Random);
		new MainController();
		
		ModelParameters.paramExpe(150, 150, simuLength, StartMode.Old);
		new MainController();
		
		ModelParameters.paramExpe(150, 150, simuLength, StartMode.NewBorn);
		new MainController();
		
		ModelParameters.paramExpe(150, 50, simuLength, StartMode.Random);
		new MainController();
		
		ModelParameters.paramExpe(150, 300, simuLength, StartMode.Random);
		new MainController();
		
		System.out.println("All Expes Done");
	}

}
