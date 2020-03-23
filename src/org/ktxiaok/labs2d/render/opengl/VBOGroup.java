package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.util.Utils;

public class VBOGroup {
    private VertexBufferObject[] vbos;
    private Integer hash=null;
    private VBOGroup(VertexBufferObject[] vbos){
        this.vbos=vbos;
    }
    public static class VBOGroupBuilder{
        private VertexBufferObject[] vbos;
        private VBOGroupBuilder(int vboCount){
            vbos=new VertexBufferObject[vboCount];
        }
        public VBOGroupBuilder putVBO(int index,VertexBufferObject vbo){
            vbos[index]=vbo;
            return this;
        }
        public VBOGroupBuilder putIBO(VertexBufferObject ibo){
            vbos[vbos.length-1]=ibo;
            return this;
        }
        public VBOGroup create(){
            return new VBOGroup(vbos);
        }
    }
    public static VBOGroupBuilder builder(int vboCount){
        return new VBOGroupBuilder(vboCount);
    }
    public VertexBufferObject getVBO(int index){
        return vbos[index];
    }
    public VertexBufferObject getIBO(){
        return vbos[vbos.length-1];
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VBOGroup){
            VBOGroup key1=(VBOGroup)obj;
            if(vbos.length!=key1.vbos.length)return false;
            for(int i=0;i<vbos.length;i++){
                if(!Utils.isEqual(vbos[i],key1.vbos[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        if(hash==null){
            hash=new Integer(Utils.computeHashCode(vbos));
        }
        return hash.intValue();
    }
}
