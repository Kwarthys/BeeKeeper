package com.beekeeper.model.outside;

import java.awt.geom.Point2D;

public interface OutsideManagerServices
{
	public Point2D.Double getExitPoint();
	
	public boolean weatherPermitFlight();
	
	
}
