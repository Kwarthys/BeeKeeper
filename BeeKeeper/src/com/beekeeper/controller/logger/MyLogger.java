package com.beekeeper.controller.logger;

import com.beekeeper.parameters.ModelParameters;

public class MyLogger
{
	boolean started = false;
	
	private WriterThread writer;
	
	public MyLogger()
	{
		writer = new WriterThread(getParam());
	}
	
	public void logTask(int beeID, String taskName)
	{
		submit(new LogEntry(beeID + " started " + taskName + "\n"));
	}

	public void log(int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		submit(new LogEntry(turnIndex, beeID, beeTaskName, beePhysio));
	}

	public void log(String log)
	{
		submit(new LogEntry(log));
	}
	
	private void submit(LogEntry entry)
	{
		if(!started)
		{
			writer.start();
			started = true;
		}
		writer.submit(entry);
	}

	public void closing()
	{
		if(writer.running)
		{
			writer.running = false;
			System.out.println("Asking thread to stop");
		}
		else
		{
			System.out.println("Already closed");
		}
	}
	
	public Thread getThread()
	{
		return writer;
	}
	
	public boolean threadFinished()
	{
		System.out.println("writer.done " + writer.done);
		return writer.done;
	}

	public String getParam()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(ModelParameters.startMode);
		sb.append("_");
		sb.append(ModelParameters.NUMBER_BEES);
		sb.append("_");
		sb.append(ModelParameters.NUMBER_LARVAE);
		sb.append("_");
		sb.append(ModelParameters.SIMU_LENGTH);
		sb.append("_");
		sb.append(ModelParameters.expeIndex);
		
		return sb.toString();
	}
}

