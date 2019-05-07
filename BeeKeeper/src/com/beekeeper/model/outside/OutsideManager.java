package com.beekeeper.model.outside;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

public class OutsideManager
{
	protected ArrayList<Point2D.Double> sourcePos = new ArrayList<>();
	
	
	public boolean weatherPermitFlight()
	{
		return true;
	}
	
	public Point2D.Double getExitPoint()
	{
		return new Point2D.Double(0,0);
	}
	
	
	public OutsideManagerServices getNewServices()
	{
		return new OutsideManagerServices() {			
			@Override
			public boolean weatherPermitFlight() {
				return OutsideManager.this.weatherPermitFlight();
			}
			
			@Override
			public Double getExitPoint() {
				return OutsideManager.this.getExitPoint();
			}
		};
	}
}
