package org.ktxiaok.labs2d.render.opengl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RenderBatcher{
    private RootBatch rootBatch;
    public RenderBatcher() {
        rootBatch=new RootBatch();
    }
    public RootBatch rootBatch(){
        return rootBatch;
    }
//    private class ObjectBatchItr implements Iterator<ObjectBatch> {
//        private Iterator<LayerBatch> layerItr=null;
//        private Iterator<ShaderBatch> shaderItr=null;
//        private Iterator<ObjectBatch> batchItr=null;
//        public ObjectBatchItr() {
//            initLayerItr();
//        }
//        private void initLayerItr() {
//            layerItr=rootBatch.iterator();
//        }
//        private void updateShaderItr() {
//            shaderItr=layerItr.next().iterator();
//        }
//        private void updateBatchItr() {
//            batchItr=shaderItr.next().iterator();
//        }
//        @Override
//        public boolean hasNext() {
//            if(layerItr.hasNext()) {
//                return true;
//            }else if(shaderItr!=null&&shaderItr.hasNext()) {
//                return true;
//            }else if(batchItr!=null&&batchItr.hasNext()) {
//                return true;
//            }
//            return false;
//        }
//
//        @Override
//        public ObjectBatch next() {
//            if(batchItr==null||!batchItr.hasNext()) {
//                if(shaderItr==null||!shaderItr.hasNext()) {
//                    if(!layerItr.hasNext()) {
//                        return null;
//                    }
//                    updateShaderItr();
//                }
//                updateBatchItr();
//            }
//            return batchItr.next();
//        }
//
//    }
//    private class LayerItr implements Iterator<Integer>{
//        private Iterator<LayerBatch> itr;
//        public LayerItr() {
//            itr=rootBatch.iterator();
//        }
//        @Override
//        public boolean hasNext() {
//            return itr.hasNext();
//        }
//
//        @Override
//        public Integer next() {
//            return itr.next().getLayer();
//        }
//
//    }
//
//    @Override
//    public Iterator<ObjectBatch> iterator(){
//        return new ObjectBatchItr();
//    }
//    public Iterator<Integer> layerIterator(){
//        return new LayerItr();
//    }
//    public Set<ShaderProgram> getActivatedShaderPrograms(){
//        Set<ShaderProgram> shaders=new HashSet<ShaderProgram>();
//        for(LayerBatch layerBatch:rootBatch) {
//            shaderFor:for(ShaderBatch shaderBatch:layerBatch) {
//                for(ObjectBatch objectBatch:shaderBatch) {
//                    if(!objectBatch.isEmpty()) {
//                        shaders.add(shaderBatch.getShaderProgram());
//                        continue shaderFor;
//                    }
//                }
//            }
//        }
//        return shaders;
//    }
    public void addObject(RenderObject obj) {
        ShaderProgram shader=obj.getShaderProgram();
        TextureUnits texture=obj.getTextureUnits();
        rootBatch.getLayerBatch(obj.getLayer()).getShaderBatch(shader).getObjectBatch(texture).addObject(obj);
    }

    public static class RootBatch implements Iterable<LayerBatch>{
        private Map<Integer,LayerBatch> layerBatchMap;
        private List<LayerBatch> layerBatchSequence;
        public RootBatch() {
            layerBatchMap=new HashMap<Integer, LayerBatch>();
            layerBatchSequence=new ArrayList<LayerBatch>();
        }
        public LayerBatch getLayerBatch(int layer) {
            if(!layerBatchMap.containsKey(layer)) {
                LayerBatch newBatch=new LayerBatch(layer);
                layerBatchMap.put(layer,newBatch);
                //分配新图层到序列------
                if(layerBatchSequence.isEmpty()) {
                    layerBatchSequence.add(newBatch);
                }else {
                    int currentIndex=0;
                    for(LayerBatch batch0:layerBatchSequence) {
                        if(layer<batch0.getLayer()) {
                            break;
                        }
                        currentIndex++;
                    }
                    layerBatchSequence.add(currentIndex,newBatch);
                }
                //----------------------
                return newBatch;
            }
            return layerBatchMap.get(layer);
        }
        @Override
        public Iterator<LayerBatch> iterator(){
            return layerBatchSequence.iterator();
        }

    }
    public static class LayerBatch implements Iterable<ShaderBatch>{
        private Map<ShaderProgram,ShaderBatch> shaderBatchMap;
        private int layer;
        public LayerBatch(int layer) {
            shaderBatchMap=new HashMap<ShaderProgram, RenderBatcher.ShaderBatch>();
            this.layer =layer;
        }
        public ShaderBatch getShaderBatch(ShaderProgram shader) {
            if(!shaderBatchMap.containsKey(shader)) {
                ShaderBatch newBatch=new ShaderBatch(shader);
                shaderBatchMap.put(shader,newBatch);
                return newBatch;
            }
            return shaderBatchMap.get(shader);
        }
        public int getLayer() {
            return layer;
        }
        @Override
        public Iterator<ShaderBatch> iterator(){
            return shaderBatchMap.values().iterator();
        }
    }
    public static class ShaderBatch implements Iterable<ObjectBatch>{
        private Map<TextureUnits,ObjectBatch> texBatchMap;
        private ShaderProgram shader;
        public ShaderBatch(ShaderProgram shader) {
            this.shader=shader;
            texBatchMap=new HashMap<TextureUnits,RenderBatcher.ObjectBatch>();
        }
        public ObjectBatch getObjectBatch(TextureUnits textureUnits) {
            if(!texBatchMap.containsKey(textureUnits)) {
                ObjectBatch newBatch=new ObjectBatch();
                texBatchMap.put(textureUnits,newBatch);
                return newBatch;
            }
            return texBatchMap.get(textureUnits);
        }
        public ShaderProgram getShaderProgram() {
            return shader;
        }
        @Override
        public Iterator<ObjectBatch> iterator(){
            return texBatchMap.values().iterator();
        }
    }
    public static class ObjectBatch implements Iterable<RenderObject>{
        private List<RenderObject> objList;
        public ObjectBatch() {
            objList=new ArrayList<RenderObject>();
        }
        public void clear() {
            objList.clear();
        }
        public void addObject(RenderObject obj) {
            objList.add(obj);
        }
        public boolean isEmpty() {
            return objList.isEmpty();
        }
        public TextureUnits getTextureUnits(){
            if(isEmpty())return null;
            return objList.get(0).getTextureUnits();
        }
        @Override
        public Iterator<RenderObject> iterator() {
            return objList.iterator();
        }

    }
}

