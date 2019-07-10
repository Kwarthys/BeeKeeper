package com.beekeeper.utils;

import java.awt.geom.Point2D;

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
	
	public static double getRotFromDir(double x2, double y2)
	{
		/*
		double x1 = -1;
		double y1 = 0;
		double dot = Math.acos(x2*x1+y2*y1);
		double det = x2*y1 - y2*x1;
		double angle = Math.atan2(det, dot);
		*/
		//double angle = Math.acos(x2 / Math.sqrt(x2*x2 + y2*y2));
		double angle = Math.atan2(y2, x2);
		System.out.println("going " + x2 + ";" + y2 + " -> " + Math.toDegrees(angle));
		return angle;
	}
}
