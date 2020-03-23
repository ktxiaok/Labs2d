package org.ktxiaok.labs2d.render.opengl;

import org.joml.Matrix3f;
import org.ktxiaok.labs2d.system.util.MathUtil;
import org.ktxiaok.labs2d.system.util.Rect;

public class Camera {
    public Camera(float viewportWidth, float viewportHeight) {
        this.viewportWidth=viewportWidth;
        this.viewportHeight=viewportHeight;
    }
    //viewport的单位为游戏世界单位
    private float viewportWidth=0;
    private float viewportHeight=0;

    //OpenGL纹理坐标系:左下角(0,0),右上角(1,1)
    private float screenOffsetX=0;
    private float screenOffsetY=0;
    private float screenWidth=1;
    private float screenHeight=1;

    //游戏世界坐标系
    private float cameraPosX=0;
    private float cameraPosY=0;

    private float angle=0;

    public static final int LOCATE_NONE=0;
    public static final int LOCATE_ABSOLUTE=1;
    public static final int LOCATE_SMOOTH=2;
    public static final float DEFAULT_SMOOTH_LOCATE_FACTOR=0.1f;
    private float smoothLocateFactor=DEFAULT_SMOOTH_LOCATE_FACTOR;

    private int locateStrategy=LOCATE_ABSOLUTE;

    private int layer=0;

    private float targetX=0;
    private float targetY=0;

    private Matrix3f vpMatrix=new Matrix3f();
    private Matrix3f tempMatrix=new Matrix3f();

    public Rect getViewportRect() {
        float x=cameraPosX-viewportWidth/2;
        float y=cameraPosY-viewportHeight/2;
        return new Rect(x,y,viewportWidth,viewportHeight);
    }
    public float getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(float viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public float getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(float viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public float getScreenOffsetX() {
        return screenOffsetX;
    }

    public void setScreenOffsetX(float screenOffsetX) {
        this.screenOffsetX = screenOffsetX;
    }

    public float getScreenOffsetY() {
        return screenOffsetY;
    }

    public void setScreenOffsetY(float screenOffsetY) {
        this.screenOffsetY = screenOffsetY;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getCameraPosX() {
        return cameraPosX;
    }

    public void setCameraPosX(float cameraPosX) {
        this.cameraPosX = cameraPosX;
    }

    public float getCameraPosY() {
        return cameraPosY;
    }

    public void setCameraPosY(float cameraPosY) {
        this.cameraPosY = cameraPosY;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getLocateStrategy() {
        return locateStrategy;
    }
    public void setLocateStrategy(int strategy) {
        this.locateStrategy=strategy;
    }

    public void setTarget(float x,float y) {
        targetX=x;
        targetY=y;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }
    public float getTargetX() {
        return targetX;
    }
    public float getTargetY() {
        return targetY;
    }

    public void setSmoothLocateFactor(float factor) {
        smoothLocateFactor=factor;
    }
    public float getSmoothLocateFactor() {
        return smoothLocateFactor;
    }
    protected void locate() {
        switch(locateStrategy) {
            case LOCATE_NONE:
                break;
            case LOCATE_ABSOLUTE:
                cameraPosX=targetX;
                cameraPosY=targetY;
                break;
            case LOCATE_SMOOTH:
                float dx=targetX-cameraPosX;
                float dy=targetY-cameraPosY;
                float factor0=smoothLocateFactor;
                cameraPosX+=(dx*factor0);
                cameraPosY+=(dy*factor0);
                break;
        }
    }
    public Matrix3f getViewProjectionMatrix(){
        vpMatrix.identity();

//        vpMatrix.mulLocal(TransformUtil.getTranslationMatrix(tempMatrix,-cameraPosX,-cameraPosY));
//        if(!MathUtil.isEqual(angle,0)){
//            vpMatrix.mulLocal(TransformUtil.getRotationMatrix(tempMatrix,-angle));
//        }
//        vpMatrix.mulLocal(TransformUtil.getScaleMatrix(tempMatrix,2/viewportWidth,2/viewportHeight));
//        vpMatrix.mulLocal(TransformUtil.getTranslationMatrix(tempMatrix,1,1));
//        vpMatrix.mulLocal(TransformUtil.getScaleMatrix(tempMatrix,screenWidth,screenHeight));
//        vpMatrix.mulLocal(TransformUtil.getTranslationMatrix(tempMatrix,-1+2*screenOffsetX,-1+2*screenOffsetY));


        TransformUtil.getTranslationMatrix(tempMatrix,-cameraPosX,-cameraPosY).mul(vpMatrix,vpMatrix);
        if(!MathUtil.isEqual(angle,0)){
            TransformUtil.getRotationMatrix(tempMatrix,-angle).mul(vpMatrix,vpMatrix);
        }
        TransformUtil.getScaleMatrix(tempMatrix,2/viewportWidth,2/viewportHeight).mul(vpMatrix,vpMatrix);
        TransformUtil.getTranslationMatrix(tempMatrix,1,1).mul(vpMatrix,vpMatrix);
        TransformUtil.getScaleMatrix(tempMatrix,screenWidth,screenHeight).mul(vpMatrix,vpMatrix);
        TransformUtil.getTranslationMatrix(tempMatrix,-1+2*screenOffsetX,-1+2*screenOffsetY).mul(vpMatrix,vpMatrix);

        return vpMatrix;
    }
}
