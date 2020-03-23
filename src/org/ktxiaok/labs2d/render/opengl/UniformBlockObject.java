package org.ktxiaok.labs2d.render.opengl;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class UniformBlockObject extends GLObject {
    private int bindingPoint;
    private Map<String,Integer> varOffsetMap;
    private boolean initFinished;
    private DynamicBuffer tempBuffer;
    public UniformBlockObject(String[] varNames){
        int[] id0=new int[1];
        gl.glGenBuffers(1,id0,0);
        setID(id0[0]);
        bindingPoint=getContext().allocateUBOBindingPoint();
        varOffsetMap=new HashMap<>();
        for(String varName0:varNames){
            varOffsetMap.put(varName0,null);
        }
        initFinished=false;
        tempBuffer=new DynamicBuffer();
    }
    @Override
    protected GLObject getBindingObject() {
        return getContext().getBindingUniformBlockObject();
    }

    @Override
    protected void setBindingObject(GLObject glObject) {
        getContext().setBindingUniformBlockObject((UniformBlockObject)glObject);
    }

    @Override
    protected void unbindObject(GLObject bindingObject) {
        gl.glBindBuffer(gl.GL_UNIFORM_BUFFER,0);
    }

    @Override
    protected void bindObject() {
        gl.glBindBuffer(gl.GL_UNIFORM_BUFFER,getID());
    }

    public void bindShaderProgram(int shaderProgramID,String uniformBlockName){
        checkBinding();
        int uniformBlockIndex=gl.glGetUniformBlockIndex(shaderProgramID,uniformBlockName);
        gl.glUniformBlockBinding(shaderProgramID,uniformBlockIndex,bindingPoint);
        if(!initFinished){
            int[] blockSize=new int[1];
            gl.glGetActiveUniformBlockiv(shaderProgramID,uniformBlockIndex,gl.GL_UNIFORM_BLOCK_DATA_SIZE,blockSize,0);
            gl.glBufferData(gl.GL_UNIFORM_BUFFER,blockSize[0],null,gl.GL_DYNAMIC_DRAW);
            gl.glBindBufferBase(gl.GL_UNIFORM_BUFFER,bindingPoint,getID());

            String[] uniformNames=new String[varOffsetMap.size()];
            int i=0;
            for(String varName0:varOffsetMap.keySet()){
                uniformNames[i]=varName0;
                i++;
            }
            int[] uniformIndices=new int[uniformNames.length];
            gl.glGetUniformIndices(shaderProgramID,uniformNames,uniformIndices,0);
            int[] uniformOffset=new int[uniformNames.length];
            gl.glGetActiveUniformsiv(shaderProgramID,uniformNames.length,uniformIndices,
                    0,gl.GL_UNIFORM_OFFSET,uniformOffset,0);
            for(i=0;i<uniformNames.length;i++){
                varOffsetMap.put(uniformNames[i],uniformOffset[i]);
            }
            initFinished=true;
        }
    }

    private void updateData(String varName,Buffer data, int size){
        checkBinding();
        gl.glBufferSubData(gl.GL_UNIFORM_BUFFER,varOffsetMap.get(varName),size,data);
    }
    public void updateData(String varName, byte[] data){
        updateData(varName,tempBuffer.update(data),data.length);
    }
    public void updateData(String varName, int[] data){
        updateData(varName,tempBuffer.update(data),data.length*DynamicBuffer.INT_SIZE);
    }
    public void updateData(String varName, float[] data){
        updateData(varName,tempBuffer.update(data),data.length*DynamicBuffer.FLOAT_SIZE);
    }

    public void cleanTempBuffer(){
        tempBuffer.clean();
        tempBuffer=new DynamicBuffer();
    }

}
