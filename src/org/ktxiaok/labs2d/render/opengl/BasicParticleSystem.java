package org.ktxiaok.labs2d.render.opengl;

import org.joml.Vector2f;
import org.ktxiaok.labs2d.system.util.Rect;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class BasicParticleSystem extends RenderObject{

    public static final float DEFAULT_ARRAY_GROW_FACTOR =0.25f;

    private ParticleParam pos=null;
    private ParticleParam scale=null;
    private ParticleParam angle=null;
    private ParticleParam color=null;
    private ParticleParam textureAtlasIndex=null;


    private int paramFlag=0;
    public static final int PARAM_SCALE_BIT=1;
    public static final int PARAM_ANGLE_BIT=1<<1;
    public static final int PARAM_COLOR_BIT=1<<2;


    private boolean vertexPosBufferUpdate=true;

    private float[] vertexPosBuffer;

    private float[] colorBuffer=null;
    private float[] transformTBuffer=null;
    private float[] transformSBuffer=null;
    private float[] transformABuffer=null;
    private float[] textureRegionBuffer=null;
    private float[] textureRotateBuffer=null;

    private VBOHandler vertexPosVBO;
    private VBOHandler colorVBO;
    private VBOHandler transformTVBO;
    private VBOHandler transformSVBO;
    private VBOHandler transformAVBO;
    private VBOHandler textureRegionVBO;
    private VBOHandler textureRotateVBO;

    private VBOGroup vboGroup;
    private VertexArrayObject vao;

    private Set<Integer> activeIndexSet=new HashSet<>();
    private Queue<Integer> availableIndexQueue=new LinkedList<>();

    private List<ParticleParam> particleParamList=new LinkedList<>();

    private TextureAtlas textureAtlas=null;

    private Renderer renderer;

    private static final int UNIT_VERTEX_COUNT=4;

    private float arrayGrowFactor = DEFAULT_ARRAY_GROW_FACTOR;

    private TransformStruct transform=new TransformStruct();

    private Rect boundingRect=null;

    private int capacity;

    public BasicParticleSystem(int initialCapacity, DefaultParticleShaderProgram shaderProgram){
        this.capacity=initialCapacity;
        setShaderProgram(shaderProgram);
        vertexPosBuffer=new float[UNIT_VERTEX_COUNT*2];
        setStandardMesh(1,1,0);
    }

    public void registerParticleParam(ParticleParam particleParam){
        particleParam.allocate(capacity);
        particleParamList.add(particleParam);
    }
    public void removeParticleParam(ParticleParam particleParam){
        particleParamList.remove(particleParam);
    }

    //Param Getter and Setter (Start)---------------
    public void enableParam(int param){
        paramFlag|=param;
    }
    public boolean isEnableScale(){
        return (paramFlag&PARAM_SCALE_BIT)==PARAM_SCALE_BIT;
    }
    public boolean isEnableAngle(){
        return (paramFlag&PARAM_ANGLE_BIT)==PARAM_ANGLE_BIT;
    }
    public boolean isEnableColor(){
        return (paramFlag&PARAM_COLOR_BIT)==PARAM_COLOR_BIT;
    }
    //Param Getter and Setter (End)---------------

    public Iterator<Integer> activeIndexItertor(){
        return activeIndexSet.iterator();
    }

    public int getActiveParticleCount(){
        return activeIndexSet.size();
    }

    public boolean hasAvailableParticle(){
        return !availableIndexQueue.isEmpty();
    }

    public int createParticle(){
        if(availableIndexQueue.isEmpty()){
            grow();
        }
        int index=availableIndexQueue.poll();
        activeIndexSet.add(index);
        return index;
    }
    public boolean destroyParticle(int index){
        if(!activeIndexSet.remove(index)){
            return false;
        }
        availableIndexQueue.offer(index);
        return true;
    }

    public float getArrayGrowFactor() {
        return arrayGrowFactor;
    }
    public void setArrayGrowFactor(float arrayGrowFactor) {
        this.arrayGrowFactor = arrayGrowFactor;
    }

    public int getCapacity(){
        return capacity;
    }
    private int computeGrowCount(){
        return (int)(getCapacity()*arrayGrowFactor)+1;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
    public void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public void setStandardMesh(float width, float height, float angle){
        vertexPosBufferUpdate=true;
        float[] squareVertexPos=SquareMesh.VERTEX_POS;
        for(int i=0;i<squareVertexPos.length;i+=2){
            Vector2f point=new Vector2f(squareVertexPos[i],squareVertexPos[i+1]);
            point.x*=width;
            point.y*=height;
            TransformUtil.rotate(point,angle);
            vertexPosBuffer[i]=point.x;
            vertexPosBuffer[i+1]=point.y;
        }
    }
    @Override
    protected void init(){
        this.renderer=getRenderComponent().getRenderer();

        for(int i=0;i<capacity;i++){
            availableIndexQueue.offer(i);
        }

        allocateVBOBuffer(capacity);
        pos=new ParticleParam(2);
        registerParticleParam(pos);
        if(isEnableScale()){
            scale=new ParticleParam(2);
            registerParticleParam(scale);
        }
        if(isEnableAngle()){
            angle=new ParticleParam(1);
            registerParticleParam(angle);
        }
        if(isEnableColor()){
            color=new ParticleParam(4);
            registerParticleParam(color);
        }
        if(textureAtlas!=null){
            textureAtlasIndex=new ParticleParam(1);
            registerParticleParam(textureAtlasIndex);
        }

        renderer.getTaskProcessor().execute(new Runnable() {
            @Override
            public void run() {
                vertexPosVBO=VBOHandler.createArrayBuffer();
                colorVBO=VBOHandler.createArrayBuffer();
                transformTVBO=VBOHandler.createArrayBuffer();
                transformSVBO=VBOHandler.createArrayBuffer();
                transformAVBO=VBOHandler.createArrayBuffer();
                textureRegionVBO=VBOHandler.createArrayBuffer();
                textureRotateVBO=VBOHandler.createArrayBuffer();
            }
        });
        VertexBufferObject texcoordVBO=renderer.getSquareMesh().getTexcoordVBO();
        VertexBufferObject vertexIndexVBO=renderer.getSquareMesh().getIndexVBO();
        vboGroup=VBOGroup.builder(9)
                .putVBO(0,vertexPosVBO.getVBO())
                .putVBO(1,texcoordVBO)
                .putVBO(2,colorVBO.getVBO())
                .putVBO(3,transformTVBO.getVBO())
                .putVBO(4,transformSVBO.getVBO())
                .putVBO(5,transformAVBO.getVBO())
                .putVBO(6,textureRegionVBO.getVBO())
                .putVBO(7,textureRotateVBO.getVBO())
                .putIBO(vertexIndexVBO).create();
        vao=getShaderProgram().getOrCreateVAO(vboGroup,this,renderer);
    }
    private void allocateVBOBuffer(int capacity){
        float[] defaultColor={0f,0f,0f,1.0f};
        colorBuffer=new float[4*capacity];
        transformTBuffer=new float[2*capacity];
        transformSBuffer=new float[2*capacity];
        transformABuffer=new float[capacity];
        textureRegionBuffer=new float[4*capacity];
        textureRotateBuffer=new float[capacity];
        for(int i=0;i<capacity;i++){
            int colorBufferPos=4*i;
            colorBuffer[colorBufferPos]=defaultColor[0];
            colorBuffer[colorBufferPos+1]=defaultColor[1];
            colorBuffer[colorBufferPos+2]=defaultColor[2];
            colorBuffer[colorBufferPos+3]=defaultColor[3];
            int transformTBufferPos=2*i;
            transformTBuffer[transformTBufferPos]=0;
            transformTBuffer[transformTBufferPos+1]=0;
            int transformSBufferPos=2*i;
            transformSBuffer[transformSBufferPos]=1;
            transformSBuffer[transformSBufferPos+1]=1;
            int transformABufferPos=i;
            transformABuffer[transformABufferPos]=0;
            int textureRegionBufferPos=4*i;
            textureRegionBuffer[textureRegionBufferPos]=0;
            textureRegionBuffer[textureRegionBufferPos+1]=0;
            textureRegionBuffer[textureRegionBufferPos+2]=1;
            textureRegionBuffer[textureRegionBufferPos+3]=1;
            int textureRotateBufferPos=i;
            textureRotateBuffer[textureRotateBufferPos]=0;
        }
    }
    //继承需要加写的函数 Start---------------

    protected void grow(){
        int currentCapacity= getCapacity();
        int growCount=computeGrowCount();
        int newCapacity=currentCapacity+growCount;
        for(int i=currentCapacity;i<newCapacity;i++){
            availableIndexQueue.offer(i);
        }

        allocateVBOBuffer(newCapacity);
        for(ParticleParam particleParam:particleParamList){
            particleParam.grow(growCount);
        }
        capacity=newCapacity;
    }
    //继承需要加写的函数 End---------------
    public TransformStruct getTransform() {
        return transform;
    }
    @Override
    public void dispose(){
        //TODO
        renderer.getTaskProcessor().post(new Runnable() {
            @Override
            public void run() {
                getShaderProgram().discardVAO(vboGroup,BasicParticleSystem.this,renderer);
                vertexPosVBO.dispose();
                colorVBO.dispose();
                transformTVBO.dispose();
                transformSVBO.dispose();
                transformAVBO.dispose();
                textureRegionVBO.dispose();
                textureRotateVBO.dispose();
            }
        });
    }

    @Override
    public void render() {
        int activeCount=activeIndexSet.size();
        if(activeCount==0)return;
        if(vertexPosBufferUpdate){
            vertexPosVBO.uploadData(vertexPosBuffer,GLContext.GL().GL_STATIC_DRAW);
            vertexPosBufferUpdate=false;
        }
        int count=0;
        for(int index:activeIndexSet){
            int count2=count*2;
            int count4=count*4;
            transformTBuffer[count2]=pos.getData(index,0);
            transformTBuffer[count2+1]=pos.getData(index,1);
            if(isEnableColor()){
                colorBuffer[count4]=color.getData(index,0);
                colorBuffer[count4+1]=color.getData(index,1);
                colorBuffer[count4+2]=color.getData(index,2);
                colorBuffer[count4+3]=color.getData(index,3);
            }
            if(isEnableScale()){
                transformSBuffer[count2]=scale.getData(index,0);
                transformSBuffer[count2+1]=scale.getData(index,1);
            }
            if(isEnableAngle()){
                transformABuffer[count]=angle.getData(index,0);
            }
            if(textureAtlas!=null){
                TextureRegion textureRegion=textureAtlas.getTextureRegion((int)textureAtlasIndex.getData(index,0));
                textureRegionBuffer[count4]=textureRegion.getOffsetX();
                textureRegionBuffer[count4+1]=textureRegion.getOffsetY();
                textureRegionBuffer[count4+2]=textureRegion.getTilingX();
                textureRegionBuffer[count4+3]=textureRegion.getTilingY();
                textureRotateBuffer[count]=textureRegion.isRotate()?1:0;
            }
            count++;
        }
        int staticUsage=GLContext.GL().GL_STATIC_DRAW;
        int streamUsage=GLContext.GL().GL_STREAM_DRAW;

        transformTVBO.uploadData(transformTBuffer,0,activeCount*2,streamUsage);
        if(isEnableColor()){
            colorVBO.uploadData(colorBuffer,0,activeCount*4,streamUsage);
        }else{
            if(colorBuffer!=null){
                colorVBO.uploadData(colorBuffer,staticUsage);
                colorBuffer=null;
            }
        }
        if(isEnableScale()){
            transformSVBO.uploadData(transformSBuffer,0,activeCount*2,streamUsage);
        }else{
            if(transformSBuffer!=null){
                transformSVBO.uploadData(transformSBuffer,staticUsage);
                transformSBuffer=null;
            }
        }
        if(isEnableAngle()){
            transformAVBO.uploadData(transformABuffer,0,activeCount,streamUsage);
        }else{
            if(transformABuffer!=null){
                transformAVBO.uploadData(transformABuffer,staticUsage);
                transformABuffer=null;
            }
        }
        if(textureAtlas==null){
            if(textureRegionBuffer!=null){
                textureRegionVBO.uploadData(textureRegionBuffer,staticUsage);
                textureRotateVBO.uploadData(textureRotateBuffer,staticUsage);
                textureRegionBuffer=null;
                textureRotateBuffer=null;
            }
        }else{
            textureRegionVBO.uploadData(textureRegionBuffer,0,activeCount*4,streamUsage);
            textureRotateVBO.uploadData(textureRotateBuffer,0,activeCount,streamUsage);
        }
        vao.bind();
        vao.drawElementsInstanced(6*activeCount,activeCount);
        vao.unbind();
    }

    public void setBoundingRect(Rect boundingRect) {
        this.boundingRect = boundingRect;
    }
    @Override
    public Rect getBoundingRect() {
        return boundingRect;
    }

    @Override
    public DefaultParticleShaderProgram getShaderProgram() {
        return (DefaultParticleShaderProgram)super.getShaderProgram();
    }
}
