package org.ktxiaok.labs2d.render.opengl;

public class VertexAttrib {
    public int index;
    public int size;
    public int type;
    public boolean normalized;
    public int stride;
    public int offset;
    public int divisor;

    public VertexAttrib(int index, int size, int type, boolean normalized, int stride, int offset,int divisor) {
        this.index = index;
        this.size = size;
        this.type = type;
        this.normalized = normalized;
        this.stride = stride;
        this.offset = offset;
        this.divisor=divisor;
    }

    public VertexAttrib(int index, int size, int type) {
        this(index,size,type,false,0,0,0);
    }
    public VertexAttrib(int index,int size,int type,int divisor){
        this(index,size,type,false,0,0,divisor);
    }
}
