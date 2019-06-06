package com.beekeeper.model.stimuli.manager;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.EmitterAgent;
import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.parameters.ModelParameters;

public class StimuliManager
{
	private ArrayList<Agent> agents = new ArrayList<>();
	
	public StimuliManager(ArrayList<Agent> agents)
	{
		this.agents = agents;
	}

	public StimuliMap getAllStimuliAround(Point2D.Double position)
	{
		StimuliMap perception = new StimuliMap();

		for(Agent bee : agents)
		{
			StimuliLoad load = ((EmitterAgent)bee).getStimuliLoad();
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
		for(Agent bee : agents)
		{
			StimuliLoad load = ((EmitterAgent)bee).getStimuliLoad();
			
			if(strongestPos == null || strongestAmount < load.getSensedStimulusAmount(type, load.emiterPos.distance(sensorPos)))
			{
				strongestAmount = load.getSensedStimulusAmount(type, load.emiterPos.distance(sensorPos));
				strongestPos = load.emiterPos;
			}
		}
		
		if(strongestAmount == 0)
		{
			return null;
		}
		return strongestPos;
	}
	
	public void updateStimuli()
	{
		for(Agent bee : agents)
		{
			((EmitterAgent)bee).getStimuliLoad().evaporate();
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
