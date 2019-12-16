package com.beekeeper.controller.logger;

public class LogEntry
{
	String logString;
	
	public LogEntry(String logString)
	{
		this.logString = logString + "\n";
	}

	public LogEntry(int turnIndex, int beeID, String beeTaskName, double beePhysio)
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
		
		this.logString = sb.toString();
	}
}
