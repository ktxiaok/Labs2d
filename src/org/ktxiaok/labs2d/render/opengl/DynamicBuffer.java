package org.ktxiaok.labs2d.render.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class DynamicBuffer {
    private Buffer buffer=null;
    private int type=0;
    public static final int TYPE_BYTE=0;
    public static final int TYPE_INT=1;
    public static final int TYPE_FLOAT=2;
    private float increaseFactor= DEFAULT_INCREASE_FACTOR;
    public static final int INT_SIZE=4;
    public static final int FLOAT_SIZE=4;
    public static final float DEFAULT_INCREASE_FACTOR =0.5f;
    public DynamicBuffer(){

    }
    public DynamicBuffer(int length,int type){
        buffer=allocate(length,type);
    }

    public float getIncreaseFactor() {
        return increaseFactor;
    }

    public void setIncreaseFactor(float increaseFactor) {
        this.increaseFactor = increaseFactor;
    }

    private Buffer allocate(int length,int type){
        Buffer buffer=null;
        switch(type){
            case TYPE_BYTE:
                buffer=BufferFactory.getInstance().allocateByte(length);
                break;
            case TYPE_INT:
                buffer=BufferFactory.getInstance().allocateInt(length);
                break;
            case TYPE_FLOAT:
                buffer=BufferFactory.getInstance().allocateFloat(length);
                break;
        }
        return buffer;
    }
    private void prepare(int length,int type){
        if(buffer==null||this.type!=type){
            buffer=allocate(length,type);
            this.type=type;
        }else{
            int oldLen=buffer.capacity();
            if(oldLen>=length){
                buffer.clear();
            }else{
                BufferFactory.getInstance().free(buffer);
                int distance=length-oldLen;
                int expectedIncrement=(int)(oldLen*increaseFactor);
                if(expectedIncrement>=distance){
                    buffer=allocate(oldLen+expectedIncrement,type);
                }else{
                    buffer=allocate(length,type);
                }
            }
        }
    }
    public ByteBuffer update(byte[] data,int offset,int length){
        prepare(length,TYPE_BYTE);
        ByteBuffer byteBuffer=(ByteBuffer)buffer;
        byteBuffer.put(data,offset,length);
        buffer.flip();
        return byteBuffer;
    }
    public IntBuffer update(int[] data, int offset, int length){
        prepare(length,TYPE_INT);
        IntBuffer intBuffer=(IntBuffer)buffer;
        intBuffer.put(data,offset,length);
        buffer.flip();
        return intBuffer;
    }
    public FloatBuffer update(float[] data, int offset, int length){
        prepare(length,TYPE_FLOAT);
        FloatBuffer floatBuffer=(FloatBuffer)buffer;
        floatBuffer.put(data,offset,length);
        buffer.flip();
        return floatBuffer;
    }
    public ByteBuffer update(byte[] data){
        return update(data,0,data.length);
    }
    public IntBuffer update(int[] data){
        return update(data,0,data.length);
    }
    public FloatBuffer update(float[] data){
        return update(data,0,data.length);
    }
    public void clean(){
        if(buffer==null)return;
        BufferFactory.getInstance().free(buffer);
        buffer=null;
    }
}
