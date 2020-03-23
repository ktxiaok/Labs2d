package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.Component;
import org.ktxiaok.labs2d.system.TransformComponent;

public final class RenderComponent extends Component {
    private RenderObject renderObject=null;
    public RenderComponent(){

    }
    public RenderComponent(RenderObject renderObject){
        this.renderObject=renderObject;
        renderObject.setRenderComponent(this);
    }

    @Override
    public Class<? extends Component>[] getDependentComponentClasses() {
        return new Class[]{TransformComponent.class};
    }
    public TransformComponent transformComponent(){
        return getDependentComponent(0);
    }

    public GLRenderSystem renderSystem(){
        return (GLRenderSystem)getWorld().getRenderSystem();
    }
    public RenderObject getRenderObject(){
        return renderObject;
    }
    private Renderer _renderer=null;
    public Renderer getRenderer(){
        if(_renderer==null){
            _renderer=renderSystem().getRenderer();
        }
        return _renderer;
    }


    public void replaceRenderObject(RenderObject renderObject,boolean disposeOld){
        if(disposeOld){
            dispose();
        }
        this.renderObject=renderObject;
        init();
    }
    public void replaceRenderObject(RenderObject renderObject){
        replaceRenderObject(renderObject,true);
    }
    public void setRenderObject(RenderObject renderObject){
        this.renderObject=renderObject;
    }
    private void registerRenderObject(RenderObject renderObject){
        renderObject.init();
        renderSystem().addRenderObject(renderObject);
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        if(renderObject!=null){
            renderObject.delete();
            renderObject.dispose();
        }
    }
    private void init(){
        if(renderObject!=null){
            registerRenderObject(renderObject);
        }
    }
}
