package com.beekeeper.controller.logger;

import java.io.BufferedWriter;

public class LogWork
{
	String logString;
	BufferedWriter writer;
	
	public LogWork(BufferedWriter taskWriter, String logString)
	{
		this.logString = logString;
		this.writer = taskWriter;
	}
}
