package org.ktxiaok.labs2d.system.util;

public class MathUtil {
  private MathUtil() {
	  
  }
  public static final float SMALL_NUM=0.00001f;
  
  /*
   * Convert the range of an angle to [0,2PI]
   */
  public static float angleRangeConvert(float angle) {
	  float pi2=2*(float)Math.PI;
	  int c=(int)(angle/pi2);
	  if(angle<0) {
		  float d0=pi2*c-angle;
		  return (pi2-d0);
	  }else {
		  float d0=angle-pi2*c;
		  return d0;
	  }
	  
  }
  public static float atan2(float x,float y) {
	  return (float)Math.atan2(y,x);
  }
  public static float atan(float a) {
	  return (float)Math.atan(a);
  }
  public static float sin(float a) {
	  return (float)Math.sin(a);
  }
  public static float cos(float a) {
	  return (float)Math.cos(a);
  }
  public static float sqrt(float a) {
	  return (float)Math.sqrt(a);
  }
  public static float pow(float baseNum,float exponent) {
	  return (float)Math.pow(baseNum,exponent);
  }
  public static float abs(float a) {
	  return Math.abs(a);
  }
  public static float random() {
	  return (float)Math.random();
  }
  public static float random(float start,float end) {
	  return (start+(end-start)*random());
  }
  public static boolean isEqual(float a, float b) {
	  float d=a-b;
	  if(d>=-SMALL_NUM&&d<=SMALL_NUM) {
		  return true;
	  }
	  return false;
  }

  public static boolean isIntervalOverlap(float start1,float end1,float start2,float end2) {
	  if(end1<start2||end2<start1) {
		  return false;
	  }
	  return true;
  }
  
  
  public static int sgn(float a){
	  if(a>0) {
		  return 1;
	  }
	  if(a<0) {
		  return -1;
	  }
	  return 0;
  }
	public static boolean chooseClockwise(float currentAngle,float expectedAngle) {
		float d=expectedAngle-currentAngle;
		boolean clockwise=d>=0?false:true;
		if(MathUtil.abs(d)>Math.PI) {
			clockwise=!clockwise;
		}
		return clockwise;
	}

	public static float intervalMap(float srcIntervalStart,float srcIntervalEnd,
                                    float destIntervalStart,float destIntervalEnd,float value){
      float srcLen=srcIntervalEnd-srcIntervalStart;
      float destLen=destIntervalStart-destIntervalEnd;
      return ((value-srcIntervalStart)/srcLen*destLen+destIntervalStart);
    }

}
