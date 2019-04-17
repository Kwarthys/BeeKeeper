package com.beekeeper.launcher;

import com.beekeeper.controller.MainController;

public class Launcher {

	public static void main(String[] args)
	{	
		System.setProperty("sun.java2d.opengl", "true");
		new MainController();
	}

}
