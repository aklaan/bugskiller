package com.rdupuis.gamefactory.shaders;


import android.opengl.GLES20;

public class ProgramShader_simple extends ProgramShader {

    // déclaration des attributs spécifiques au shader
    public final String VSH_ATTRIB_VERTEX_COORD = "aPosition";
    public final String VSH_ATTRIB_COLOR = "aColor";
    public final String VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";


    // déclaration des uniforms spécifiques au shader
    public final String VSH_UNIFORM_MVP = "uMvp";
    public final String FSH_UNIFORM_TEXTURE = "tex0";
    public final String FSH_UNIFORM_ALPHA = "aAlpha";

    public ProgramShader_simple() {
        super();

    }

    @Override
    public void initCode() {
        this.mName = "simple";
        this.attrib_vertex_coord_name = this.VSH_ATTRIB_VERTEX_COORD;
        this.attrib_color_name = this.VSH_ATTRIB_COLOR;
        this.attrib_texture_coord_name = this.VSH_ATTRIB_TEXTURE_COORD;
        this.uniform_mvp_name = this.VSH_UNIFORM_MVP;
        this.uniform_texture_buffer_name = this.FSH_UNIFORM_TEXTURE;

        this.fragmentShaderSource = "#ifdef GL_ES \n"
                // pour les fragment shader, il n'existe pas de de précision par défaut
                // il est donc necessaire de la précisier soit pour tous (comme
                // ici) ou pour chaque variable.
                + " precision highp float; \n" + " #endif \n"
                + " uniform sampler2D " + this.FSH_UNIFORM_TEXTURE + ";"
                + " uniform float " + this.FSH_UNIFORM_ALPHA + ";"
                + " varying vec2 vTexCoord; " + " varying vec4 vColor;"
                + " varying vec3 pos;" + " void main() {"
                // + "    gl_FragColor =  vColor;"
                + "gl_FragColor = texture2D(tex0, vTexCoord) * " + this.FSH_UNIFORM_ALPHA + "; "
                // +
                // "    gl_FragColor =  vec4(sin(pos.x), sin(pos.y), 0.0, 1.0);"
                + " " + "}";

        this.vertexShaderSource =

                // il n'y a pas de déclaration de la version GLSL a utiliser. c'est par défaut 1.0 car
                // pour openGL 2.0 c'est la même que pour opengl 1.x c'est à dire 1.00
                // ce n'est qu'avec la version 3.0 que l'on doit spécifier la version 3.0.

                // par defaut si on ne précise rien toutes les variables du vertex
                // shader sont en hight precision
                // si on réduit la précision, on gagne en rapididté mais on perd
                // potentiellement en qualité (artefacts)
                // on peu préciser la précision sur une variable en particulier
                // exemlple :
                // highp vec4 position;
                // varying lowp vec4 color

                // ou bien déclarer une bonne fois pour toute en debut de programme
                // ex: precision highp float;

                "uniform mat4 " + this.VSH_UNIFORM_MVP + ";"
                        + "uniform float " + this.FSH_UNIFORM_ALPHA + ";"
                        + "attribute vec3 " + this.VSH_ATTRIB_VERTEX_COORD + ";"
                        + "attribute vec2 " + this.VSH_ATTRIB_TEXTURE_COORD + ";"
                        + "attribute vec4 " + this.VSH_ATTRIB_COLOR + ";"
                        + "varying vec4 vColor;"
                        + "varying vec2 vTexCoord;"
                        + "varying vec3 pos;"

                        + "void main() {"
                        // on calcule la position du point via la matrice de projection
                        + " pos = " + this.VSH_ATTRIB_VERTEX_COORD + ";"

                        + " vec4 position = uMvp * vec4(" + this.VSH_ATTRIB_VERTEX_COORD + ".xyz, 1.);"
                        // + " vec4 position = vec4(aPosition.xyz, 1.);"
                        + " vColor = aColor;" + " vTexCoord = aTexCoord;"
                        + "gl_PointSize = 10.;"
                        // cette commande doit toujours être la dernière du vertex shader.
                        + "	gl_Position =  position;" + "}";

    }

    /**
     * @Override public void make() { super.make(); this.initLocations(); }
     */
    @Override
    public void initLocations() {
        // les attribs
        this.attrib_vertex_coord_location = GLES20.glGetAttribLocation(
                mGLSLProgram_location, this.VSH_ATTRIB_VERTEX_COORD);
        this.attrib_color_location = GLES20.glGetAttribLocation(
                mGLSLProgram_location, this.VSH_ATTRIB_COLOR);
        this.attrib_texture_coord_location = GLES20.glGetAttribLocation(
                this.mGLSLProgram_location, this.VSH_ATTRIB_TEXTURE_COORD);

        // les Uniforms

        this.uniform_mvp_location = GLES20.glGetUniformLocation(
                this.mGLSLProgram_location, this.VSH_UNIFORM_MVP);

        this.uniform_texture_location = GLES20.glGetUniformLocation(
                this.mGLSLProgram_location, this.FSH_UNIFORM_TEXTURE);

        this.uniform_alpha_location = GLES20.glGetUniformLocation(
                this.mGLSLProgram_location, this.FSH_UNIFORM_ALPHA);
    }

    // *******************************************************************
    // Attention : il ne faut pas rendre enable un attribut non valorisé
    // sinon c'est ecran noir !
    public void enableShaderVar() {
        GLES20.glEnableVertexAttribArray(this.attrib_vertex_coord_location);
        //GLES20.glEnableVertexAttribArray(this.attrib_color_location);
        GLES20.glEnableVertexAttribArray(this.attrib_texture_coord_location);

        // /les uniforms ne sont pas attrib !!
        // GLES20.glEnableVertexAttribArray(this.uniform_mvp_location);
        // GLES20.glEnableVertexAttribArray(this.uniform_texture_location);
    }

    // **************************************************************************
    public void disableShaderVar() {
        GLES20.glDisableVertexAttribArray(this.attrib_vertex_coord_location);
        GLES20.glDisableVertexAttribArray(this.attrib_color_location);
        GLES20.glDisableVertexAttribArray(this.attrib_texture_coord_location);
        GLES20.glDisableVertexAttribArray(this.uniform_mvp_location);
        GLES20.glDisableVertexAttribArray(this.uniform_texture_location);

    }

}
