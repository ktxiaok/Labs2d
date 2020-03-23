package org.ktxiaok.labs2d.render.opengl;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.ktxiaok.labs2d.system.util.MathUtil;

public class TransformUtil {
    private TransformUtil(){

    }
    public static void rotate(Vector2f v0,float angle) {
        float sina=MathUtil.sin(angle);
        float cosa=MathUtil.cos(angle);
        float x1=v0.x*cosa-v0.y*sina;
        float y1=v0.x*sina+v0.y*cosa;
        v0.x=x1;
        v0.y=y1;
    }
    public static Matrix3f getTranslationMatrix(Matrix3f t,float tx, float ty) {
        t.set(1,0,0,0,1,0,tx,ty,1);
        return t;
    }
    public static Matrix3f getRotationMatrix(Matrix3f r,float angle) {
        float sina=MathUtil.sin(angle);
        float cosa=MathUtil.cos(angle);
        r.set(cosa,sina,0,-sina,cosa,0,0,0,1);
        return r;
    }
    public static Matrix3f getScaleMatrix(Matrix3f s,float sx,float sy) {
        s.set(sx,0,0,0,sy,0,0,0,1);
        return s;
    }

}
