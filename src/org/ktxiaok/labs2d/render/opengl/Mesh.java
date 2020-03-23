package org.ktxiaok.labs2d.render.opengl;

import org.joml.Vector2f;
import org.ktxiaok.labs2d.system.util.MathUtil;
import org.ktxiaok.labs2d.system.util.Rect;

import java.util.HashMap;
import java.util.Map;

public class Mesh {
    /**
     * The array of positions of the vertexes
     */
    public float[] vertexPos;
    /**
     * Texture mapping coordinates
     */
    public float[] texcoord;
    /**
     * The data of the EBO(Elements Buffer Object)
     */
    public int[] vertexIndex;

    private Rect boundingRect=null;
    private float boundingRadius=-1;

    private VertexBufferObject posVBO=null;
    private VertexBufferObject texcoordVBO=null;
    private VertexBufferObject indexVBO=null;

    //private Map<TextureAtlas,VertexBufferObject[]> textureAtlasMap0 =null;
    private Map<TextureAtlas,float[][]> textureAtlasMap=null;
    public Mesh() {
        vertexPos=new float[0];
        texcoord=new float[0];
        vertexIndex=new int[0];
    }
    public Mesh(float[] vertexPos, float[] texcoord, int[] vertexIndex) {
        this.vertexPos=vertexPos;
        this.texcoord=texcoord;
        this.vertexIndex=vertexIndex;
    }

//    public void uploadAtlasTexcoord(final VBOHandler texcoordVBO, final TextureAtlas textureAtlas, int index, Renderer renderer){
//        if(textureAtlasMap0 ==null){
//            textureAtlasMap0 =new HashMap<>(4);
//        }
//        VertexBufferObject[] vbos= textureAtlasMap0.get(textureAtlas);
//        if(vbos==null) {
//            final VertexBufferObject[][] result = {null};
//            Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
//                @Override
//                public void run() {
//                    VertexBufferObject[] vbos = new VertexBufferObject[textureAtlas.getSize()];
//                    int vboTarget = GLContext.GL().GL_COPY_READ_BUFFER;
//                    int vboUsage = GLContext.GL().GL_STATIC_DRAW;
//                    for (int i = 0; i < vbos.length; i++) {
//                        vbos[i] = new VertexBufferObject(vboTarget);
//                        VBOHandler vboHandler = new VBOHandler(vbos[i]);
//                        vboHandler.bind();
//                        float[] texcoordConverted = new float[texcoord.length];
//                        TextureRegion textureRegion = textureAtlas.getTextureRegion(i);
//                        textureRegion.convertTexcoord(texcoordConverted, texcoord);
//                        //Log.i("texcoordConverted", Arrays.toString(texcoordConverted));
//                        vboHandler.uploadData(texcoordConverted, vboUsage);
//                        vboHandler.cleanTempBuffer();
//                    }
//                    textureAtlasMap0.put(textureAtlas, vbos);
//                    result[0] = vbos;
//                }
//            }, true);
//            vbos = result[0];
//        }
//        final VertexBufferObject vbo=vbos[index];
//        Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
//            @Override
//            public void run() {
//                vbo.bind();
//                texcoordVBO.bind();
//                VertexBufferObject.copyBufferSubData(GLContext.GL().GL_COPY_READ_BUFFER,
//                        GLContext.GL().GL_ARRAY_BUFFER,0,0,
//                        texcoord.length*DynamicBuffer.FLOAT_SIZE);
//            }
//        }, false);
//    }
    public float[] getAtlasTexcoord(TextureAtlas textureAtlas,int index){
        if(textureAtlas==null){
            textureAtlasMap=new HashMap<>(4);
        }
        float[][] atlasTexcoord=textureAtlasMap.get(textureAtlas);
        if(atlasTexcoord==null){
            atlasTexcoord=new float[textureAtlas.getSize()][];
            textureAtlasMap.put(textureAtlas,atlasTexcoord);
        }
        if(atlasTexcoord[index]==null){
            atlasTexcoord[index]=new float[this.texcoord.length];
            TextureRegion textureRegion=textureAtlas.getTextureRegion(index);
            textureRegion.convertTexcoord(atlasTexcoord[index],0,
                    this.texcoord,0,this.texcoord.length);
        }
        return atlasTexcoord[index];
    }
    public Mesh deepCopy() {
        return new Mesh(vertexPos.clone(),texcoord.clone(),vertexIndex.clone());
    }
    public Mesh shallowCopy() {
        return new Mesh(vertexPos,texcoord,vertexIndex);
    }
    public static void createPolygonTexcoord(float[] vertexPos,int vertexPosOffset,int length,
                                             float[] texcoord,int texcoordOffset){
        float minX=vertexPos[0];
        float minY=vertexPos[1];
        float maxX=minX;
        float maxY=minY;
        for(int i=vertexPosOffset,len=vertexPosOffset+length;i<len;i+=2){
            float vx=vertexPos[i];
            float vy=vertexPos[i+1];
            if(vx>maxX){
                maxX=vx;
            }else if(vx<minX){
                minX=vx;
            }
            if(vy>maxY){
                maxY=vy;
            }else if(vy<minY){
                minY=vy;
            }
        }
        float xLen=maxX-minX;
        float yLen=maxY-minY;
        for(int i=vertexPosOffset,len=vertexPosOffset+length,j=0;i<len;i+=2,j+=2){
            float vx=vertexPos[i];
            float vy=vertexPos[i+1];
            texcoord[texcoordOffset+j]=(vx-minX)/xLen;
            texcoord[texcoordOffset+j+1]=(vy-minY)/yLen;
        }
    }
    public static void createPolygonVertexIndex(int vertexCount,float[] vertexIndex,int offset){
        for(int i=1;i<vertexCount-1;i++){
            int pos=offset+(i-1)*3;
            vertexIndex[pos]=0;
            vertexIndex[pos+1]=i;
            vertexIndex[pos+2]=i+1;
        }
    }
    public void createMeshVBO(){
        if(vertexPos!=null){
            VBOHandler vboHandler=VBOHandler.createArrayBuffer();
            vboHandler.bind();
            vboHandler.uploadData(vertexPos,GLContext.GL().GL_STATIC_DRAW);
            vboHandler.cleanTempBuffer();
            posVBO=vboHandler.getVBO();
        }
        if(texcoord!=null){
            VBOHandler vboHandler=VBOHandler.createArrayBuffer();
            vboHandler.bind();
            vboHandler.uploadData(texcoord,GLContext.GL().GL_STATIC_DRAW);
            vboHandler.cleanTempBuffer();
            texcoordVBO=vboHandler.getVBO();
        }
        if(vertexIndex!=null){
            VBOHandler vboHandler=VBOHandler.createElementArrayBuffer();
            vboHandler.bind();
            vboHandler.uploadData(vertexIndex,GLContext.GL().GL_STATIC_DRAW);
            vboHandler.cleanTempBuffer();
            indexVBO=vboHandler.getVBO();
        }
    }
    public void disposeTextureAtlas(){
//        if(textureAtlasMap0 !=null){
//            for(VertexBufferObject[] vbos: textureAtlasMap0.values()){
//                for(VertexBufferObject vbo:vbos){
//                    vbo.dispose();
//                }
//            }
//        }
        textureAtlasMap=null;
    }
    public void disposeMeshVBO(){
        if(posVBO!=null){
            posVBO.dispose();
        }
        if(texcoordVBO!=null){
            texcoordVBO.dispose();
        }
        if(indexVBO!=null){
            indexVBO.dispose();
        }
    }
    public void dispose(){
        disposeTextureAtlas();
        disposeMeshVBO();
    }
    public VertexBufferObject getVertexPosVBO(){
        return posVBO;
    }
    public VertexBufferObject getTexcoordVBO(){
        return texcoordVBO;
    }
    public VertexBufferObject getIndexVBO(){
        return indexVBO;
    }

    private void transform(TransformStruct transform) {
        Vector2f v0=new Vector2f();
        for(int i=0;i<vertexPos.length;i+=2){
            v0.x=vertexPos[i];
            v0.y=vertexPos[i+1];
            transform.transform(v0);
            vertexPos[i]=v0.x;
            vertexPos[i+1]=v0.y;
        }
    }
    private void combine(Mesh mesh) {
        int oldIndexLen=vertexIndex.length;
        int oldVertexLen=vertexPos.length/2;
        vertexPos=org.ktxiaok.labs2d.system.util.Utils.combineArray(vertexPos,mesh.vertexPos);
        texcoord=org.ktxiaok.labs2d.system.util.Utils.combineArray(texcoord,mesh.texcoord);
        vertexIndex=org.ktxiaok.labs2d.system.util.Utils.combineArray(vertexIndex,mesh.vertexIndex);
        for(int i=oldIndexLen;i<vertexIndex.length;i++) {
            vertexIndex[i]+=oldVertexLen;
        }
    }
    public void updateBoundingRadius(){
        float maxLen=-1f;
        for(int i=0;i<vertexPos.length;i+=2){
            float x=vertexPos[i];
            float y=vertexPos[i+1];
            float len0=x*x+y*y;
            if(len0>maxLen){
                maxLen=len0;
            }
        }
        boundingRadius= MathUtil.sqrt(maxLen);
    }
    public float getBoundingRadius(){
        return boundingRadius;
    }
    public Rect getBoundingRect(TransformStruct transform){
        if(boundingRect==null){
            boundingRect=new Rect();
            updateBoundingRadius();
        }
        float rx=boundingRadius*MathUtil.abs(transform.getScaleX());
        float ry=boundingRadius*MathUtil.abs(transform.getScaleY());
        boundingRect.x=transform.getPositionX()-rx;
        boundingRect.y=transform.getPositionY()-ry;
        boundingRect.w=rx*2;
        boundingRect.h=ry*2;
        return boundingRect;
    }
}
