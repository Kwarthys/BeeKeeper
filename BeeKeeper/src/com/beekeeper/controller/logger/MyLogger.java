package com.beekeeper.controller.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MyLogger
{
	private BufferedWriter taskWriter;	
	
	private WritterThread writter = new WritterThread();
	
	public MyLogger()
	{
		FileWriter fw;
		try {
			fw = new FileWriter("tasks.txt", false);
			this.taskWriter = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writter.start();
	}
	
	public void logTask(int beeID, String taskName)
	{
		writter.submit(new LogWork(taskWriter, beeID + " started " + taskName + "\n"));
	}
}
