package org.ktxiaok.labs2d.render.opengl;

public class VBOHandler {
    private VertexBufferObject vbo;
    private DynamicBuffer tempBuffer;
    public VBOHandler(VertexBufferObject vbo){
        this.vbo=vbo;
        tempBuffer=new DynamicBuffer();
    }
    public static VBOHandler createArrayBuffer(){
        VertexBufferObject vbo=VertexBufferObject.createArrayBuffer();
        return new VBOHandler(vbo);
    }
    public static VBOHandler createElementArrayBuffer(){
        VertexBufferObject vbo=VertexBufferObject.createElementArrayBuffer();
        return new VBOHandler(vbo);
    }
    public void cleanTempBuffer(){
        tempBuffer.clean();
    }
    public VertexBufferObject getVBO(){
        return vbo;
    }
    public void bind(){
        vbo.bind();
    }
    public void uploadData(byte[] data,int offset,int length,int usage){
        vbo.updateBufferData(tempBuffer.update(data,offset,length),data.length,usage);
    }
    public void uploadData(int[] data,int offset,int length,int usage){
        vbo.updateBufferData(tempBuffer.update(data,offset,length),data.length*DynamicBuffer.INT_SIZE,usage);
    }
    public void uploadData(float[] data,int offset,int length,int usage){
        vbo.updateBufferData(tempBuffer.update(data,offset,length),data.length*DynamicBuffer.FLOAT_SIZE,usage);
    }
    public void uploadData(byte[] data,int usage){
        uploadData(data,0,data.length,usage);
    }
    public void uploadData(int[] data,int usage){
        uploadData(data,0,data.length,usage);
    }
    public void uploadData(float[] data,int usage){
        uploadData(data,0,data.length,usage);
    }
    public void dispose(){
        cleanTempBuffer();
        vbo.dispose();
    }

}
