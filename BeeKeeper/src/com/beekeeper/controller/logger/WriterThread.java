package com.beekeeper.controller.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class WriterThread extends Thread {

	private LinkedBlockingQueue<LogEntry> work = new LinkedBlockingQueue<LogEntry>();
	public volatile boolean running = true;
	public volatile boolean done = false;
	
	private BufferedWriter writer;

	public void submit(LogEntry w)
	{
		work.add(w);
	}
	
	public WriterThread(String param)
	{
		FileWriter fw;
		try {
			fw = new FileWriter("expe/"+param+".csv", false);
			System.out.println("Started writing for : expe/"+param+".csv");
			this.writer = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(this.isAlive() && running)
		{
			while(!work.isEmpty())
			{
				try {
					LogEntry w = work.poll();
					if(w != null)
					{
						writer.write(w.logString);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			writer.close();
			System.out.println("ClosedNicely");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		done = true;
	}
}
