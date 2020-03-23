package org.ktxiaok.labs2d.render.opengl;


public class Shader {
    private int id=0;
    private GL gl=GLContext.GL();
    private Shader(){

    }
    public int getID(){
        return id;
    }
    public static Shader createShader(int type,String shaderCode) throws OpenGLException{
        Shader shader=new Shader();
        shader.compileShader(type,shaderCode);
        return shader;
    }
    public static Shader createVertexShader(String shaderCode) throws  OpenGLException{
        return createShader(GLContext.GL().GL_VERTEX_SHADER,shaderCode);
    }
    public static Shader createFragmentShader(String shaderCode) throws OpenGLException{
        return createShader(GLContext.GL().GL_FRAGMENT_SHADER,shaderCode);
    }
    private void compileShader(int type,String shaderCode) throws OpenGLException{
        id=gl.glCreateShader(type);
        if(id==0){
            throw new OpenGLException("Could not create GL Shader!");
        }
        gl.glShaderSource(id,shaderCode);
        gl.glCompileShader(id);
        final int[] compileStatus=new int[1];
        gl.glGetShaderiv(id,gl.GL_COMPILE_STATUS,compileStatus,0);
        if (compileStatus[0] == 0) {
            String errorInfo = gl.glGetShaderInfoLog(id);
            dispose();
            throw new OpenGLException("GL Shader Compile Error:"+errorInfo);
        }
    }
    public void dispose(){
        if(id!=0){
            gl.glDeleteShader(id);
        }
    }
}
