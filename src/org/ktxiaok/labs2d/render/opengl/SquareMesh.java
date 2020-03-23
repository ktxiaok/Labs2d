package org.ktxiaok.labs2d.render.opengl;

public class SquareMesh extends Mesh {
    public static final float[] VERTEX_POS=new float[]{0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f};
    public static final float[] TEXCOORD=new float[]{1f, 1f, 1f, 0f, 0f, 0f, 0f, 1f};
    public static final int[] VERTEX_INDEX=new int[]{0, 1, 2, 2, 3, 0};
    public SquareMesh() {
        super(VERTEX_POS,TEXCOORD,VERTEX_INDEX);
        createMeshVBO();
    }
    public static void squareOrder(float[] data,int offset,
                                   float centerX,float centerY,
                                   float halfW,float halfH){
        data[offset]=centerX+halfW;
        data[offset+1]=centerY+halfH;
        data[offset+2]=centerX+halfW;
        data[offset+3]=centerY-halfH;
        data[offset+4]=centerX-halfW;
        data[offset+5]=centerY-halfH;
        data[offset+6]=centerX-halfW;
        data[offset+7]=centerY+halfH;
    }
    public static void squareOrder(float[] data,
                                   float centerX,float centerY,
                                   float halfW,float halfH){
        squareOrder(data,0,centerX,centerY,halfW,halfH);
    }
}
