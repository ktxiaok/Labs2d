package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.util.Rect;

public abstract class RenderObject {
    private ShaderProgram shaderProgram=null;
    private TextureUnits textureUnits=null;
    private int layer=0;
    private boolean active=true;
    private boolean visible=true;
    private boolean cameraVisible=true;
    private boolean ignoreCamera=false;
    private boolean delete=false;
    private boolean batchUpdate=false;

    private RenderComponent renderComponent=null;


    public RenderObject(){

    }
    public RenderObject(ShaderProgram shaderProgram, TextureUnits textureUnits) {
        this.shaderProgram = shaderProgram;
        this.textureUnits = textureUnits;
    }

    public RenderComponent getRenderComponent() {
        return renderComponent;
    }

    protected void setRenderComponent(RenderComponent renderComponent) {
        this.renderComponent = renderComponent;
    }

    public abstract void render();
    //Getter and Setter
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    protected void setShaderProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        batchUpdate=true;
    }

    public TextureUnits getTextureUnits() {
        return textureUnits;
    }

    protected void setTextureUnits(TextureUnits textureUnits) {
        this.textureUnits = textureUnits;
        batchUpdate=true;
    }


    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        batchUpdate=true;
    }

    public boolean isIgnoreCamera() {
        return ignoreCamera;
    }

    public void setIgnoreCamera(boolean ignoreCamera) {
        this.ignoreCamera = ignoreCamera;
    }

    public boolean isCameraVisible() {
        return cameraVisible;
    }

    protected void setCameraVisible(boolean cameraVisible) {
        this.cameraVisible = cameraVisible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean isVisible() {
        return visible;
    }

    public boolean isDeleted() {
        if(delete){
            delete=false;
            return true;
        }
        return false;
    }

    public void delete() {
        delete=true;
    }

    protected boolean isBatchUpdate(){
        return batchUpdate;
    }

    protected void setBatchUpdate(boolean batchUpdate) {
        this.batchUpdate = batchUpdate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract Rect getBoundingRect();
    protected abstract void init();

    public abstract void dispose();
}
