package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class TextureUnits {
    private Texture[] textures;
    private GLContext glContext=null;
    private GL gl=null;
    private TextureUnits(Texture[] textures){
        this.textures=textures;
    }
    public Texture[] getTextures(){
        return textures;
    }
    public void activeAndBindTexture() {
        if(glContext==null){
            glContext=GLContext.getCurrentContext();
            gl=GLContext.GL();
        }
        if(equals(glContext.getBindingTextureUnits()))return;
        for(int i=0;i<textures.length;i++) {
            Texture t0=textures[i];
            gl.glActiveTexture(gl.GL_TEXTURE[i]);
            t0.bind();
        }
        glContext.setBindingTextureUnits(this);
    }
    public void dispose(){
        PoolKey poolKey=new PoolKey(textures);
        PoolValue poolValue=pool.get(poolKey);
        if(poolValue.counter==1){
            pool.remove(poolKey);
        }
    }
    public static TextureUnits create(Texture[] textures){
        PoolKey poolKey=new PoolKey(textures);
        PoolValue poolValue=pool.get(poolKey);
        if(poolValue==null){
            poolValue=new PoolValue();
            poolValue.textureUnits =new TextureUnits(textures);
            pool.put(poolKey,poolValue);
        }else{
            poolValue.counter++;
        }
        return poolValue.textureUnits;
    }
    private static Map<PoolKey,PoolValue> pool=new HashMap<PoolKey,PoolValue>();
    private static class PoolKey {
        private Texture[] textures;
        private Integer hashcode=null;
        public PoolKey(Texture[] textures){
            this.textures=textures;
        }
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PoolKey){
                PoolKey key=(PoolKey)obj;
                Texture[] textures1=key.textures;
                if(textures.length==textures1.length){
                    for(int i=0;i<textures.length;i++){
                        if(!textures[i].equals(textures1[i])){
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            if(hashcode==null){
                hashcode= Utils.computeHashCode(textures);
            }
            return hashcode;
        }
    }
    private static class PoolValue{
        public TextureUnits textureUnits;
        public int counter=1;
    }


}
