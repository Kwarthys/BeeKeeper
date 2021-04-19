package com.beekeeper.controller.logger;

import java.text.DecimalFormat;

import com.beekeeper.parameters.ModelParameters;

public class MyLogger
{
	boolean started = false;
	
	private WriterThread writer;
	
	public MyLogger()
	{
		String params = getParam();
		writer = new WriterThread(params);
		System.out.println("Created log file " + params);
	}
	
	public MyLogger(int beeID)
	{
		writer = new WriterThread("bees/" + getParam() + "_Bee" + beeID);
	}
	
	public void logTask(int beeID, String taskName)
	{
		submit(beeID + " started " + taskName + "\n");
	}

	public void log(int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(turnIndex);
		sb.append(",");
		sb.append(beeID);
		sb.append(",");
		sb.append(beeTaskName);
		sb.append(",");
		sb.append(beePhysio);
		sb.append("\n");
		
		submit(sb.toString());
	}

	public void log(String log)
	{
		submit(log);
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
		sb.append("\n");
		
		submit(sb.toString());
	}
	
	private void submit(String entry)
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
			writer.submit("stop");
			//System.out.println("Asking thread to stop");
		}
		else
		{
			//System.out.println("Already closed");
		}
	}
	
	public Thread getThread()
	{
		return writer;
	}
	
	public boolean threadFinished()
	{
		//System.out.println("writer.done " + writer.done);
		return writer.done;
	}

	public String getParam()
	{
		StringBuffer sb = new StringBuffer();
		
		DecimalFormat df = new DecimalFormat("#.####");

		sb.append(ModelParameters.identifier);
		sb.append("_");
		sb.append(ModelParameters.getModelState());
		sb.append("_");
		sb.append(ModelParameters.startMode);
		sb.append("_");
		sb.append(ModelParameters.NUMBER_BEES);
		sb.append("_");
		sb.append(ModelParameters.NUMBER_LARVAE);
		sb.append("_");
		sb.append(ModelParameters.SIMU_LENGTH);
		sb.append("_");
		sb.append(ModelParameters.SIMU_ACCELERATION);
		sb.append("_LEoEmC");
		sb.append(df.format(ModelParameters.LARVA_EO_EMISSION_COEF));
		
		return sb.toString();
	}
}

