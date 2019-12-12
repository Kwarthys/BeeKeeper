package com.beekeeper.controller.logger;

import java.io.BufferedWriter;

public class LogEntry
{
	String logString;
	BufferedWriter writer;
	
	public LogEntry(BufferedWriter taskWriter, String logString)
	{
		this.logString = logString;
		this.writer = taskWriter;
	}

	public LogEntry(BufferedWriter taskWriter, int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(turnIndex);
		sb.append(",");
		sb.append(beeID);
		sb.append(",");
		sb.append(beeTaskName);
		sb.append(",");
		sb.append(beePhysio);
		sb.append(";\n");
		
		this.logString = sb.toString();
		this.writer = taskWriter;
	}
}
