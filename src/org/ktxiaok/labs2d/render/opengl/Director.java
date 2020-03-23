package org.ktxiaok.labs2d.render.opengl;


import org.joml.Matrix3f;
import org.ktxiaok.labs2d.system.util.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TODO 把GLES30转为GL
 */
public class Director{
    public Director(Renderer renderer){
        batchProcessor=new BatchProcessor();
        renderObjectList=new ArrayList<>();
        cameraList=new ArrayList<>();
        gl=GLContext.GL();
        this.renderer=renderer;
        defaultShaderProgram=renderer.getDefaultShaderProgram();
    }
    private BatchProcessor batchProcessor;
    private List<RenderObject> renderObjectList;
    private List<Camera> cameraList;

    private Camera currentCamera=null;
    private Matrix3f cameraTransformMatrix=null;

    private GL gl;

    private Renderer renderer=null;
    private DefaultShaderProgram defaultShaderProgram=null;


    public Renderer getRenderer(){
        return renderer;
    }
    public void registerRenderObject(RenderObject renderObject){
        renderObjectList.add(renderObject);
        batchProcessor.addRenderObject(renderObject);
    }
    public void registerCamera(Camera camera){
        //Camera Layer Sorting
        int len=cameraList.size();
        int layer=camera.getLayer();
        if(len==0) {
            cameraList.add(camera);
            return;
        }
        for(int i=0;i<len;i++) {
            int layer0=cameraList.get(i).getLayer();
            if(layer>=layer0) {
                cameraList.add(i,camera);
                return;
            }
        }
        cameraList.add(camera);
    }
    public void removeCamera(Camera camera){
        cameraList.remove(camera);
    }

    public void clean(){
        batchProcessor.clean();
        renderObjectList=new ArrayList<>();
        cameraList=new ArrayList<>();
    }


    public Camera getCurrentCamera(){
        return currentCamera;
    }
    public Matrix3f getCurrentCameraTransformMatrix(){
        return cameraTransformMatrix;
    }

    public void render(){
        gl.glClear(gl.GL_COLOR_BUFFER_BIT);
        for(int i=cameraList.size()-1;i>=0;i--){
            Camera camera0=cameraList.get(i);
            currentCamera=camera0;
            cameraTransformMatrix=camera0.getViewProjectionMatrix();
            defaultShaderProgram.updateViewProjectionMatrix(cameraTransformMatrix);
            Rect cameraRect=camera0.getViewportRect();
            int scissorX=(int)(camera0.getScreenOffsetX()*renderer.getWindowWidth());
            int scissorY=(int)(camera0.getScreenOffsetY()*renderer.getWindowHeight());
            int scissorW=(int)(camera0.getScreenWidth()*renderer.getWindowWidth());
            int scissorH=(int)(camera0.getScreenHeight()*renderer.getWindowHeight());
            //Log.i("glScissor","x:"+scissorX+" y:"+scissorY+" w:"+scissorW+" h:"+scissorH);
            gl.glScissor(scissorX,scissorY,scissorW,scissorH);
            Iterator<RenderObject> itr=renderObjectList.iterator();
            while(itr.hasNext()){
                RenderObject renderObject0=itr.next();
                if(!renderObject0.isVisible())continue;
                if(renderObject0.isDeleted()){
                    itr.remove();
                    continue;
                }
                Rect boundingRect=renderObject0.getBoundingRect();
                if(boundingRect!=null){
                    renderObject0.setCameraVisible(cameraRect.isOverlap(boundingRect));
                }
            }
            batchProcessor.render();
        }
    }
}
