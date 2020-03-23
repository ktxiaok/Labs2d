package org.ktxiaok.labs2d.render.opengl;

import android.util.Log;

import org.joml.Matrix3f;

public class BasicShaderProgram extends ShaderProgram {
    public static final String UNIFORM_MODEL_MATRIX="modelMatrix";
    public static final String UNIFORM_VIEW_PROJECTION_MATRIX="viewProjectionMatrix";
    public static final String UNIFORM_IGNORE_CAMERA="ignoreCamera";

    public static final String UNIFORM_MAIN_SAMPLER="mainSampler";

    private float[] tempArray=new float[9];

    public BasicShaderProgram(String vertexShaderCode, String fragmentShaderCode){
        super();
        try{
            attachShader(Shader.createVertexShader(vertexShaderCode));
            attachShader(Shader.createFragmentShader(fragmentShaderCode));
            linkProgram();
        }catch(OpenGLException e){
            Log.e("OpenGL","OpenGLException",e);
        }
        bind();
        setUniform(UNIFORM_MAIN_SAMPLER,0);
    }
    public void updateModelMatrix(TransformStruct transform){
        setUniformMatrix3f(UNIFORM_MODEL_MATRIX,transform.getModelMatrix().get(tempArray));
    }
    public void updateViewProjectionMatrix(Matrix3f vp){
        setUniformMatrix3f(UNIFORM_VIEW_PROJECTION_MATRIX,vp.get(tempArray));
    }
    public void setIgnoreCamera(boolean ignoreCamera){
        if(ignoreCamera){
            setUniform(UNIFORM_IGNORE_CAMERA,1);
        }else{
            setUniform(UNIFORM_IGNORE_CAMERA,0);
        }
    }
}
