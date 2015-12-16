package com.rdupuis.gamefactory.providers;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.Scene;
import com.rdupuis.gamefactory.components.Vertex;
import com.rdupuis.gamefactory.shaders.ProgramShader;
import com.rdupuis.gamefactory.utils.CONST;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by rodol on 24/11/2015.
 */
public class GameObjectManager {

    private int idSequenceNumber;
    private Scene mScene;
    private ArrayList<GameObject> mGameObjectList;
    public int[] vbo;
    public int[] vboi;

    /********************************************
     * GETTER & SETTER
     ********************************************/
    public Scene getScene() {
        return mScene;
    }

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }

    public GameObjectManager(Scene scene) {

        //mémoriser le lien ave la scène
        this.setScene(scene);

        //initialisation d'une liste d'objets vide
        this.mGameObjectList = new ArrayList<GameObject>();
    }

    public ArrayList<GameObject> GOList() {
        return mGameObjectList;
    }

    public void add(GameObject gameObject) {
        this.mGameObjectList.add(gameObject);
    }

    public void remove(GameObject gameObject) {
        this.mGameObjectList.remove(gameObject);
    }


    /**
     * on fabrique un buffer contenant les coordonées de vertex et les coordonées de texture
     * {x,y,z,u,v,x,y,z,u,v.......}
     * Cette technique ne doit pas être utlisé si on fait évoluer les coordonées de texture
     * d'un Gameobject. l'intérêt de passer par un strideBuffer c'est de placer les infos dans
     * la mémoire graphique et de ne plus y toucher pour faire l'économie d'écriture entre la mémoire
     * client et la mémoire graphique
     * si on est obligé de mettre à jour la mémoire graphique à chaques frame, ça ne vaut pas le coup
     *
     * @param gameObject
     */
    public void loadVBO(GameObject gameObject) {

        //on crée un buffer de travail pour récupérer les informations à
        // transmettre dans la mémoire graphique

        // la taille de ce buffer est nombre de vertex * taille d'un stride * taille d'un float
        // sachant qu'un stride c'est la taille des coordonnées de vertex xyz
        // + la taille des coordonées uv de texture
        // + la taille de la couleur rgba du veertex
        FloatBuffer tempBuffer = ByteBuffer.allocateDirect(gameObject.getNbvertex() * Vertex.stride * CONST.FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        int index = 0;
        for (Vertex vertex : gameObject.getVertices()) {

            tempBuffer.rewind();
            tempBuffer.position((Vertex.stride) * index);
            tempBuffer.put(vertex.x).put(vertex.y).put(vertex.z)
                    .put(vertex.u).put(vertex.v)
                    .put(vertex.r).put(vertex.g).put(vertex.b).put(vertex.a);
            tempBuffer.rewind();
            index++;

        }

        //on se place sur le glbuffer assigné à l'objet
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, gameObject.getGlVBoId());

        //on charge les données
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, tempBuffer.capacity() * CONST.FLOAT_SIZE,
                tempBuffer, GLES20.GL_STATIC_DRAW);

        //unbind
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //on vide le buffer temporaire pour libérer la mémoire cliente
        tempBuffer.clear();
    }


    public void loadVBOi(GameObject gameObject) {
        //On se place sur le GlBuffer assigné
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, gameObject.getGlVBiId());

        //On charge les données dans la mémoire graphique
        //-------------------------------------------------------------------------------------------------
        //      target = un buffer ELEMENT_ARRAY dans mémoire graphique
        //      size = la taille du buffer = le nombre d'indices à stocker * la taille d'un SHORT
        //      data = les données
        //      usage :   GL_STATIC_DRAW  : les données sont lues une fois et sont réutilisée a chaque frame
        //             ou GL_DYNAMIC_DRAW : les données sont lues a chaque frame
        //-------------------------------------------------------------------------------------------------
        ShortBuffer wrkIndiceBuffer = gameObject.getIndices();
        wrkIndiceBuffer.rewind();
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, wrkIndiceBuffer.capacity() * CONST.SHORT_SIZE,
                wrkIndiceBuffer, GLES20.GL_STATIC_DRAW);

        //unbind
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    /**
     * Récupérer un GameObject de la scène via son TAG
     * TODO : ici on récupère seulement le premier qui vient
     * TODO : on peut très bien avoir plusieurs objets avec le même TAG
     *
     * @param tagName
     * @return
     */
    public GameObject getGameObjectByTag(String tagName) {
        GameObject result = null;
        for (GameObject gameObject : this.mGameObjectList) {
            // Log.i("info : ", gameObject.getTagName());
            if (gameObject.getTagName() == tagName) {
                result = gameObject;
            }

        }
        return result;
    }

    /**
     * initialisation du contexte OpenGL : on va charger les objets dans la mémoire graphique
     */
    public void initializeGLContext() {

        //on récupère le nombre d'objets
        int nbObjects = GOList().size();

        //on crée un tableau qui va référencer les vertex buffer
        vbo = new int[nbObjects];

        //on demande à OpenGL de créer les buffer et de les référencer dans le tableau vbo
        GLES20.glGenBuffers(nbObjects, vbo, 0);

        //on crée un tableau qui va référencer les index buffer
        vboi = new int[nbObjects];

        //on demande à OpenGL de créer des buffer et de les référencer dans le tableau vboi
        GLES20.glGenBuffers(nbObjects, vboi, 0);


        int indx = 0;

        //pour chaque gameObject, on lui assigne un Glbuffer
        //pour la partie vertex et la partie index
        //ensuite on charge les buffers
        for (GameObject gameObject : this.GOList()) {
            gameObject.setGlVBoId(vbo[indx]);
            this.loadVBO(gameObject);

            gameObject.setGlVBiId(vboi[indx]);
            this.loadVBOi(gameObject);
            indx++;
        }


    }

    /**
     *
     */
public void update(){
    for (GameObject gameObject:this.GOList()){
        gameObject.update();
        updateModelView(gameObject);
    }
}


    /**
     * Mise à jour de la ModelView pour prendre en compte les
     * modification apportées à l'ojet
     * taille - position - rotation
     * <p/>
     * /!\ l'ordre où on applique les transformation et hyper important
     * il faut toujours faire : translation*rotation*scale
     */

    public void updateModelView(GameObject gameObject) {
        float[] wrkRotationMatrix = new float[16];
        float[] modelView = new float[16];

        //on initialise une matrice identitaire
        Matrix.setIdentityM(modelView, 0);

        //on fabrique une matrice de déplacement vers les coordonnées x,y,z
        Matrix.translateM(modelView, 0, gameObject.X, gameObject.Y, gameObject.Z);

        //on fabrique une matrice de rotation
        Matrix.setRotateEulerM(wrkRotationMatrix, 0, gameObject.angleRADX,
                gameObject.angleRADY, gameObject.angleRADZ);

        //Calcul de la matrice ModelView
        Matrix.multiplyMM(gameObject.mModelView, 0, modelView, 0,
                wrkRotationMatrix, 0);

        //Scales matrix m in place by sx, sy, and sz.
        Matrix.scaleM(gameObject.mModelView, 0, gameObject.getWidth(), gameObject.getHeight(), 0.f);

    }



}

