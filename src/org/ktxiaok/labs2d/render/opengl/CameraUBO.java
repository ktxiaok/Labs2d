package org.ktxiaok.labs2d.render.opengl;

import org.joml.Matrix3f;

public class CameraUBO extends UniformBlockObject {
    public static final String VARNAME_VPMATRIX="viewProjectionMatrix";
    public static final String[] VARNAME_LIST=new String[]{VARNAME_VPMATRIX};
    public CameraUBO() {
        super(VARNAME_LIST);
    }
    public void updateVPMatrix(float[] data){
        updateData(VARNAME_VPMATRIX,data);
    }
    public void updateVPMatrix(Matrix3f m){
        updateVPMatrix(m.get(new float[9]));
    }
    public void init(DefaultShaderProgram defaultShaderProgram){
        bind();
        //bindShaderProgram(defaultShaderProgram.getID(),DefaultShaderProgram.UNIFORMBLOCK_CAMERA);
    }
}
