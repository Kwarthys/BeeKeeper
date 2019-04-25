package com.beekeeper.model.comb.cell;

import java.awt.geom.Point2D;

import com.beekeeper.model.stimuli.StimuliLoad;
import com.beekeeper.model.stimuli.declarations.FoodSmellStimulus;

public class CombCell
{
	protected Point2D.Double position;
	protected StimuliLoad stimuliLoad;

	public CombCell()
	{
		this.position = new Point2D.Double(100+Math.random()*300, 100+Math.random()*300);
		this.stimuliLoad = new StimuliLoad(this.position);
	}
	
	public void live()
	{
		this.stimuliLoad.emit(new FoodSmellStimulus(1));
	}
	
	public Point2D.Double getPosition(){return this.position;}
	public StimuliLoad getExternalStimuli() {return this.stimuliLoad;}
}
