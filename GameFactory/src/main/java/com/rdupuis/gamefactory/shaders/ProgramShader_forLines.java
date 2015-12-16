package com.rdupuis.gamefactory.shaders;


import android.opengl.GLES20;

public class ProgramShader_forLines extends ProgramShader {

    public static final String SHADER_FOR_LINES = "SHDR_FOR_LINES";
    // déclaration des attributs spécifiques au shader
    public final String VSH_ATTRIB_VERTEX_COORD = "aPosition";
    public final String VSH_ATTRIB_COLOR = "aColor";
    public final String VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

    // déclaration des uniforms spécifiques au shader
    public final String VSH_UNIFORM_MVP = "uMvp";


    public ProgramShader_forLines() {
        super();

    }

    @Override
    public void initCode() {
        this.mName = SHADER_FOR_LINES;
        this.attrib_vertex_coord_name = this.VSH_ATTRIB_VERTEX_COORD;
        this.attrib_color_name = this.VSH_ATTRIB_COLOR;
        this.uniform_mvp_name = this.VSH_UNIFORM_MVP;


        this.fragmentShaderSource =
                "#ifdef GL_ES \n"
                        + " precision highp float; \n"
                        + " #endif \n"

                       // + " varying vec2 vTexCoord; "
                        + " varying vec4 vColor;"
            //            + " varying vec3 pos;"
                        + " void main() {"
     //                    + "    gl_FragColor =  vColor;"
                       + "    gl_FragColor =  vec4(1.,0.,1.,1.0);"

                        + "}";

        this.vertexShaderSource =
                "uniform mat4 " + this.VSH_UNIFORM_MVP + ";"
                        + "attribute vec3 " + this.VSH_ATTRIB_VERTEX_COORD + ";"
                        + "attribute vec4 " + this.VSH_ATTRIB_COLOR + ";"
                        + "varying vec4 vColor;"

                        + "void main() {"
                        // on calcule la position du point via la matrice de projection
                        + " vec4 position = "+this.VSH_UNIFORM_MVP+" * vec4(" + this.VSH_ATTRIB_VERTEX_COORD + ".xyz, 1.);"
                      //  + "vec4 position = vec4(aPosition.xyz, 1.);"
                        + " vColor = " + this.VSH_ATTRIB_COLOR + ";"
                     //   + " gl_PointSize = 1.0;"
                        // cette commande doit toujours �tre la derni�re du vertex shader.
                        + "	gl_Position =  position;"
                        + "}";

    }

    @Override
    public void initLocations() {
        // les attribs
        this.attrib_vertex_coord_location = GLES20.glGetAttribLocation(
                mGLSLProgram_location, this.VSH_ATTRIB_VERTEX_COORD);
        this.attrib_color_location = GLES20.glGetAttribLocation(
                mGLSLProgram_location, this.VSH_ATTRIB_COLOR);


        // les Uniforms
        this.uniform_mvp_location = GLES20.glGetUniformLocation(
                this.mGLSLProgram_location, this.VSH_UNIFORM_MVP);

    }

    // *******************************************************************
    // Attention : il ne faut pas rendre enable un attribut non valoris�
    // sinon c'est ecran noir !
    @Override
    public void enableAttribs() {
        GLES20.glEnableVertexAttribArray(this.attrib_vertex_coord_location);
        GLES20.glEnableVertexAttribArray(this.attrib_color_location);

    }

    // **************************************************************************
    @Override
    public void disableAttribs() {
        GLES20.glDisableVertexAttribArray(this.attrib_vertex_coord_location);
        GLES20.glDisableVertexAttribArray(this.attrib_color_location);
        GLES20.glDisableVertexAttribArray(this.uniform_mvp_location);


    }


}
