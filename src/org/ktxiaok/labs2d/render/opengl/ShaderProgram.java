package org.ktxiaok.labs2d.render.opengl;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShaderProgram extends GLObject {
    private Map<String,Integer> uniformMap;
    private List<VertexAttrib> vertexAttribList;
    private Map<VBOGroup,VertexArrayObject> vaoMap;
    private Map<VertexArrayObject,Set<Object>> vaoHandlerMap;
    public ShaderProgram(){
        setID(gl.glCreateProgram());
        uniformMap=new ArrayMap<>();
        vertexAttribList=new ArrayList<>();
        vaoMap=new HashMap<>();
        vaoHandlerMap=new HashMap<>();
    }
    @Override
    protected GLObject getBindingObject() {
        return getContext().getBindingShaderProgram();
    }

    @Override
    protected void setBindingObject(GLObject glObject) {
        getContext().setBindingShaderProgram((ShaderProgram)glObject);
    }
    @Override
    protected void unbindObject(GLObject bindingObject) {
        gl.glUseProgram(0);
    }
    @Override
    protected void bindObject() {
        gl.glUseProgram(getID());
    }
    public void attachShader(Shader shader){
        gl.glAttachShader(getID(),shader.getID());
    }
    public void linkProgram() throws OpenGLException{
        final int programID=getID();
        gl.glLinkProgram(programID);
        final int[] linkStatus=new int[1];
        gl.glGetProgramiv(programID, gl.GL_LINK_STATUS, linkStatus, 0);
        if(linkStatus[0]==0){
            String errorInfo=gl.glGetProgramInfoLog(programID);
            dispose();
            throw new OpenGLException("GL Program Link Error:"+errorInfo);
        }
    }
    private int tryGetUniform(String uniform){
        Integer location=uniformMap.get(uniform);
        if(location==null){
            int l=gl.glGetUniformLocation(getID(),uniform);
//            if(l<0){
//                return -1;
//            }
            location=new Integer(l);
            uniformMap.put(uniform,location);
        }
        return location.intValue();
    }
    public boolean containsUniform(String uniform){
        return (tryGetUniform(uniform)>=0);
    }
    private int findUniform(String uniform){
        int location=tryGetUniform(uniform);
        if(location<0){
            throw new OpenGLOperationException("Could not find shader uniform:" + uniform);
        }
        return location;
    }
    public void setUniformMatrix3f(String uniform,float[] matrix3f){
        setUniformMatrix3f(uniform,matrix3f,0,1);
    }
    //column major order
    public void setUniformMatrix3f(String uniform,float[] matrix3f,int offset,int count){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniformMatrix3fv(l,count,false,matrix3f,offset);
    }
    public void setUniform(String uniform,int i){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniform1i(l,i);
    }
    public void setUniform(String uniform,float f){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniform1f(l,f);
    }
    public void setUniform(String uniform,float f1,float f2){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniform2f(l,f1,f2);
    }
    public void setUniform(String uniform,float f1,float f2,float f3){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniform3f(l,f1,f2,f3);
    }
    public void setUniform(String uniform,float f1,float f2,float f3,float f4){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniform4f(l,f1,f2,f3,f4);
    }
    public void setUniform1v(String uniform,float f[],int count,int offset){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniform1fv(l,count,f,offset);
    }
    public void setUniform1v(String uniform,int i[],int count,int offset){
        checkBinding();
        int l=findUniform(uniform);
        gl.glUniform1iv(l,count,i,offset);
    }
    @Override
    public void dispose() {
        super.dispose();
        cleanVAOCache();
        gl.glDeleteProgram(getID());
    }

    public ShaderProgram putVertexAttrib(VertexAttrib vertexAttrib){
        vertexAttribList.add(vertexAttrib);
        return this;
    }

    public VertexArrayObject getOrCreateVAO(final VBOGroup vboGroup,final Object handler,final Renderer renderer){
        final VertexArrayObject[] result=new VertexArrayObject[1];
        Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
            @Override
            public void run() {
                VertexArrayObject vao=vaoMap.get(vboGroup);
                if(vao==null){
                    vao=new VertexArrayObject();
                    vao.bind();
                    for(VertexAttrib vertexAttrib0:vertexAttribList){
                        vboGroup.getVBO(vertexAttrib0.index).bind();
                        vao.attribPointer(vertexAttrib0.index,vertexAttrib0.size,vertexAttrib0.type,
                                vertexAttrib0.normalized,vertexAttrib0.stride,vertexAttrib0.offset);
                        if(vertexAttrib0.divisor!=0){
                            vao.attribDivisor(vertexAttrib0.index,vertexAttrib0.divisor);
                        }
                        vao.enableAttrib(vertexAttrib0.index);
                    }
                    VertexBufferObject ibo=vboGroup.getIBO();
                    if(ibo!=null){
                        ibo.bind();
                    }
                    vaoMap.put(vboGroup,vao);
                }

                Set<Object> handlers=vaoHandlerMap.get(vao);
                if(handlers==null){
                    handlers=new HashSet<>();
                    vaoHandlerMap.put(vao,handlers);
                }
                handlers.add(handler);
                vao.unbind();
                result[0]=vao;
            }
        },true);
        return result[0];
    }
    public void discardVAO(VBOGroup vboGroup,Object handler,final Renderer renderer){
        final VertexArrayObject vao=vaoMap.get(vboGroup);
        if(vao==null)return;
        Set<Object> handlers=vaoHandlerMap.get(vao);
        if(handlers==null)return;
        handlers.remove(handler);
        if(handlers.isEmpty()){
            vaoHandlerMap.remove(vao);
            vaoMap.remove(vboGroup);
            Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
                @Override
                public void run() {
                    vao.dispose();
                }
            },false);
        }
    }
    public void cleanVAOCache(){
        for(VertexArrayObject vao0:vaoMap.values()){
            vao0.dispose();
        }
    }
}
