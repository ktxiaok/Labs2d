package org.ktxiaok.labs2d.system.util;

public class Rect {
	//(x,y)在矩形左下角
	public float x;
	public float y;
	public float w;
	public float h;
	public Rect(){
		this(0,0,0,0);
	}
	public Rect(float x,float y,float w,float h) {
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h; 
	}
	public boolean isOverlap(Rect r) {
		if(MathUtil.isIntervalOverlap(x, x+w, r.x, r.x+r.w)) {
			if(MathUtil.isIntervalOverlap(y, y+h, r.y, r.y+r.h)) {
				return true;
			}
		}
		return false;
	}
}
