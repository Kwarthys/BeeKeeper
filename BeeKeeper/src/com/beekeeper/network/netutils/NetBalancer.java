package com.beekeeper.network.netutils;

public class NetBalancer {

	private NetBalancerCallBack cb;
	private int tickrate = 1;
	private int ticks = 0;
	
	public boolean affectedByCondition = false;
	
	public NetBalancer(NetBalancerCallBack cb, int tickrate, int offset, boolean isAffectedByFF)
	{
		this.cb = cb;
		this.tickrate = tickrate;
		this.ticks = offset % tickrate;
		
		affectedByCondition = isAffectedByFF;
	}
	
	public void call()
	{
		if(++ticks >= tickrate)
		{
			cb.call();
			ticks = 0;
		}
	}
}
