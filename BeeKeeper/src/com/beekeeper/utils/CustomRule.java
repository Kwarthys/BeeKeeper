package com.beekeeper.utils;

public interface CustomRule <T> {
	public boolean isValid(T t);
	public double getRange();
	public double getOffset();
}
