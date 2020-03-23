package org.ktxiaok.labs2d.render.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class BufferFactory {
    private static BufferFactory bufferFactory;
    public static BufferFactory getInstance(){
        if(bufferFactory==null){
            bufferFactory=new DirectBufferFactory();
        }
        return bufferFactory;
    }
    public static void configure(BufferFactory bufferFactory){
        BufferFactory.bufferFactory=bufferFactory;
    }
    public abstract ByteBuffer allocateByte(int length);
    public abstract IntBuffer allocateInt(int length);
    public abstract FloatBuffer allocateFloat(int length);
    public abstract void free(Buffer buffer);

}
class DirectBufferFactory extends BufferFactory {
    @Override
    public ByteBuffer allocateByte(int length) {
        ByteBuffer buffer=ByteBuffer.allocateDirect(length)
                .order(ByteOrder.nativeOrder());
        return buffer;
    }

    @Override
    public IntBuffer allocateInt(int length) {
        return allocateByte(length* DynamicBuffer.INT_SIZE).asIntBuffer();
    }

    @Override
    public FloatBuffer allocateFloat(int length) {
        return allocateByte(length*DynamicBuffer.FLOAT_SIZE).asFloatBuffer();
    }

    @Override
    public void free(Buffer buffer) {
//        if(!buffer.isDirect()){
//            return;
//        }
//        try{
//            Method cleanerMethod = buffer.getClass().getMethod("cleaner");
//            cleanerMethod.setAccessible(true);
//            Object cleaner = cleanerMethod.invoke(buffer);
//            Method cleanMethod = cleaner.getClass().getMethod("clean");
//            cleanMethod.setAccessible(true);
//            cleanMethod.invoke(cleaner);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }
}
