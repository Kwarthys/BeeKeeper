package com.beekeeper.controller.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MyLogger
{
	private BufferedWriter taskWriter;	
	
	private WritterThread writter = new WritterThread();
	
	FileWriter fw;
	
	public MyLogger()
	{
		try {
			fw = new FileWriter("tasks.txt", false);
			this.taskWriter = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writter.start();
	}
	
	public void logTask(int beeID, String taskName)
	{
		writter.submit(new LogEntry(taskWriter, beeID + " started " + taskName + "\n"));
	}

	public void log(int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		writter.submit(new LogEntry(taskWriter, turnIndex, beeID, beeTaskName, beePhysio));
	}

	public void closing()
	{
		if(fw != null)
		{
			writter.running = false;
			System.out.println("Asking thread to stop");
		}
	}
}

