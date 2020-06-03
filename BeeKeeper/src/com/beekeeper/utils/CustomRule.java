package com.beekeeper.utils;

import java.awt.geom.Point2D;

public interface CustomRule <T> {
	public boolean isValid(T t);
	public Point2D.Double getRange();
	public Point2D.Double getOffset();
}
