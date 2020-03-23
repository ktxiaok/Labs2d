package org.ktxiaok.labs2d.render.opengl;

import java.nio.ByteBuffer;

public class Texture extends GLObject {
    public Texture(TextureAttrib attrib){
        int[] id0=new int[1];
        gl.glGenTextures(1,id0,0);
        setID(id0[0]);
        bind();
        gl.glPixelStorei(gl.GL_UNPACK_ALIGNMENT, 1);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, attrib.min_filter);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, attrib.max_filter);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, attrib.wrap_s);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, attrib.wrap_t);
    }
    public void loadImage(ByteBuffer pixels, int width, int height){
        gl.glTexImage2D(gl.GL_TEXTURE_2D,0,gl.GL_RGBA,width,height,0,gl.GL_RGBA,gl.GL_UNSIGNED_BYTE,pixels);
    }
    @Override
    protected GLObject getBindingObject() {
        return getContext().getBindingTexture();
    }

    @Override
    protected void setBindingObject(GLObject glObject) {
        getContext().setBindingTexture((Texture)glObject);
    }

    @Override
    protected void unbindObject(GLObject bindingObject) {
        gl.glBindTexture(gl.GL_TEXTURE_2D,0);
    }

    @Override
    protected void bindObject() {
        gl.glBindTexture(gl.GL_TEXTURE_2D,getID());
    }

    @Override
    public void dispose() {
        super.dispose();
        gl.glDeleteTextures(1,new int[]{getID()},0);
    }
}
