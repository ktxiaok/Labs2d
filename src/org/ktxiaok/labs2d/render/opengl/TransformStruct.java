package org.ktxiaok.labs2d.render.opengl;

import org.joml.Matrix3f;
import org.joml.Vector2f;

public class TransformStruct {
    private float tx,ty;
    private float sx,sy;
    private float angle;
    public TransformStruct() {
        tx=0;
        ty=0;
        sx=1;
        sy=1;
        angle=0;
    }
    public TransformStruct(TransformStruct t) {
        set(t);
    }
    public TransformStruct set(TransformStruct t) {
        tx=t.tx;
        ty=t.ty;
        sx=t.sx;
        sy=t.sy;
        angle=t.angle;
        return this;
    }
    public float getPositionX() {
        return tx;
    }
    public float getPositionY() {
        return ty;
    }

    public float getScaleX() {
        return sx;
    }
    public float getScaleY() {
        return sy;
    }
    public float getAngle() {
        return angle;
    }
    public TransformStruct setPositionX(float x) {
        tx=x;
        return this;
    }
    public TransformStruct setPositionY(float y) {
        ty=y;
        return this;
    }
    public TransformStruct setPosition(float x,float y) {
        tx=x;
        ty=y;
        return this;
    }
    public TransformStruct setScaleX(float x) {
        sx=x;
        return this;
    }
    public TransformStruct setScaleY(float y) {
        sy=y;
        return this;
    }
    public TransformStruct setScale(float x,float y) {
        sx=x;
        sy=y;
        return this;
    }
    public TransformStruct setAngle(float _angle) {
        angle=_angle;
        return this;
    }
    public TransformStruct rotate(float a) {
        angle+=a;
        return this;
    }
    public TransformStruct scale(float _sx,float _sy) {
        sx*=_sx;
        sy*=_sy;
        return this;
    }
    public TransformStruct translate(float _tx,float _ty) {
        tx+=_tx;
        ty+=_ty;
        return this;
    }
    //先缩放、后旋转、再平移
    public void transform(Vector2f v0) {
        v0.x*=sx;
        v0.y*=sy;
        TransformUtil.rotate(v0,angle);
        v0.x+=tx;
        v0.y+=ty;
    }
    public void combine(TransformStruct transform){
        tx+=transform.tx;
        ty+=transform.ty;
        sx*=transform.sx;
        sy*=transform.sy;
        angle+=transform.angle;
    }

    private Matrix3f modelMatrix=null;
    private Matrix3f tempMatrix=null;
    //s->r->t;
    public Matrix3f getModelMatrix() {
        if(modelMatrix==null){
            modelMatrix=new Matrix3f().identity();
            tempMatrix=new Matrix3f();
        }
        modelMatrix.identity();
//        modelMatrix.mulLocal(TransformUtil.getScaleMatrix(tempMatrix,sx,sy));
//        modelMatrix.mulLocal(TransformUtil.getRotationMatrix(tempMatrix,angle));
//        modelMatrix.mulLocal(TransformUtil.getTranslationMatrix(tempMatrix,tx,ty));


        TransformUtil.getScaleMatrix(tempMatrix,sx,sy).mul(modelMatrix,modelMatrix);
        TransformUtil.getRotationMatrix(tempMatrix,angle).mul(modelMatrix,modelMatrix);
        TransformUtil.getTranslationMatrix(tempMatrix,tx,ty).mul(modelMatrix,modelMatrix);
        return modelMatrix;
    }
}
