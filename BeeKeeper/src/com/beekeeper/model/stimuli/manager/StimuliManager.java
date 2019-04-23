package com.beekeeper.model.stimuli.manager;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.beekeeper.model.agent.EmptyBee;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.parameters.ModelParameters;

public class StimuliManager
{
	private ArrayList<EmptyBee> agents;
	
	public StimuliManager(ArrayList<EmptyBee> agents)
	{
		this.agents = agents;
	}

	public StimuliMap getAllStimuliAround(Point2D.Double position)
	{
		StimuliMap perception = new StimuliMap();

		for(EmptyBee bee : agents)
		{
			StimuliLoad load = bee.getExternalStimuli();
			addPerceptionOf(load, perception, position);
		}		
		
		return perception;
	}
	


	public void addPerceptionOf(StimuliLoad load, StimuliMap perception, Point2D.Double receiverPos)
	{
		double distance = load.emiterPos.distance(receiverPos);
		
		load.getMapCopy().forEach((type, stimulus) -> {
			double sensedAmount = 0;
			
			if(distance < 1)
			{
				sensedAmount = stimulus.getAmount();
			}
			else
			{
				sensedAmount = stimulus.getAmount() * Math.pow(stimulus.getSmellRange(), distance);
			}
			
			if(sensedAmount >= ModelParameters.SMELL_THRESHOLD)
			{				
				perception.addAmount(type, sensedAmount);
			}
		});
	}
	
	private Point2D.Double getPosOfStrongestEmitter(Double sensorPos, Stimulus type)
	{
		double strongestAmount = 0;
		Point2D.Double strongestPos = null;
		for(EmptyBee bee : agents)
		{
			StimuliLoad load = bee.getExternalStimuli();
			
			if(strongestPos == null || strongestAmount < load.getSensedStimulusAmount(type, load.emiterPos.distance(sensorPos)))
			{
				strongestAmount = load.getSensedStimulusAmount(type, load.emiterPos.distance(sensorPos));
				strongestPos = load.emiterPos;
			}
		}
		
		return strongestPos;
	}
	
	public void updateStimuli()
	{
		for(EmptyBee bee : agents)
		{
			bee.getExternalStimuli().evaporate();
		}
	}

	public StimuliManagerServices getNewServices()
	{
		return new StimuliManagerServices() {

			@Override
			public StimuliMap getAllStimuliAround(Double position) {
				return StimuliManager.this.getAllStimuliAround(position);
			}

			@Override
			public Point2D.Double getPosOfStrongestEmitter(Double sensorPos, Stimulus type) {
				return StimuliManager.this.getPosOfStrongestEmitter(sensorPos, type);
			}
		};
	}
}
