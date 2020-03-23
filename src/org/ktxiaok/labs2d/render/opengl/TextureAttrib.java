package org.ktxiaok.labs2d.render.opengl;

public class TextureAttrib {
    public int max_filter;
    public int min_filter;
    public int wrap_s;
    public int wrap_t;

    public TextureAttrib(int max_filter, int min_filte, int wrap_s, int wrap_t) {
        this.max_filter = max_filter;
        this.min_filter = min_filte;
        this.wrap_s = wrap_s;
        this.wrap_t = wrap_t;
    }

    public TextureAttrib(int filter, int wrap) {
        this.max_filter = filter;
        this.min_filter = filter;
        this.wrap_s = wrap;
        this.wrap_t = wrap;
    }
}
