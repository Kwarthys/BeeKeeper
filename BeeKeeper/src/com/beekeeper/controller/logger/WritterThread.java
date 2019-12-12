package com.beekeeper.controller.logger;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class WritterThread extends Thread {

	private LinkedBlockingQueue<LogEntry> work = new LinkedBlockingQueue<LogEntry>();
	public volatile boolean running = true;

	public void submit(LogEntry w)
	{
		work.add(w);
	}

	@Override
	public void run() {
		while(this.isAlive() && running)
		{
			try {
				LogEntry w = work.take();
				w.writer.write(w.logString);
				//System.out.print(w.logString);
				System.out.println(work.size());					
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			work.take().writer.close();
			System.out.println("ClosedNicely");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
