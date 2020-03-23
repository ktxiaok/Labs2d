package org.ktxiaok.labs2d.render.opengl;

import java.nio.Buffer;

public class VertexBufferObject extends GLObject{
    private int target;

    private int bufferSize=0;
    private int bufferUsage=-1;
    public VertexBufferObject(int target){
        this.target =target;
        int[] id0=new int[1];
        gl.glGenBuffers(1,id0,0);
        setID(id0[0]);
    }
    public int getTarget(){
        return target;
    }
    public static VertexBufferObject createArrayBuffer(){
        return new VertexBufferObject(GLContext.GL().GL_ARRAY_BUFFER);
    }
    public static VertexBufferObject createElementArrayBuffer(){
        return new VertexBufferObject(GLContext.GL().GL_ELEMENT_ARRAY_BUFFER);
    }
    @Override
    protected GLObject getBindingObject() {
        return getContext().getBindingVertexBufferObject();
    }

    @Override
    protected void setBindingObject(GLObject glObject) {
        getContext().setBindingVertexBufferObject((VertexBufferObject)glObject);
    }

    @Override
    protected void unbindObject(GLObject bindingObject) {
        gl.glBindBuffer(target,0);
    }

    @Override
    protected void bindObject() {
//        final VertexArrayObject bindingVAO=getContext().getBindingVertexArrayObject();
//        if(bindingVAO==null){
//            throw new OpenGLOperationException("This VBO doesn't belong to any VAO. Please call VAO bind()"
//                    + "before call VBO bind()!");
//        }
//        if(vao==null){
//            vao=bindingVAO;
//        }else if(!vao.equals(bindingVAO)){
//            throw new OpenGLOperationException("This VBO doesn't belong to the binding VAO.");
//        }
        gl.glBindBuffer(target,getID());
    }

    @Override
    public void dispose() {
        super.dispose();
        gl.glDeleteBuffers(1,new int[]{getID()},0);
    }

    public void bufferData(Buffer data, int size, int usage){
        checkBinding();
        bufferSize=size;
        bufferUsage=usage;
        gl.glBufferData(target,size,data,usage);
    }
    public void bufferSubData(Buffer data,int size,int offset){
        checkBinding();
        gl.glBufferSubData(target,offset,size,data);
    }
    public void updateBufferData(Buffer data,int size,int usage){
        if(size>bufferSize||bufferUsage!=usage) {
            bufferData(data, size, usage);
        }else{
            bufferSubData(data,size,0);
        }
    }

    public static void copyBufferSubData(int readTarget, int writeTarget, int readOffset, int writeOffset, int size){
        GLContext.GL().glCopyBufferSubData(readTarget,writeTarget,readOffset,writeOffset,size);
    }

}
