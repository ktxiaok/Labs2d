package org.ktxiaok.labs2d.render.opengl;

import android.util.ArrayMap;

import java.util.Map;

public class GLContext {
    private GLContext(Thread thread) {
        this.thread=thread;
    }
    public static GLContext getCurrentContext(){
        Thread thread=Thread.currentThread();
        GLContext glContext=contextMap.get(thread);
        if(glContext==null){
            glContext=new GLContext(thread);
            contextMap.put(thread,glContext);
            return glContext;
        }
        return glContext;
    }
    public static void init(GL gl){
        GLContext.gl=gl;
    }
    public static GL GL(){
        if(gl==null){
            throw new RuntimeException("The GL interface was not configured!");
        }
        return gl;
    }

    private static GL gl=null;
    private static Map<Thread,GLContext> contextMap =new ArrayMap<Thread,GLContext>();

    private Thread thread;

    public Thread getThread(){
        return thread;
    }
    public void reset(){
        bindingShaderProgram =null;
        bindingVertexArrayObject =null;
        bindingVertexBufferObject =null;
        bindingTexture=null;
        bindingTextureUnits=null;
        bindingUniformBlockObject=null;
    }
    private ShaderProgram bindingShaderProgram =null;
    private VertexArrayObject bindingVertexArrayObject =null;
    private VertexBufferObject bindingVertexBufferObject =null;
    private Texture bindingTexture=null;
    private TextureUnits bindingTextureUnits=null;
    private UniformBlockObject bindingUniformBlockObject=null;

    public ShaderProgram getBindingShaderProgram() {
        return bindingShaderProgram;
    }
    public void setBindingShaderProgram(ShaderProgram bindingShaderProgram) {
        this.bindingShaderProgram = bindingShaderProgram;
    }

    public VertexArrayObject getBindingVertexArrayObject() {
        return bindingVertexArrayObject;
    }

    public void setBindingVertexArrayObject(VertexArrayObject bindingVertexArrayObject) {
        this.bindingVertexArrayObject = bindingVertexArrayObject;
    }

    public VertexBufferObject getBindingVertexBufferObject() {
        return bindingVertexBufferObject;
    }

    public void setBindingVertexBufferObject(VertexBufferObject bindingVertexBufferObject) {
        this.bindingVertexBufferObject = bindingVertexBufferObject;
    }

    public Texture getBindingTexture() {
        return bindingTexture;
    }

    public void setBindingTexture(Texture bindingTexture) {
        this.bindingTexture = bindingTexture;
    }

    public TextureUnits getBindingTextureUnits() {
        return bindingTextureUnits;
    }

    public void setBindingTextureUnits(TextureUnits bindingTextureUnits) {
        this.bindingTextureUnits = bindingTextureUnits;
    }

    public UniformBlockObject getBindingUniformBlockObject() {
        return bindingUniformBlockObject;
    }

    public void setBindingUniformBlockObject(UniformBlockObject bindingUniformBlockObject) {
        this.bindingUniformBlockObject = bindingUniformBlockObject;
    }

    private int nextUBOBindingPoint=0;

    public int allocateUBOBindingPoint(){
        int next=nextUBOBindingPoint;
        nextUBOBindingPoint++;
        return next;
    }
}
