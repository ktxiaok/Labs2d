package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.render.AbstractRenderSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GLRenderSystem extends AbstractRenderSystem {

    public GLRenderSystem(Renderer renderer) {
        renderObjectAddQueue=new LinkedList<>();
        cameraAddQueue=new LinkedList<>();
        cameraRemoveQueue=new LinkedList<>();
        cameraComponentList=new ArrayList<>();
        this.renderer=renderer;
        director=new Director(renderer);
        renderer.addDirector(director);
    }
    private Queue<RenderObject> renderObjectAddQueue;
    private Queue<CameraComponent> cameraAddQueue;
    private Queue<CameraComponent> cameraRemoveQueue;

    private List<CameraComponent> cameraComponentList;

    private Renderer renderer;
    private Director director;
    @Override
    public boolean render() {
        if(!renderer.isRendering()){
            while(!renderObjectAddQueue.isEmpty()){
                director.registerRenderObject(renderObjectAddQueue.poll());
            }
            while(!cameraAddQueue.isEmpty()){
                CameraComponent cameraComponent0=cameraAddQueue.poll();
                director.registerCamera(cameraComponent0.getCamera());
                cameraComponentList.add(cameraComponent0);
            }
            while(!cameraRemoveQueue.isEmpty()){
                CameraComponent cameraComponent0=cameraRemoveQueue.poll();
                director.removeCamera(cameraComponent0.getCamera());
                cameraComponentList.remove(cameraComponent0);
            }

            for(CameraComponent cameraComponent0:cameraComponentList){
                cameraComponent0.update();
            }
            renderer.requestRender();
            return true;
        }
        return false;
    }
    public Director getDirector(){
        return director;
    }
    public Renderer getRenderer(){return renderer;}
    protected void addRenderObject(RenderObject renderObject){
        renderObjectAddQueue.offer(renderObject);
    }
    protected void registerCameraComponent(CameraComponent cameraComponent){
        cameraAddQueue.offer(cameraComponent);
    }
    protected void removeCameraComponent(CameraComponent cameraComponent){
        cameraRemoveQueue.offer(cameraComponent);
    }

}
