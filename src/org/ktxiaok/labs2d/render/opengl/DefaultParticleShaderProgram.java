package org.ktxiaok.labs2d.render.opengl;

public final class DefaultParticleShaderProgram extends BasicShaderProgram {
    public static final String VERTEX_SHADER_CODE="#version 300 es\n" +
            "precision mediump float;\n" +
            "precision mediump int;\n" +
            "layout (location=0) in vec2 vertexPos;\n" +
            "layout (location=1) in vec2 texcoord;\n" +
            "layout (location=2) in vec4 color;\n" +
            "layout (location=3) in vec2 transformT;\n" +
            "layout (location=4) in vec2 transformS;\n" +
            "layout (location=5) in float transformA;\n" +
            "layout (location=6) in vec4 textureRegion;\n" +
            "layout (location=7) in float textureRotate;\n" +
            "\n" +
            "uniform mat3 modelMatrix\n" +
            "uniform mat3 viewProjectionMatrix;\n" +
            "uniform int ignoreCamera;\n" +
            "out vec2 outTexcoord;\n" +
            "out vec4 outColor;\n" +
            "void transform(float tx,float ty,float sx,float sy,float angle,inout vec2 point){\n" +
            "\tpoint.x*=sx;\n" +
            "\tpoint.y*=sy;\n" +
            "\tfloat sina=sin(angle);\n" +
            "\tfloat cosa=cos(angle);\n" +
            "\tfloat x1=point.x*cosa-point.y*sina;\n" +
            "    float y1=point.x*sina+point.y*cosa;\n" +
            "\tx1+=tx;\n" +
            "\ty1+=ty;\n" +
            "\tpoint.x=tx;\n" +
            "\tpoint.y=ty;\n" +
            "}\n" +
            "void convertTexcoord(out vec2 convertedTexcoord){\n" +
            "\tvec2 point;\n" +
            "\tpoint=vec2(mix(texcoord.x,texcoord.y,textureRotate),mix(texcoord.y,1.0-texcoord.x,textureRotate));\n" +
            "\tconvertedTexcoord.x=textureRegion.x+textureRegion.z*point.x;\n" +
            "\tconvertedTexcoord.y=textureRegion.y+textureRegion.w*point.y;\t\n" +
            "}\n" +
            "void main(){\n" +
            "\tconvertTexcoord(outTexcoord);\n" +
            "\toutColor=color;\n" +
            "\tvec2 vertexPos0=vertexPos.xy;\n" +
            "\ttransform(transformT.x,transformT.y,transformS.x,transformS.y,transformA,vertexPos);\n" +
            "\tmat3 transformMatrix;\n" +
            "\tif(ignoreCamera==0){\n" +
            "\t\ttransformMatrix=viewProjectionMatrix*modelMatrix;\n" +
            "\t}else{\n" +
            "\t\ttransformMatrix=modelMatrix;\n" +
            "\t}\n" +
            "\tgl_Position=vec4(transformMatrix*vec3(vertexPos0,1.0),1.0);\n" +
            "}\n";
    public static final String FRAGMENT_SHADER_CODE="#version 300 es\n" +
            "precision mediump float;\n" +
            "precision mediump int;\n" +
            "in vec2 outTexcoord;\n" +
            "in vec4 outColor;\n" +
            "uniform sampler2D mainSampler;\n" +
            "out vec4 fragColor;\n" +
            "void main(){\n" +
            "\tvec4 textureColor=texture(mainSampler,outTexcoord);\n" +
            "\tfragColor=vec4(outColor.rgb+textureColor.rgb,outColor.a*textureColor.a);\n" +
            "}";

    public DefaultParticleShaderProgram(){
        super(VERTEX_SHADER_CODE,FRAGMENT_SHADER_CODE);
        int glFloat=GLContext.GL().GL_FLOAT;
        putVertexAttrib(new VertexAttrib(0,2,glFloat));
        putVertexAttrib(new VertexAttrib(1,2,glFloat));
        putVertexAttrib(new VertexAttrib(2,4,glFloat,1));
        putVertexAttrib(new VertexAttrib(3,2,glFloat,1));
        putVertexAttrib(new VertexAttrib(4,2,glFloat,1));
        putVertexAttrib(new VertexAttrib(5,1,glFloat,1));
        putVertexAttrib(new VertexAttrib(6,4,glFloat,1));
        putVertexAttrib(new VertexAttrib(7,1,glFloat,1));
    }

}
