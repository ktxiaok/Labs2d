package org.ktxiaok.labs2d.render.opengl;

import java.util.Iterator;

public class BatchProcessor {
    private RenderBatcher batcher;
    public BatchProcessor(){
        batcher=new RenderBatcher();
    }
    public void addRenderObject(RenderObject renderObject){
        batcher.addObject(renderObject);
    }
    public void clean(){
        batcher=new RenderBatcher();
    }
    public void render(){
        RenderBatcher.RootBatch rootBatch=batcher.rootBatch();
        for(RenderBatcher.LayerBatch layerBatch:rootBatch){
            for(RenderBatcher.ShaderBatch shaderBatch:layerBatch){
                ShaderProgram shaderProgram=shaderBatch.getShaderProgram();
                for(RenderBatcher.ObjectBatch objectBatch:shaderBatch){
                    if(!objectBatch.isEmpty()){
                        TextureUnits textureUnits=objectBatch.getTextureUnits();
                        shaderProgram.bind();
                        textureUnits.activeAndBindTexture();
                        Iterator<RenderObject> itr=objectBatch.iterator();
                        while(itr.hasNext()){
                            RenderObject renderObject=itr.next();
                            if(renderObject.isDeleted()){
                                itr.remove();
                                continue;
                            }
                            if(renderObject.isActive()){
                                if(renderObject.isCameraVisible()){
//                                    if(shaderProgram instanceof DefaultShaderProgram){
//                                        DefaultShaderProgram defaultShaderProgram=(DefaultShaderProgram)shaderProgram;
//                                        defaultShaderProgram.setIgnoreCamera(renderObject.isIgnoreCamera());
//                                        defaultShaderProgram.updateModelMatrix(renderObject.getTransform());
//                                    }
                                    renderObject.render();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
