package org.ktxiaok.labs2d.system.util;

public class Timer {
	private long totalTime=0;
	private long startTime=0;
	public Timer() {
		
	}
	public void start() {
		totalTime=0;
		startTime=System.nanoTime();
	}
	public Timer record() {
		totalTime=System.nanoTime()-startTime;
		return this;
	}
	public float getSecond() {
		return totalTime/1000000000f;
	}
	public float getMillisecond() {
		return totalTime/1000000f;
	}
}
