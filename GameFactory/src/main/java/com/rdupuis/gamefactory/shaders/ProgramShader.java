package com.rdupuis.gamefactory.shaders;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.Scene;
import com.rdupuis.gamefactory.components.Vertex;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class ProgramShader extends AbstractProgramShader {

    public String mName;

    // ! GLES20 Program
    public int mGLSLProgram_location;

    // ! Vertex shader
    public String vertexShaderSource;
    public int VertexShader_location;

    // Fragment shader
    public String fragmentShaderSource;
    public int FragmentShader_location;

    // déclaration des attributs spécifiques au shader
    public String attrib_vertex_coord_name;
    public int attrib_vertex_coord_location;

    public String attrib_color_name;
    public int attrib_color_location;

    public String attrib_texture_coord_name;
    public int attrib_texture_coord_location;

    public String uniform_mvp_name;
    public int uniform_mvp_location;

    public String uniform_texture_buffer_name;
    public int uniform_texture_location;

    public String uniform_alpha_name;
    public int uniform_alpha_location;


    public ProgramShader() {
        mGLSLProgram_location = 0;
        VertexShader_location = 0;
        FragmentShader_location = 0;
        this.attrib_vertex_coord_location = -1;
        this.attrib_color_location = -1;
        this.attrib_texture_coord_location = -1;
        this.uniform_mvp_location = -1;
        this.uniform_texture_location = -1;
        this.uniform_alpha_location = -1;
        this.initCode();
        this.make();
    }

    public void make() {
        mGLSLProgram_location = GLES20.glCreateProgram();
        this.loadShaders(vertexShaderSource, fragmentShaderSource);
        this.initLocations();
    }

    public void initLocations() {

    }

    public void initCode() {

    }

    public void delete() {
        // delete Vertex shader
        if (VertexShader_location != 0) {
            GLES20.glDeleteShader(VertexShader_location);
        }
        // delete Fragment shader
        if (FragmentShader_location != 0) {
            GLES20.glDeleteShader(FragmentShader_location);
        }
        // delete a GLES20 program
        if (mGLSLProgram_location != 0) {
            GLES20.glDeleteProgram(mGLSLProgram_location);
        }
    }

    /**
     * @param vertexCode
     * @param fragmentCode
     * @return
     */
    public boolean loadShaders(String vertexCode, String fragmentCode) {
        if (this.mGLSLProgram_location == 0) {
            Log.e(this.getClass().getName(), "No GLSL Program created!");
            return false;
        }

        this.FragmentShader_location = this.loadShader(
                this.fragmentShaderSource, GLES20.GL_FRAGMENT_SHADER);

        this.VertexShader_location = this.loadShader(this.vertexShaderSource,
                GLES20.GL_VERTEX_SHADER);

        // if one of shader cannot be read return false
        if (this.FragmentShader_location == 0
                || this.VertexShader_location == 0) {
            Log.e(this.getClass().getName(), "Shader doesn' compile");
            return false;
        }

        GLES20.glAttachShader(this.mGLSLProgram_location,
                this.VertexShader_location);

        GLES20.glAttachShader(this.mGLSLProgram_location,
                this.FragmentShader_location);
        link();
        return true;
    }

    /**
     * /* load a Vertex or Fragment shader
     *
     * @throws IOException
     */
    private int loadShader(String source, int shaderType) {

        int shader = GLES20.glCreateShader(shaderType);

        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(this.getClass().getName(), "Could not compile shader "
                        + shaderType + ":");
                throw new RuntimeException("Erreur de compilation du Shader : " + GLES20.glGetShaderInfoLog(shader));
                //GLES20.glDeleteShader(shader);
                //shader = 0;
            }
        }
        Log.i(this.getClass().getName(), this.mName + " : " + shaderType
                + " shader compiled");
        return shader;
    }

    /**
     * @return
     */
    public boolean link() {
        if (this.mGLSLProgram_location == 0) {
            Log.e(this.getClass().getName(),
                    "Please create a GL program before Link shaders!");
            return false;
        }

        GLES20.glLinkProgram(this.mGLSLProgram_location);

        int[] linkStatus = new int[1];

        GLES20.glGetProgramiv(this.mGLSLProgram_location,
                GLES20.GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e(this.getClass().getName(), "Could not link program: ");
            Log.e(this.getClass().getName(),
                    "logs:"
                            + GLES20.glGetProgramInfoLog(this.mGLSLProgram_location));
            GLES20.glDeleteProgram(this.mGLSLProgram_location);
            this.mGLSLProgram_location = 0;
            return false;
        }

        Log.i("Shader.link()", "Shader linkded");
        return true;
    }

    /**
     * @param fb
     */
    public void setVerticesCoord(FloatBuffer fb) {
        GLES20.glVertexAttribPointer(this.attrib_vertex_coord_location, 3,
                GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES, fb);

    }

    /**
     * @param VboBufferIndex
     */
    public void setVBOVerticesCoord(int VboBufferIndex) {
        //on se positionne sur le buffer souhaité
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VboBufferIndex);

        //plutôt qur de passer des valeur au shader, on passe un pointeur vers le buffer
        GLES20.glVertexAttribPointer(this.attrib_vertex_coord_location, 3,
                GLES20.GL_FLOAT, false, 0, 0);

        //on active l'utilisation de la variable aPostion dans le shader
        GLES20.glEnableVertexAttribArray(this.attrib_vertex_coord_location);

    }

    /**
     * @param fb
     */
    public void setTextureCoord(FloatBuffer fb) {
        GLES20.glVertexAttribPointer(this.attrib_texture_coord_location, 2,
                GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, fb);

    }

    @Override
    public void enableAttribs() {

    }

    @Override
    public void disableAttribs() {

    }


    /**
     * On demande au shader de dessiner un objet
     * nb: le shader doit être activé par le manager. c'est lui qui sait quel est le shader
     * à utiliser
     *
     * @param gameObject
     */

    public void draw(GameObject gameObject, float[] projectionMatrix) {
        //si l'utilisation d'une texture est active on l'utilise
        if (gameObject.isTextureEnabled()) {

            //on active l'unité de traitement des textures 0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

            // on demande à opengl d'utiliser la texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, gameObject.getTexture().getGlBufferId());

            //on fait pointer  uniform_texture_location sur le buffer où est la texture
            GLES20.glUniform1i(this.uniform_texture_location, 0);
        }

        /********************************************************************************
         * les données Varying du shader doivent être connues POUR CHAQUES VERTEX !!!!
         * si on passe la couleur en varying, on doit avoit un buffer couleur
         * qui contient les couleurs de chaques vertex !!
         * pour rappel, on a 1 vertex Shader par vertex !!!
         *
         * Si tous les vertex ont la même couleur , on peu simplement utiliser un Uniform
         * car les uniforms sont connus pour tous les vertex shader
         */

        //--------------------------------------------

        //je me place sur le buffer stride qui contient x,y,z,u,v
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, gameObject.getGlVBoId());

        //lecture des coordonées x,y,z:
        // plutôt que de passer des valeur au shader, on indique une position en int.
        // OpenGl comprend alors qu'il faut passer par le pointeur sur lequel on vient de
        // se positionner pour aller chercher les valeurs contenue dans la mémoire graphique
        // ici on commence à lire le buffer à partir de la position zéro et on fait des saut
        // de "stride=5" pour passer aux coordonnées suivantes
        GLES20.glVertexAttribPointer(this.attrib_vertex_coord_location, Vertex.Vertex_COORD_SIZE,
                GLES20.GL_FLOAT, false, Vertex.stride * Vertex.FLOAT_SIZE, 0);

        //ici on commence la lecture des coordonées de texture juste après les premier x,y,z
        // ensuite on fait des saut pour lire les suivantes
        GLES20.glVertexAttribPointer(this.attrib_texture_coord_location, Vertex.Vertex_TEXT_SIZE,
                GLES20.GL_FLOAT, false, Vertex.stride * Vertex.FLOAT_SIZE, Vertex.Vertex_COORD_SIZE * Vertex.FLOAT_SIZE);

        //ici on commence la lecture des coordonées de texture juste après les premier x,y,z
        // ensuite on fait des saut pour lire les suivantes
        GLES20.glVertexAttribPointer(this.attrib_color_location, Vertex.Vertex_COLOR_SIZE,
                GLES20.GL_FLOAT, false, Vertex.stride * Vertex.FLOAT_SIZE, (Vertex.Vertex_COORD_SIZE + Vertex.Vertex_TEXT_SIZE) * Vertex.FLOAT_SIZE);

        //--------------------------------------------
        this.enableAttribs();

        //Calcul de la matrice model-view-projection
        float[] mMvp = new float[16];
        Matrix.multiplyMM(mMvp, 0, projectionMatrix, 0, gameObject.mModelView, 0);

        // On alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(this.uniform_mvp_location, 1, false, mMvp, 0);

        //on alimente la donnée Alpha
        GLES20.glUniform1f(this.uniform_alpha_location, gameObject.getAlpha());

        //je me place sur le buffer des index
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, gameObject.getGlVBiId());

        //je dessine les vertex selon l'ordre indiqué dans l'index.
        // au lieu de fournir des valeurs on indique zéro. ce qui permet à opengl de comprendre
        // qu'il faut passer par le pointeur sur lequel on viens de se postionner
        ///this.drawMode = GLES20.GL_LINES;
        //drawElement
        // mode de dessin
        //nombre d'indices (en théorie = 6)
        //

        GLES20.glDrawElements(gameObject.drawMode, gameObject.getNbIndex(),
                GLES20.GL_UNSIGNED_SHORT, 0);

        //je pointe les buffer sur "rien" pour ne pas les réutiliser par erreur
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        //--------------------------------------------
        this.disableAttribs();

    }


}
