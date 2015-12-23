package com.rdupuis.gamefactory.providers;

import java.util.ArrayList;
import java.util.HashMap;

import com.rdupuis.gamefactory.components.AbstractGameObject;
import com.rdupuis.gamefactory.components.CopoundGameObject;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.Scene;
import com.rdupuis.gamefactory.shaders.ProgramShader;


import android.opengl.GLES20;
import android.util.Log;

/**
 * le shader provider va référencer les program Shader a utiliser
 *
 * @author NC10
 */
public class ProgramShaderManager {


    public Scene mScene;
    public ArrayList<ProgramShader> shaderList;
    public HashMap<String, Integer> catalogShader;

    public HashMap<GameObject, ProgramShader> gameObjectShaderList;

    public ProgramShader getCurrentActiveShader() {
        return mCurrentActiveShader;
    }

    private ProgramShader mCurrentActiveShader;

    // déclaration des attributs du shader : default
    public final String DEFAULT_VSH_ATTRIB_VERTEX_COORD = "aPosition";
    public final String DEFAULT_VSH_ATTRIB_COLOR = "aColor";
    public final String DEFAULT_VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

    public final String DEFAULT_VSH_UNIFORM_MVP = "uMvp";
    public final String DEFAULT_FSH_UNIFORM_TEXTURE = "tex0";

    public ProgramShader getDefaultShader() {
        return mDefaultShader;
    }

    public void setDefaultSader(ProgramShader mDefaultShader) {
        this.mDefaultShader = mDefaultShader;
    }

    private ProgramShader mDefaultShader;


    /**
     * getter & setter
     */
    public HashMap<GameObject, ProgramShader> getGameObjectShaderList() {
        return gameObjectShaderList;
    }

    public void setGameObjectShaderList(HashMap<GameObject, ProgramShader> gameObjectShaderList) {
        this.gameObjectShaderList = gameObjectShaderList;
    }

    public Scene getScene() {
        return mScene;
    }

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }

    /***
     * Constructeur
     */
    public ProgramShaderManager(Scene scene) {
        this.setScene(scene);
        this.mCurrentActiveShader = null;
        catalogShader = new HashMap<String, Integer>();
        shaderList = new ArrayList<ProgramShader>();
        this.gameObjectShaderList = new HashMap<GameObject, ProgramShader>();
    }


    /***
     * Ajouter un shader dans le catalogue
     */
    public void add(ProgramShader shader) {
        shader.getClass().getName();
        int newindex = catalogShader.size() + 1;
        catalogShader.put(shader.mName, newindex);
        shaderList.add(shader);
    }

    /**
     * récupérer un shader via son nom
     *
     * @param shaderName
     * @return
     */
    public ProgramShader getShaderByName(String shaderName) {
        ProgramShader result = null;
        if (catalogShader.get(shaderName) == null) {
            Log.e(this.getClass().getName(), "Shader " + shaderName
                    + " unknow on Catalog");
        } else {
            result = shaderList.get(catalogShader.get(shaderName) - 1);
        }
        return result;
    }

    /**
     * @param shaderName
     */
    public void use(String shaderName) {
        ProgramShader sh = this.getShaderByName(shaderName);
        this.use(sh);

    }

    /**
     * Activer l'utilisation d'un shader
     *
     * @param shader
     */
    public void use(ProgramShader shader) {

        // use program
        if (this.mCurrentActiveShader != shader
                || this.mCurrentActiveShader == null) {

            if (this.mCurrentActiveShader != null) {
                this.mCurrentActiveShader.disableAttribs();
            }
            GLES20.glUseProgram(shader.mGLSLProgram_location);
            this.mCurrentActiveShader = shader;

        }


    }

    /**
     * effectuer le rendu de la liste des objets
     *
     * @param goList
     */
    public void render(ArrayList<AbstractGameObject> goList) {
        for (AbstractGameObject gameObject : goList) {

            //Dessiner
            if (gameObject.getVisibility()) {
                //Appel au shader de l'objet s'il en requiert un en particulier.
                //sinon on utilise le shader par defaut.
                if (this.getGameObjectShaderList().get(gameObject) != null) {
                    this.use(this.getGameObjectShaderList().get(gameObject));
                } else this.use(this.getDefaultShader());

                //TODO  si c'est un objet composé, il faut dessiner les composants

                if (CopoundGameObject.class.isInstance(gameObject)) {

                    CopoundGameObject cgo = (CopoundGameObject) gameObject;

                    for (GameObject rgo : cgo.getGameObjectList()) {

                        this.getCurrentActiveShader().draw(rgo, this.getScene().getProjectionView());
                    }


                } else {
                    // on demande su shader de rendre l'objet
                    GameObject go = (GameObject) gameObject;
                    this.getCurrentActiveShader().draw(go, this.getScene().getProjectionView());
                }


            }
        }
    }


}
