package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.util.ModificationSafeList;
import org.ktxiaok.labs2d.system.util.TaskProcessor;
import org.ktxiaok.labs2d.system.util.Timer;

import java.util.ArrayList;

public abstract class Renderer{
    private TaskProcessor taskProcessor;
    public static final float DEFAULT_FPS=60f;
    private Timer fpsTimer=new Timer();
    private int delayTime=(int)(1000f/DEFAULT_FPS);

    private ModificationSafeList<Director> directorList;

    private float realFPS=0;

    private int windowWidth=0;
    private int windowHeight=0;

    private boolean renderFlag=false;

    private DefaultShaderProgram defaultShaderProgram;
    private SquareMesh squareMesh=null;

    public Renderer(){
        taskProcessor=new TaskProcessor();
        directorList=new ModificationSafeList<>(new ArrayList<Director>(),true);
    }
    public TaskProcessor getTaskProcessor(){
        return taskProcessor;
    }
    protected abstract void swapBuffers();
    protected void renderLoop(){
        fpsTimer.start();
        if(renderFlag){
            for(Director director:directorList.getList()){
                director.render();
            }
            swapBuffers();
            renderFlag=false;
        }
        directorList.handleRequest();
        taskProcessor.run();
        realFPS=1000f/fpsTimer.record().getMillisecond();
        try {
            while(fpsTimer.record().getMillisecond()<=delayTime){
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
        }
    }
    public float getRealFPS(){
        return realFPS;
    }
    public void addDirector(final Director director){
        directorList.requestAdd(director);
    }
    public void removeDirector(Director director){
        directorList.requestRemove(director);
    }
    public void setWindowSize(int width,int height){
        windowWidth=width;
        windowHeight=height;
        getTaskProcessor().post(new Runnable() {
            @Override
            public void run() {
                GLContext.GL().glViewport(0,0,windowWidth,windowHeight);
            }
        });
    }

    public void requestRender(){
        renderFlag=true;
    }
    public boolean isRendering(){
        return renderFlag;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void initBasicGLObjects(){
        getTaskProcessor().execute(new Runnable() {
            @Override
            public void run() {
                //cameraUBO=new CameraUBO();
                defaultShaderProgram=new DefaultShaderProgram();
                //cameraUBO.init(defaultShaderProgram);
                squareMesh=new SquareMesh();
            }
        });
    }
    public void deleteBasicGLObjects(){
        getTaskProcessor().post(new Runnable() {
            @Override
            public void run() {
                //cameraUBO.dispose();
                defaultShaderProgram.dispose();
                //cameraUBO=null;
                defaultShaderProgram=null;
                squareMesh.dispose();
                squareMesh=null;
            }
        });
    }
    public DefaultShaderProgram getDefaultShaderProgram() {
        return defaultShaderProgram;
    }
    public Mesh getSquareMesh(){
        return squareMesh;
    }
}
