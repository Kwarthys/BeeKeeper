package com.beekeeper.controller.logger;

import java.util.concurrent.LinkedBlockingQueue;

public class WritterThread extends Thread {

	private LinkedBlockingQueue<LogWork> work = new LinkedBlockingQueue<LogWork>();

	public void submit(LogWork w)
	{
		work.add(w);
	}

	@Override
	public void run() {
		while(this.isAlive())
		{
			try {
				LogWork w = work.take();
				w.writer.write(w.logString);
				//System.out.print(w.logString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
