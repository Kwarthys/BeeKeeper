package com.beekeeper.utils;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

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

			@Override
			public double getRange() {
				return 2*outterRadius;
			}

			@Override
			public double getOffset() {
				return center.x-outterRadius;
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

			@Override
			public double getRange() {
				return 2*radius;
			}

			@Override
			public double getOffset() {
				return center.x-radius;
			}
		};
	}
	
	public static Point2D.Double getPointInRule(CustomRule<Point2D.Double> rule)
	{
		double offset = rule.getOffset();
		double range = rule.getRange();
		
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
	
	
	/**
	 * 
	 * @param v
	 * @param down
	 * @param up
	 * @return Value v clamped between up and down
	 */
	public static double clamp(double v, double down, double up)
	{
		return v < down ? down : v > up ? up : v;
	}
	
	/**
	 * 
	 * @param v
	 * @return Value v clamped between 0 and 1
	 */
	public static double clamp(double v)
	{
		return clamp(v,0,1);
	}
	
	public static double getRotFromDir(double x2, double y2)
	{
		double angle = Math.atan2(y2, x2);
		//System.out.println("going " + x2 + ";" + y2 + " -> " + Math.toDegrees(angle));
		return angle;
	}
	
	public static double distance(Point p1, Point p2)
	{
		//System.out.println(p1 + " " + p2);
		if(p1 == null || p2 == null)
		{
			System.err.println("null in MyUtils");
		}
		return Point2D.Double.distance(p1.x, p1.y, p2.x, p2.y);
	}
	
	public static <T> void showList(ArrayList<T> list)
	{		
		for(int i = 0; i < list.size(); ++i)
		{
			System.out.print(list.get(i) + " ");
		}
		System.out.println();
	}
	
	public static <T> void switchElementsInList(ArrayList<T> list, int index1, int index2)
	{
		if(index1 > index2)
		{
			int tmp = index1;
			index1 = index2;
			index2 = tmp;
		}
		
		T tmp = list.remove(index1);
		list.add(index1,list.remove(index2-1));
		list.add(index2, tmp);
	}
	
	public static <k,v> void showSexyHashMap(HashMap<k,v> map)
	{
		StringBuffer sb = new StringBuffer();

		map.forEach((key, value) -> {
			sb.append(key);
			sb.append(": ");
			sb.append(value);
			sb.append(".\n");
		});
		
		System.out.println(sb.toString());
	}
}
