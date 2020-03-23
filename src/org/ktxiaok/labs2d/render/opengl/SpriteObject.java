package org.ktxiaok.labs2d.render.opengl;

import org.joml.Vector2f;
import org.ktxiaok.labs2d.system.TransformComponent;
import org.ktxiaok.labs2d.system.util.Rect;

public class SpriteObject extends RenderObject {
    private TransformStruct transform;
    private TransformStruct localTransform;
    private Mesh mesh=null;
    private VBOGroup vboGroup=null;
    private VertexArrayObject vao=null;

    private VBOHandler vertexPosVBOHandler=null;
    private VBOHandler vertexIndexVBOHandler=null;
    private VBOHandler texcoordVBOHandler =null;
    private boolean vertexPosModFlag=false;
    private boolean texcoordModFlag=false;
    private boolean vertexIndexModFlag=false;
    private int vertexCount=0;
    private TextureAtlas textureAtlas=null;
    private int textureAtlasIndex=0;
    public SpriteObject(Texture texture,TextureAtlas textureAtlas,int textureAtlasIndex,Mesh mesh) {
        setTextureUnits(TextureUnits.create(new Texture[]{texture}));
        transform=new TransformStruct();
        localTransform=new TransformStruct();
        this.textureAtlas=textureAtlas;
        this.textureAtlasIndex=textureAtlasIndex;
        this.mesh=mesh;
    }
    public SpriteObject(Texture texture,TextureAtlas textureAtlas,int textureAtlasIndex){
        this(texture,textureAtlas,textureAtlasIndex,null);
    }
    public SpriteObject(Texture texture){
        this(texture,null,0,null);
    }
    public Renderer getRenderer(){
        return getRenderComponent().getRenderer();
    }

    public void setTextureAtlas(TextureAtlas textureAtlas){
        this.textureAtlas=textureAtlas;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    @Override
    protected void init() {
        setShaderProgram(getRenderer().getDefaultShaderProgram());
        if(mesh==null){
            this.mesh=getRenderer().getSquareMesh();
        }
        loadMesh(mesh);
    }

    @Override
    public void setTextureUnits(TextureUnits textureUnits) {
        super.setTextureUnits(textureUnits);
    }

    @Override
    public void dispose() {
        if(vboGroup!=null){
            getShaderProgram().discardVAO(vboGroup,this,getRenderer());
        }
        Utils.runOnRenderThreadOrLocal(getRenderer(),
                new Runnable() {
                    @Override
                    public void run() {
                        if(vertexPosModFlag){
                            vertexPosVBOHandler.dispose();
                        }
                        if(texcoordModFlag){
                            texcoordVBOHandler.dispose();
                        }
                        if(vertexIndexModFlag){
                            vertexIndexVBOHandler.dispose();
                        }
                    }
                },false);
    }


    public void updateVertexPos(final float[] vertexPos,final int usage,final boolean runOnRenderThread){
        Renderer renderer=runOnRenderThread?getRenderer():null;
        if(!vertexPosModFlag){
            Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
                @Override
                public void run() {
                    vertexPosVBOHandler=new VBOHandler(VertexBufferObject.createArrayBuffer());
                    updateVBOGroup(false);
                    vertexPosModFlag=true;
                }
            },true);
        }
        final VBOHandler vertexPosVBOHandlerFinal=vertexPosVBOHandler;
        Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
            @Override
            public void run() {
                vertexPosVBOHandlerFinal.bind();
                vertexPosVBOHandlerFinal.uploadData(vertexPos,usage);
            }
        },false);
    }
    public void updateTexcoord(final float[] texcoord,final int usage,final boolean runOnRenderThread){
        Renderer renderer=runOnRenderThread?getRenderer():null;
        if(!texcoordModFlag){
            Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
                @Override
                public void run() {
                    texcoordVBOHandler=new VBOHandler(VertexBufferObject.createArrayBuffer());
                    updateVBOGroup(false);
                    texcoordModFlag=true;
                }
            },true);
        }
        final VBOHandler texcoordVBOHandlerFinal=texcoordVBOHandler;
        Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
            @Override
            public void run() {
                texcoordVBOHandlerFinal.bind();
                texcoordVBOHandlerFinal.uploadData(texcoord,usage);
            }
        },false);
    }
    public void updateVertexIndex(final int[] vertexIndex,final int usage,final boolean runOnRenderThread){
        Renderer renderer=runOnRenderThread?getRenderer():null;
        vertexCount=vertexIndex.length;
        if(!vertexIndexModFlag){
            Utils.runOnRenderThreadOrLocal(runOnRenderThread?getRenderer():null, new Runnable() {
                @Override
                public void run() {
                    vertexIndexVBOHandler=new VBOHandler(VertexBufferObject.createElementArrayBuffer());
                    updateVBOGroup(false);
                    vertexIndexModFlag=true;
                }
            },true);
        }
        final VBOHandler vertexIndexVBOHandlerFinal=vertexIndexVBOHandler;
        Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
            @Override
            public void run() {
                vertexIndexVBOHandlerFinal.bind();
                vertexIndexVBOHandlerFinal.uploadData(vertexIndex,usage);
            }
        },false);
    }
    public void updateVertexPos(final float[] vertexPos,final int usage){
        updateVertexPos(vertexPos,usage,true);
    }
    public void updateTexcoord(final float[] texcoord,final int usage){
        updateTexcoord(texcoord,usage,true);
    }
    public void updateVertexIndex(final int[] vertexIndex,final int usage){
        updateVertexIndex(vertexIndex,usage,true);
    }

    public Mesh getMesh() {
        return mesh;
    }
    public void loadMesh(Mesh mesh) {
        this.mesh=mesh;
        resetVBOHandlers();
        vertexPosVBOHandler=new VBOHandler(mesh.getVertexPosVBO());
        applyTexcoord(mesh);
        vertexIndexVBOHandler=new VBOHandler(mesh.getIndexVBO());
        vertexCount=mesh.vertexIndex.length;
        updateVBOGroup(true);
    }
    private void applyTexcoord(Mesh mesh){
        if(textureAtlas==null){
            texcoordVBOHandler =new VBOHandler(mesh.getTexcoordVBO());
        }else{
            setTextureAtlasIndex(textureAtlasIndex);
        }
    }

    public void setTextureAtlasIndex(int index){
        this.textureAtlasIndex=index;

//        updateTexcoord(mesh.getAtlasTexcoord(textureAtlas,index),
//                dynamic?GLContext.GL().GL_DYNAMIC_DRAW:GLContext.GL().GL_STATIC_DRAW,
//                runOnRenderThread);

//        final int texcoordBufferSize=getMesh().texcoord.length*DynamicBuffer.FLOAT_SIZE;
//        if(!texcoordModFlag){
//            getRenderer().getTaskProcessor().execute(new Runnable() {
//                @Override
//                public void run() {
//                    texcoordVBOHandler=VBOHandler.createArrayBuffer();
//                    texcoordVBOHandler.bind();
//                    texcoordVBOHandler.getVBO().bufferData(null,texcoordBufferSize,
//                            GLContext.GL().GL_DYNAMIC_DRAW);
//                }
//            });
//            texcoordModFlag=true;
//        }
//        final VertexBufferObject texcoordConvertedVBO=getMesh().
//                getOrCreateSubTexcoordVBO(textureAtlas,index,getRenderer());
//        getRenderer().getTaskProcessor().post(new Runnable() {
//            @Override
//            public void run() {
//                texcoordConvertedVBO.bind();
//                texcoordVBOHandler.bind();
//                VertexBufferObject.copyBufferSubData(GLContext.GL().GL_COPY_READ_BUFFER,
//                        GLContext.GL().GL_ARRAY_BUFFER,0,0,
//                        texcoordBufferSize);
//            }
//        });
//        getMesh().uploadAtlasTexcoord(texcoordVBOHandler,textureAtlas,index,getRenderer());

    }

    public int getTextureAtlasIndex() {
        return textureAtlasIndex;
    }

    //不必在渲染线程调用此方法，也允许在渲染线程调用此方法
    private void resetVBOHandlers(){
        final VBOHandler vertexPosVBOHandlerFinal=this.vertexPosVBOHandler;
        final VBOHandler texcoordVBOHandlerFinal=this.texcoordVBOHandler;
        final VBOHandler vertexIndexVBOHandlerFinal=this.vertexIndexVBOHandler;
        final boolean vertexPosModFlagFinal=this.vertexPosModFlag;
        final boolean texcoordModFlagFinal=this.texcoordModFlag;
        final boolean vertexIndexModFlagFinal=this.vertexIndexModFlag;
        Utils.runOnRenderThreadOrLocal(getRenderer(), new Runnable() {
            @Override
            public void run() {
                if(vertexPosModFlagFinal){
                    vertexPosVBOHandlerFinal.dispose();
                }
                if(texcoordModFlagFinal){
                    texcoordVBOHandlerFinal.dispose();
                }
                if(vertexIndexModFlagFinal){
                    vertexIndexVBOHandlerFinal.dispose();
                }
            }
        },false);
        vertexPosModFlag=false;
        texcoordModFlag=false;
        vertexIndexModFlag=false;
    }

    private void updateVBOGroup(boolean runOnRenderThread){
        Renderer renderer=runOnRenderThread?getRenderer():null;
        if(vboGroup!=null){
            getShaderProgram().discardVAO(vboGroup,this,renderer);
        }
        vboGroup=VBOGroup.builder(3).putVBO(0,vertexPosVBOHandler.getVBO())
        .putVBO(1, texcoordVBOHandler.getVBO())
        .putIBO(vertexIndexVBOHandler.getVBO()).create();
        vao=getShaderProgram().getOrCreateVAO(vboGroup,this,renderer);
//        Utils.runOnRenderThreadOrLocal(renderer, new Runnable() {
//            @Override
//            public void run() {
//                vao.unbind();
//            }
//        },true);
    }

    public TransformStruct getLocalTransform() {
        return localTransform;
    }
    public void setWidth(float width){
        localTransform.setScaleX(width);
    }
    public void setHeight(float height){
        localTransform.setScaleY(height);
    }
    public float getWidth(){
        return localTransform.getScaleX();
    }
    public float getHeight(){
        return localTransform.getScaleY();
    }

    public TransformStruct getTransform() {
        //return Utils.updateTransform(transform,getRenderComponent().transformComponent());
        TransformComponent transformComponent=getRenderComponent().transformComponent();
        //float[] pos=transformComponent.getPosition();
        Vector2f pos=new Vector2f();
        transformComponent.getPosition(pos);
        //float[] scale=transformComponent.getScale();
        Vector2f scale=new Vector2f();
        transformComponent.getScale(scale);
        float angle=transformComponent.getAngle();
        transform.setPosition(pos.x,pos.y);
        transform.setScale(scale.x,scale.y);
        transform.setAngle(angle);
        transform.combine(localTransform);
        return transform;
    }


    @Override
    public void render() {
        DefaultShaderProgram defaultShaderProgram=(DefaultShaderProgram)getShaderProgram();
        defaultShaderProgram.setIgnoreCamera(isIgnoreCamera());
        defaultShaderProgram.updateModelMatrix(getTransform());
        if(textureAtlas==null){
            defaultShaderProgram.setTextureRegion(0,0,1,1,false);
        }else{
            defaultShaderProgram.setTextureRegion(textureAtlas.getTextureRegion(textureAtlasIndex));
        }
        vao.bind();
        vao.drawElements(vertexCount);
        vao.unbind();
    }

    @Override
    public Rect getBoundingRect() {
        return mesh.getBoundingRect(getTransform());
    }
}
