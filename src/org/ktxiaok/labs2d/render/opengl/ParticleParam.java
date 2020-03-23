package org.ktxiaok.labs2d.render.opengl;

public class ParticleParam {
    private float[] data;
    private int dimension;
    public ParticleParam(int dimension){
        this.dimension=dimension;
    }
    protected void allocate(int capacity){
        data=new float[dimension*capacity];
    }
    protected void grow(int growCount){
        int growLength=dimension*growCount;
        float[] dataGrowed=new float[data.length+growLength];
        System.arraycopy(data,0,dataGrowed,0,growLength);
        data=dataGrowed;
    }
    private final int computeOffset(int index,int component){
        return dimension*index+component;
    }
    public float getData(int index,int component){
        return data[computeOffset(index,component)];
    }
    public void setData(int index,int component,float value){
        data[computeOffset(index,component)]=value;
    }
}
