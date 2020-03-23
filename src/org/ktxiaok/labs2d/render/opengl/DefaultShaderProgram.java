package org.ktxiaok.labs2d.render.opengl;

public class DefaultShaderProgram extends BasicShaderProgram {
    public static final String VERTEX_SHADER_CODE="#version 300 es\n" +
            "precision mediump float;\n" +
            "precision mediump int;\n" +
            "layout (location=0) in vec2 vertexPos;\n" +
            "layout (location=1) in vec2 texcoord;\n" +
            "uniform mat3 modelMatrix;\n" +
            "uniform mat3 viewProjectionMatrix;\n" +
            "uniform int ignoreCamera;\n" +
            "uniform vec4 textureRegion;\n" +
            "uniform int textureRotate;\n" +
            "out vec2 outTexcoord;\n" +
            "void convertTexcoord(out vec2 convertedTexcoord){\n" +
            "\tvec2 point;\n" +
            "\tif(textureRotate==0){\n" +
            "\t\tpoint=vec2(texcoord);\n" +
            "\t}else{\n" +
            "\t\tpoint=vec2(texcoord.y,1.0-texcoord.x);\n" +
            "\t}\n" +
            "\tconvertedTexcoord.x=textureRegion.x+textureRegion.z*point.x;\n" +
            "\tconvertedTexcoord.y=textureRegion.y+textureRegion.w*point.y;\t\n" +
            "}\n" +
            "void main(){\n" +
            "\tconvertTexcoord(outTexcoord);\n" +
            "\tmat3 transformMatrix;\n" +
            "\tif(ignoreCamera==0){\n" +
            "\t\ttransformMatrix=viewProjectionMatrix*modelMatrix;\n" +
            "\t}else{\n" +
            "\t\ttransformMatrix=modelMatrix;\n" +
            "\t}\n" +
            "\tgl_Position=vec4(transformMatrix*vec3(vertexPos,1.0),1.0);\n" +
            "}\n";
    public static final String FRAGMENT_SHADER_CODE="#version 300 es\n" +
            "precision mediump float;\n" +
            "precision mediump int;\n" +
            "in vec2 outTexcoord;\n" +
            "uniform sampler2D mainSampler;\n" +
            "out vec4 fragColor;\n" +
            "void main(){\n" +
            "\tfragColor=texture(mainSampler,outTexcoord);\n" +
            "}";

    public static final String UNIFORM_TEXTURE_REGION="textureRegion";
    public static final String UNIFORM_TEXTURE_ROTATE="textureRotate";

    public DefaultShaderProgram(){
        super(VERTEX_SHADER_CODE,FRAGMENT_SHADER_CODE);
        putVertexAttrib(new VertexAttrib(0,2,GLContext.GL().GL_FLOAT));
        putVertexAttrib(new VertexAttrib(1,2,GLContext.GL().GL_FLOAT));
    }
    public void setTextureRegion(float offsetX,float offsetY,float tilingX,float tilingY,boolean rotate){
//        setUniform(UNIFORM_TEXTURE_OFFSET,offsetX,offsetY);
//        setUniform(UNIFORM_TEXTURE_TILING,tilingX,tilingY);
        setUniform(UNIFORM_TEXTURE_REGION,offsetX,offsetY,tilingX,tilingY);
        setUniform(UNIFORM_TEXTURE_ROTATE,rotate?1:0);
    }
    public void setTextureRegion(TextureRegion textureRegion){
        setTextureRegion(textureRegion.getOffsetX(),textureRegion.getOffsetY(),
                textureRegion.getTilingX(),textureRegion.getTilingY(),textureRegion.isRotate());
    }

}
