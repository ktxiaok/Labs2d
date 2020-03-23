package org.ktxiaok.labs2d.render.opengl;

public abstract class GL {
    public final int GL_VERTEX_SHADER;
    public final int GL_FRAGMENT_SHADER;
    public final int GL_COMPILE_STATUS;
    public final int GL_LINK_STATUS;
    public final int GL_TRIANGLES;
    public final int GL_UNSIGNED_INT;
    public final int GL_ARRAY_BUFFER;
    public final int GL_ELEMENT_ARRAY_BUFFER;
    public final int GL_FLOAT;
    public final int GL_INT;
    public final int GL_REPEAT;
    public final int GL_CLAMP;
    public final int GL_NEAREST;
    public final int GL_LINEAR;
    public final int[] GL_TEXTURE;
    public final int GL_UNPACK_ALIGNMENT;
    public final int GL_TEXTURE_2D;
    public final int GL_TEXTURE_MIN_FILTER;
    public final int GL_TEXTURE_MAG_FILTER;
    public final int GL_TEXTURE_WRAP_S;
    public final int GL_TEXTURE_WRAP_T;
    public final int GL_RGBA;
    public final int GL_UNSIGNED_BYTE;
    public final int GL_DYNAMIC_DRAW;
    public final int GL_STATIC_DRAW;
    public final int GL_STREAM_DRAW;
    public final int GL_UNIFORM_BUFFER;
    public final int GL_UNIFORM_BLOCK_DATA_SIZE;
    public final int GL_UNIFORM_OFFSET;
    public final int GL_COPY_READ_BUFFER;
    public final int GL_COPY_WRITE_BUFFER;
    public final int GL_COLOR_BUFFER_BIT;
    public GL(){
        GL_VERTEX_SHADER=const_gl_vertex_shader();
        GL_FRAGMENT_SHADER=const_gl_fragment_shader();
        GL_COMPILE_STATUS=const_gl_compile_status();
        GL_LINK_STATUS=const_gl_link_status();
        GL_TRIANGLES=const_gl_triangles();
        GL_UNSIGNED_INT=const_gl_unsigned_int();
        GL_ARRAY_BUFFER=const_gl_array_buffer();
        GL_ELEMENT_ARRAY_BUFFER=const_gl_element_array_buffer();
        GL_FLOAT=const_gl_float();
        GL_INT=const_gl_int();
        GL_REPEAT=const_gl_repeat();
        GL_CLAMP=const_gl_clamp();
        GL_NEAREST=const_gl_nearest();
        GL_LINEAR=const_gl_linear();
        GL_TEXTURE=const_gl_texture_x();
        GL_UNPACK_ALIGNMENT=const_gl_unpack_alignment();
        GL_TEXTURE_2D=const_gl_texture_2d();
        GL_TEXTURE_MIN_FILTER=const_gl_texture_min_filter();
        GL_TEXTURE_MAG_FILTER=const_gl_texture_mag_filter();
        GL_TEXTURE_WRAP_S=const_gl_texture_wrap_s();
        GL_TEXTURE_WRAP_T=const_gl_texture_wrap_t();
        GL_RGBA=const_gl_rgba();
        GL_UNSIGNED_BYTE=const_gl_unsigned_byte();
        GL_DYNAMIC_DRAW=const_gl_dynamic_draw();
        GL_STATIC_DRAW=const_gl_static_draw();
        GL_STREAM_DRAW=const_gl_stream_draw();
        GL_UNIFORM_BUFFER=const_gl_uniform_buffer();
        GL_UNIFORM_BLOCK_DATA_SIZE=const_gl_uniform_block_data_size();
        GL_UNIFORM_OFFSET=const_gl_uniform_offset();
        GL_COPY_READ_BUFFER=const_gl_copy_read_buffer();
        GL_COPY_WRITE_BUFFER=const_gl_copy_write_buffer();
        GL_COLOR_BUFFER_BIT=const_gl_color_buffer_bit();
    }
    protected abstract int const_gl_vertex_shader();
    protected abstract int const_gl_fragment_shader();
    protected abstract int const_gl_compile_status();
    protected abstract int const_gl_link_status();
    protected abstract int const_gl_triangles();
    protected abstract int const_gl_unsigned_int();
    protected abstract int const_gl_array_buffer();
    protected abstract int const_gl_element_array_buffer();
    protected abstract int const_gl_float();
    protected abstract int const_gl_int();
    protected abstract int const_gl_repeat();
    protected abstract int const_gl_clamp();
    protected abstract int const_gl_nearest();
    protected abstract int const_gl_linear();
    protected abstract int[] const_gl_texture_x();
    protected abstract int const_gl_unpack_alignment();
    protected abstract int const_gl_texture_2d();
    protected abstract int const_gl_texture_min_filter();
    protected abstract int const_gl_texture_mag_filter();
    protected abstract int const_gl_texture_wrap_s();
    protected abstract int const_gl_texture_wrap_t();
    protected abstract int const_gl_rgba();
    protected abstract int const_gl_unsigned_byte();
    protected abstract int const_gl_dynamic_draw();
    protected abstract int const_gl_static_draw();
    protected abstract int const_gl_stream_draw();
    protected abstract int const_gl_uniform_buffer();
    protected abstract int const_gl_uniform_block_data_size();
    protected abstract int const_gl_uniform_offset();
    protected abstract int const_gl_copy_read_buffer();
    protected abstract int const_gl_copy_write_buffer();
    protected abstract int const_gl_color_buffer_bit();

    public abstract int glCreateShader(
            int type
    );
    public abstract void glShaderSource(
            int shader,
            String string
    );
    public abstract void glCompileShader(
            int shader
    );
    public abstract void glGetShaderiv(
            int shader,
            int pname,
            int[] params,
            int offset
    );
    public abstract String glGetShaderInfoLog(
            int shader
    );
    public abstract void glDeleteShader(
            int shader
    );
    public abstract int glCreateProgram(
    );
    public abstract void glAttachShader(
            int program,
            int shader
    );
    public abstract void glLinkProgram(
            int program
    );
    public abstract void glGetProgramiv(
            int program,
            int pname,
            int[] params,
            int offset
    );
    public abstract String glGetProgramInfoLog(
            int program
    );
    public abstract void glDeleteProgram(
            int program
    );
    public abstract void glUseProgram(
            int program
    );
    public abstract void glGenVertexArrays(
            int n,
            int[] arrays,
            int offset
    );
    public abstract void glBindVertexArray(
            int array
    );
    public abstract void glEnableVertexAttribArray(
            int index
    );
    public abstract void glDisableVertexAttribArray(
            int index
    );
    public abstract void glDrawElements(
            int mode,
            int count,
            int type,
            int offset
    );
    public abstract void glDrawElementsInstanced(
            int mode,
            int count,
            int type,
            int indicesOffset,
            int instanceCount
    );
    public abstract void glDeleteVertexArrays(
            int n,
            int[] arrays,
            int offset
    );
    public abstract int glGetUniformLocation(
            int program,
            String name
    );
    public abstract void glUniformMatrix3fv(
            int location,
            int count,
            boolean transpose,
            float[] value,
            int offset
    );
    public abstract void glUniform1i(
            int location,
            int x
    );
    public abstract void glUniform1f(
            int location,
            float x
    );
    public abstract void glUniform2f(
            int location,
            float x,
            float y
    );
    public abstract void glUniform3f(
            int location,
            float x,
            float y,
            float z
    );
    public abstract void glUniform4f(
            int location,
            float x,
            float y,
            float z,
            float w
    );
    public abstract void glUniform1fv(
            int location,
            int count,
            float[] v,
            int offset
    );
    public abstract void glUniform1iv(
            int location,
            int count,
            int[] v,
            int offset
    );
    public abstract void glGenBuffers(
            int n,
            int[] buffers,
            int offset
    );
    public abstract void glBindBuffer(
            int target,
            int buffer
    );
    public abstract void glDeleteBuffers(
            int n,
            int[] buffers,
            int offset
    );
    public abstract void glBufferData(
            int target,
            int size,
            java.nio.Buffer data,
            int usage
    );
    public abstract void glBufferSubData(
            int target,
            int offset,
            int size,
            java.nio.Buffer data
    );
    public abstract void glCopyBufferSubData(
            int readTarget,
            int writeTarget,
            int readOffset,
            int writeOffset,
            int size
    );
    public abstract void glVertexAttribPointer(
            int indx,
            int size,
            int type,
            boolean normalized,
            int stride,
            int offset
    );
    public abstract void glVertexAttribDivisor(
            int index,
            int divisor
    );
    public abstract void glGenTextures(
            int n,
            int[] textures,
            int offset
    );
    public abstract void glPixelStorei(
            int pname,
            int param
    );
    public abstract void glTexParameteri(
            int target,
            int pname,
            int param
    );
    public abstract void glTexImage2D(
            int target,
            int level,
            int internalformat,
            int width,
            int height,
            int border,
            int format,
            int type,
            java.nio.Buffer pixels
    );
    public abstract void glBindTexture(
            int target,
            int texture
    );
    public abstract void glDeleteTextures(
            int n,
            int[] textures,
            int offset
    );
    public abstract void glActiveTexture(
            int texture
    );
    public abstract int glGetUniformBlockIndex(
            int program,
            String uniformBlockName
    );
    public abstract void glUniformBlockBinding(
            int program,
            int uniformBlockIndex,
            int uniformBlockBinding
    );
    public abstract void glGetActiveUniformBlockiv(
            int program,
            int uniformBlockIndex,
            int pname,
            int[] params,
            int offset
    );
    public abstract void glBindBufferBase(
            int target,
            int index,
            int buffer
    );
    public abstract void glGetUniformIndices(
            int program,
            String[] uniformNames,
            int[] uniformIndices,
            int uniformIndicesOffset
    );
    public abstract void glGetActiveUniformsiv(
            int program,
            int uniformCount,
            int[] uniformIndices,
            int uniformIndicesOffset,
            int pname,
            int[] params,
            int paramsOffset
    );
    public abstract void glClear(
            int mask
    );
    public abstract void glScissor(
            int x,
            int y,
            int width,
            int height
    );
    public abstract void glViewport(
            int x,
            int y,
            int width,
            int height
    );



}
