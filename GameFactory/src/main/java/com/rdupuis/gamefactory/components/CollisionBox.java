package com.rdupuis.gamefactory.components;

import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.providers.ProgramShaderManager;
import com.rdupuis.gamefactory.shaders.ProgramShader;
import com.rdupuis.gamefactory.shaders.ProgramShader_forLines;
import com.rdupuis.gamefactory.utils.CONST;
import com.rdupuis.gamefactory.utils.Tools;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class CollisionBox {

    private final float defaultOffset = 0.1f;

    //je pense qu'il est préférable de mémoriser l'objet plutôt
    //qu'un ID car il est fort probable que JAVA utilise un pointeur vers l'objet
    //si on utilise un id, on va devoir faire un calcul pour retrouver l'objet dans
    // la liste
    private GameObject mGameObject;
    private ArrayList<Vertex> mInnerVertices;
    private Boolean visibility;

    //tableau de vertices pour les coordonées world de la boite
    public ArrayList<Vertex> mWorldVertices; // d�finition d'un tableau de vertex

    /*****************************************************************************
     * getter & setter
     *****************************************************************************/
    public ArrayList<Vertex> getVertices() {
        return mWorldVertices;
    }

    public void setVertices(ArrayList<Vertex> mWorldVertices) {
        this.mWorldVertices = mWorldVertices;
    }


    public Boolean isVisible() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public ArrayList<Vertex> getInnerVertices() {
        return mInnerVertices;
    }

    public void setInnerVertices(ArrayList<Vertex> mInnerVertices) {
        this.mInnerVertices = mInnerVertices;
    }


    /**
     * gette & setter
     */


    public GameObject getGameObject() {
        return mGameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.mGameObject = gameObject;
    }

    /**
     * Constructor 1 : avec offset
     */
    public CollisionBox(GameObject gameObject, float offsetX, float offsetY) {
        this.commonInitialization(gameObject);
        this.initInnerVertices(offsetX, offsetX);
    }

    /**
     * Contructor 2 : avec offset par defaut
     *
     * @param gameObject
     */
    public CollisionBox(GameObject gameObject) {
        this.mInnerVertices = new ArrayList<Vertex>();
        this.commonInitialization(gameObject);
        this.initInnerVertices(defaultOffset, defaultOffset);
    }


    /**
     * @param gameObject
     */
    private void commonInitialization(GameObject gameObject) {
        //par défaut la box n'est pas visible
        this.setVisibility(false);

        //On mémorise la référence du gameObject "parent"
        this.setGameObject(gameObject);

    }

    /**
     * @param offsetX
     * @param offsetY
     */
    private void initInnerVertices(float offsetX, float offsetY) {
        //Aller rechercher les points limite de la forme et en déduire
        //un rectangle avec un retrait "offset"
        float xread = 0f;
        float yread = 0f;
        float xmin = 0f;
        float xmax = 0f;
        float ymin = 0f;
        float ymax = 0f;

        // pour chaque vertex composant la forme, on va en déterminer les
        // limites pour fabriquer une boite de colision
        for (int i = 0; i < this.getGameObject().getVertices().size(); i++) {

            // lecture du X
            xread = this.getGameObject().getVertices().get(i).x;
            xmin = (xread < xmin) ? xread : xmin;
            xmax = (xread > xmax) ? xread : xmax;

            // lecture du Y
            yread = this.getGameObject().getVertices().get(i).y;
            ymin = (yread < ymin) ? yread : ymin;
            ymax = (yread > ymax) ? yread : ymax;

			/*
             * Log.i("xy",String.valueOf(i) +" / " +String.valueOf(taillemax)
			 * +" : " +String.valueOf(xmin)+ "/" + String.valueOf(xmax));
			 */
        }

        //gestion des offset en taille
        xmin += offsetX;
        xmax += -offsetX;
        ymin += offsetY;
        ymax += -offsetY;


        //on ajoute les vertex du rectangle
        this.getInnerVertices().add(new Vertex(xmin, ymax, 0));
        this.getInnerVertices().add(new Vertex(xmin, ymin, 0));
        this.getInnerVertices().add(new Vertex(xmax, ymin, 0));
        this.getInnerVertices().add(new Vertex(xmax, ymax, 0));

    }


    public void updateWorldVertices() {

        // on redéfinit les coordonées des vertices
        // pour avoir les coordonnées transformées
        this.mWorldVertices = Tools.applyModelView(this.mInnerVertices, this.getGameObject().mModelView);


    }

    /**
     * @param Mvp
     */
    public void draw(ProgramShaderManager PSM, float[] Mvp) {

        ProgramShader sh = PSM.getShaderByName(ProgramShader_forLines.SHADER_FOR_LINES);
        PSM.use(sh);

        sh.enableShaderVar();

        // on charge les coordonnées des vertices

        FloatBuffer fbVertices = ByteBuffer.allocateDirect(4 * 3 * CONST.FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int i = 0; i < this.mWorldVertices.size(); i++) {
            Tools.putXYZIntoFbVertices(fbVertices, i, this.mWorldVertices.get(i));
        }
        sh.setVerticesCoord(fbVertices);

        // on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec
        // une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, Mvp, 0);

        // on récupère les indices du rectangle vide
        // qui indiquent dans quel ordre les vertex doivent être dessinés

        ShortBuffer indices = Rectangle2D.getIndicesForEmptyRec();
        GLES20.glDrawElements(GLES20.GL_LINES, indices.capacity(),
                GLES20.GL_UNSIGNED_SHORT, indices);

    }

}
