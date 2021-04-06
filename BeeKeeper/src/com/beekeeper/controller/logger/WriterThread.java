package com.beekeeper.controller.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import com.beekeeper.parameters.ModelParameters;

public class WriterThread extends Thread {

	private ArrayBlockingQueue<String> work = new ArrayBlockingQueue<String>((int) ((ModelParameters.NUMBER_BEES + ModelParameters.NUMBER_LARVAE) * 1.5));
	public volatile boolean running = true;
	public volatile boolean done = false;
	
	private BufferedWriter writer;

	public void submit(String w)
	{
		work.add(w);
	}
	
	public WriterThread(String param)
	{
		FileWriter fw;
		try {
			fw = new FileWriter("expe/"+param+".csv", false);
			//System.out.println("Started writing for : expe/"+param+".csv");
			this.writer = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(this.isAlive() && running)
		{			
			try {				
				StringBuffer sb = new StringBuffer();
				//int count = 0;
				
				String s = work.take();
				
				if(!s.equals("stop"))
				{
					//count++;
					sb.append(s);
				}
				else
				{
					//System.out.println("stop");
				}
				
				while(!work.isEmpty())
				{
					s = work.take();
					
					if(!s.equals("stop"))
					{
						//count++;
						sb.append(s);
					}
					else
					{
						//System.out.println("stop");
					}
				}
				
				writer.write(sb.toString());
				//System.out.println("THE LOGGER LOGGING LOGS TO THE LOGS : Writter wrote");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/*
			while(!work.isEmpty())
			{
				try {
					String w = work.poll();
					if(w != null)
					{
						writer.write(w);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			*/
		}
		
		try {
			writer.close();
			//System.out.println("ClosedNicely");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		done = true;
	}
}
