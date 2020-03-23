package org.ktxiaok.labs2d.render.opengl;


public class VertexArrayObject extends GLObject {

    public VertexArrayObject(){
        int[] id0=new int[1];
        gl.glGenVertexArrays(1,id0,0);
        setID(id0[0]);
    }
    @Override
    protected GLObject getBindingObject() {
        return getContext().getBindingVertexArrayObject();
    }

    @Override
    protected void setBindingObject(GLObject glObject) {
        getContext().setBindingVertexArrayObject((VertexArrayObject) glObject);
    }

    @Override
    protected void unbindObject(GLObject bindingObject) {
        gl.glBindVertexArray(0);
    }

    @Override
    protected void bindObject() {
        gl.glBindVertexArray(getID());
    }

    public void enableAttrib(int index){
        checkBinding();
        gl.glEnableVertexAttribArray(index);
    }
    public void disableAttrib(int index){
        checkBinding();
        gl.glDisableVertexAttribArray(index);
    }
    public void attribPointer(int index,int size,int type,boolean normalized,int stride,int offset){
        checkBinding();
        gl.glVertexAttribPointer(index,size,type,normalized,stride,offset);
    }
    public void attribDivisor(int index,int divisor){
        checkBinding();
        gl.glVertexAttribDivisor(index,divisor);
    }

    public void drawElements(int vertexCount,int offset){
        checkBinding();
        gl.glDrawElements(gl.GL_TRIANGLES,vertexCount,gl.GL_UNSIGNED_INT,offset);
    }
    public void drawElementsInstanced(int vertexCount,int offset,int instanceCount){
        checkBinding();
        gl.glDrawElementsInstanced(gl.GL_TRIANGLES,vertexCount,gl.GL_UNSIGNED_INT,offset,instanceCount);
    }
    public void drawElements(int vertexCount){
        checkBinding();
        drawElements(vertexCount,0);
    }
    public void drawElementsInstanced(int vertexCount,int instanceCount){
        checkBinding();
        drawElementsInstanced(vertexCount,0,instanceCount);
    }

    @Override
    public void dispose() {
        super.dispose();
        gl.glDeleteVertexArrays(1,new int[]{getID()},0);
    }
}
