package org.ktxiaok.labs2d.render.opengl;

import org.joml.Vector2f;
import org.ktxiaok.labs2d.system.Component;
import org.ktxiaok.labs2d.system.TransformComponent;

public class CameraComponent extends Component{
    private Camera camera=null;
    private TransformComponent target=null;
    public CameraComponent(Camera camera){
        this.camera=camera;
    }
    public CameraComponent(){

    }
    public Camera getCamera(){
        return camera;
    }
    public GLRenderSystem renderSystem(){
        return (GLRenderSystem)(getWorld().getRenderSystem());
    }
    public TransformComponent getTarget() {
        return target;
    }

    public void setTarget(TransformComponent target) {
        this.target = target;
    }
    public void setTarget(float x,float y){
        camera.setTarget(x,y);
    }
    public void setTarget(){
        Component component=getEntity().findComponent(TransformComponent.class);
        if(component!=null){
            setTarget((TransformComponent)component);
        }
    }

    protected void update(){
        if(target!=null){

//            float[] targetPos=target.getPosition();
//            camera.setTarget(targetPos[0],targetPos[1]);
            Vector2f targetPos=new Vector2f();
            target.getPosition(targetPos);
            camera.setTarget(targetPos.x,targetPos.y);
            camera.locate();
        }
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        renderSystem().registerCameraComponent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        renderSystem().removeCameraComponent(this);
    }
}
