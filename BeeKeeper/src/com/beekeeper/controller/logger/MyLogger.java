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

	public void log(String... logs)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(logs[0]);
		for(int i = 1; i < logs.length; ++i)
		{
			sb.append(",");
			sb.append(logs[i]);
		}
		
		submit(new LogEntry(sb.toString()));
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

		sb.append(ModelParameters.identifier);
		sb.append("_");
		sb.append(ModelParameters.getModelState());
		sb.append("_");
		sb.append(ModelParameters.startMode);
		sb.append("_");
		sb.append(ModelParameters.NUMBER_BEES);
		sb.append("_");
		sb.append(ModelParameters.NUMBER_LARVAE * ModelParameters.NUMBER_FRAMES);
		sb.append("_");
		sb.append(ModelParameters.SIMU_LENGTH);
		sb.append("_EOem");
		sb.append(ModelParameters.EOEmissionCoef);
		sb.append("_HJRed");
		sb.append(ModelParameters.hjReduction);
		sb.append("_LEOem");
		sb.append(ModelParameters.LARVA_EO_TIMELY_EMMISION);
		
		return sb.toString();
	}
}

