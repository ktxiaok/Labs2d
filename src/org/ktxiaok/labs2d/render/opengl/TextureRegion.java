package org.ktxiaok.labs2d.render.opengl;

import org.joml.Vector2f;
import org.ktxiaok.labs2d.system.util.MathUtil;

public class TextureRegion {
    private float offsetX;
    private float offsetY;
    private float tilingX;
    private float tilingY;
    /*
     * 原本图片是顺时针旋转90度，这里的处理是再逆时针旋转90度恢复原状
     */
    private boolean rotate =false;
    public TextureRegion() {
        this(0,0,1,1);
    }
    public TextureRegion(float offsetX, float offsetY, float tilingX, float tilingY) {
        this.offsetX=offsetX;
        this.offsetY=offsetY;
        this.tilingX=tilingX;
        this.tilingY=tilingY;
    }
    public boolean isEmpty() {
        if(!rotate) {
            if(MathUtil.isEqual(offsetX,0)) {
                if(MathUtil.isEqual(offsetY,0)) {
                    if(MathUtil.isEqual(tilingX,1)) {
                        if(MathUtil.isEqual(tilingY,1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public float getOffsetX() {
        return offsetX;
    }
    public float getOffsetY() {
        return offsetY;
    }
    public float getTilingX() {
        return tilingX;
    }
    public float getTilingY() {
        return tilingY;
    }
    public TextureRegion setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }
    public TextureRegion setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }
    public TextureRegion setTilingX(float tilingX) {
        this.tilingX = tilingX;
        return this;
    }
    public TextureRegion setTilingY(float tilingY) {
        this.tilingY = tilingY;
        return this;
    }
    public TextureRegion setRotate(boolean rotate) {
        this.rotate = rotate;
        return this;
    }
    public boolean isRotate() {
        return rotate;
    }

    public void convertTexcoord(float[] texcoordConverted,int texcoordConvertedOffset,
                                   float[] originTexcoord,int orginTexcoordOffset,int length) {
        Vector2f splitOrigin=new Vector2f(getOffsetX(),getOffsetY());
        Vector2f splitSize=new Vector2f(getTilingX(),getTilingY());
        for(int i=0;i<length;i+=2) {
            Vector2f originTexcoordPoint;
            if(isRotate()) {
                originTexcoordPoint=new Vector2f(originTexcoord[orginTexcoordOffset+i+1],
                        1-originTexcoord[orginTexcoordOffset+i]);
            }else {
                originTexcoordPoint=new Vector2f(originTexcoord[orginTexcoordOffset+i],
                        originTexcoord[orginTexcoordOffset+i+1]);
            }
            Vector2f targetPos=new Vector2f(splitOrigin).add(splitSize.x*originTexcoordPoint.x,
                    splitSize.y*originTexcoordPoint.y);
            texcoordConverted[texcoordConvertedOffset+i]=targetPos.x;
            texcoordConverted[texcoordConvertedOffset+i+1]=targetPos.y;
        }
    }
    public void convertTexcoord(float[] texcoordConverted,float[] orginTexcoord){
        convertTexcoord(texcoordConverted,0,orginTexcoord,0,orginTexcoord.length);
    }

}
