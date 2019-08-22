package com.beekeeper.model.hive;

import com.beekeeper.parameters.ModelParameters;

public class BeeHive
{
	private double temperature;
	
	public BeeHive()
	{
		this.temperature = 34;
	}
	
	public void computeInternalTemperature(double outsideTemp)
	{
		//System.out.print("Inside : " + getTemperature() + " | outside " + outsideTemp + " -> ");
		this.temperature += (outsideTemp - this.temperature)/ModelParameters.HIVE_THERMAL_RESISTANCE;
		//System.out.println(getTemperature());
	}
	
	public double getTemperature()
	{
		return this.temperature;
	}
	
	public BeeHiveServices getServices()
	{
		return new BeeHiveServices() {			
			@Override
			public double getTemperature() {
				return BeeHive.this.temperature;
			}
		};
	}

}
