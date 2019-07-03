package com.beekeeper.utils;

import java.awt.geom.Point2D;

import com.beekeeper.parameters.ModelParameters;

public class MyUtils
{
	public static CustomRule<Point2D.Double> getDonutPointRule(Point2D.Double center, double innerRadius, double outterRadius)
	{
		return new CustomRule<Point2D.Double>() {			
			@Override
			public boolean isValid(Point2D.Double t) {
				double distance = t.distance(center);
				return distance >= innerRadius && distance < outterRadius;
			}
		};
	}

	public static CustomRule<Point2D.Double> getCirclePointRule(Point2D.Double center, double radius)
	{
		return new CustomRule<Point2D.Double>() {			
			@Override
			public boolean isValid(Point2D.Double t) {
				return t.distance(center) <= radius;
			}
		};
	}
	
	public static Point2D.Double getPointInRule(double range, double offset, CustomRule<Point2D.Double> rule)
	{
		Point2D.Double pointCandidate = new Point2D.Double(offset + Math.random()*range, offset + Math.random()*range);
		while(!rule.isValid(pointCandidate))
		{
			pointCandidate = new Point2D.Double(offset + Math.random()*range, offset + Math.random()*range);
		}
		
		return pointCandidate;
	}
	
	
	public static double sigmoid(double s, double t)
	{
		return s*s / ( s*s + t*t);
	}
	
	
	public static double clamp(double v, double down, double up)
	{
		return v < down ? down : v > up ? up : v;
	}
}
