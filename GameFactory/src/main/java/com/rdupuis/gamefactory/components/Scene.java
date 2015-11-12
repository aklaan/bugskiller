package com.rdupuis.gamefactory.components;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.DisplayMetrics;

import com.rdupuis.gamefactory.animations.AnimationManager;
import com.rdupuis.gamefactory.providers.ProgramShaderProvider;
import com.rdupuis.gamefactory.providers.TextureProvider;
import com.rdupuis.gamefactory.utils.CollisionControler;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Scene implements GLSurfaceView.Renderer {
    public enum VIEW_MODE {ORTHO, CAMERA}

    public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
    public static final int SHORT_SIZE = 2;
    public final static String TAG_ERROR = "CRITICAL ERROR";

    public int[] vbo;
    public int[] vboi;


    private VIEW_MODE mViewMode;
    public OpenGLActivity mActivity;
    public ProgramShaderProvider mProgramShaderProvider;
    public TextureProvider mTextureProvider;
    private ArrayList<GameObject> mGameObjectList;
    private AnimationManager animationManager;
    public final float[] mVMatrix = new float[16];
    public final float[] mVMatrixORTH = new float[16];

    // Matrice de projection de la vue
    public float[] mProjectionView = new float[16];

    // matrice de projection orthogonale
    public float[] mProjectionORTH = new float[16];

    // public ShaderProvider mShaderProvider;
    private boolean firstFrame;
    public Camera mCamera;

    public Scene(OpenGLActivity activity) {

        mActivity = activity;
        //par defaut on est en mode de vue Orthogonale
        this.mViewMode = VIEW_MODE.ORTHO;
        // le bitmap provider peu servir pour plusieurs scene
        // on le remonte donc au plus haut.
        this.mTextureProvider = new TextureProvider(this.getActivity());
        this.mGameObjectList = new ArrayList<GameObject>();
        this.mProgramShaderProvider = new ProgramShaderProvider(mActivity);
        this.mCamera = new Camera();
        this.mCamera.centerZ = 100;
        this.firstFrame = true;
        this.animationManager = new AnimationManager();
        this.preLoading();

        UserFinger userFinger = new UserFinger();
        this.addToScene(userFinger);

    }

    public VIEW_MODE getViewMode() {
        return mViewMode;
    }

    public void setViewMode(VIEW_MODE mViewMode) {
        this.mViewMode = mViewMode;
    }


    public AnimationManager getAnimationManager() {
        return this.animationManager;
    }

    public OpenGLActivity getActivity() {
        return this.mActivity;
    }

    public TextureProvider getTextureProvider() {
        return this.mTextureProvider;
    }

    public ProgramShaderProvider getProgramShaderProvider() {
        return this.mProgramShaderProvider;
    }

    public float[] getProjectionView() {
        return this.mProjectionView;
    }

    private void preLoading() {
        // on charge les textures necessaires à la scène
        loadTextures();
        // on initialise la liste des objets qui serront contenus dans
        // la scène.

        loadGameObjects();
    }

    @Override
    public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

        // on ne peux pas créer de programe Shader en dehors du contexte
        // opengl. donc le provider est à recréer à chaque load de la scène

        initProgramShader();
        this.getTextureProvider().initialize();

        //on défini la couleur de base pour initialiser le BUFFER
        // a chaque Frame, lorsque l'on fera un appel GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // on va remplir le back buffer avec la couleur pré-définie ici
        GLES20.glClearColor(0.3f, 0.2f, 0.2f, 1.0f);


        // Activattion de la gestion de l'Alpha
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // spécifer l'orientation de la détermination des face avant/arrière
        // par dédaut c'est CCW (counterClockWiwe) l'inverse des aiguilles d'une
        // montre
        GLES20.glFrontFace(GL10.GL_CCW);

        // on indique quelle face à oculter (par défaut c'est BACK)
        //       GLES20.glCullFace(GL10.GL_BACK);

        // Activation du Culling
        // je l'ai désactivé ici car on ne s'en sert pas
        // c'est ça en moins à calculer donc c'est sans doute un gain de perf.
//        GLES20.glEnable(GL10.GL_CULL_FACE);

        // pour l'affichage en mode GL_LINES
        GLES20.glLineWidth(1.f);

        /**
         * test sur les VBO
         */

        //on crée un tableau qui va référencer les buffer vbo
        vbo = new int[1];
        //on demande à OpenGL de créer des buffer et de les référencer dans le tableau
        GLES20.glGenBuffers(1, vbo, 0);

        //on crée un tableau qui va référencer les buffer vboi
        vboi = new int[1];
        //on demande à OpenGL de créer des buffer et de les référencer dans le tableau
        GLES20.glGenBuffers(1, vboi, 0);


        /**
         *
         */

    }

    // @Override
    public void onSurfaceChanged(GL10 gl, int width, int hight) {
        // lorsqu'il y a une modification de la vue , on redéfinie la nouvelle
        // taille de la vue (par exemple quand on incline le téléphone et
        // que l'on passe de la vue portait à la vue paysage


        // le coin en bas à gauche est 0,0
        // la taille de la surface est la même que l'écran

        GLES20.glViewport(0, 0, width, hight);

        float ratio = (float) width / hight;
        // * pour un affichage Perspective *********************
        // le premier plan de clipping NEAR est défini par
        // 2 points : le point du bas à gauche et le point du haut à droite
        // le point du bas à gauche est à -ratio, -1
        // le point du haut à gauche est à ratio, 1
        // le plan de clipping NEAR est à 1 et le second plan est à 1000.

        /** avec cette version le centre est au milieu de l'écran */
        Matrix.frustumM(mProjectionView, 0, -ratio, ratio, -1, 1, 1, 1000);

        /** avec cette version le centre est en bas à gauche de l'écran */
        // Matrix.frustumM(mProjectionView, 0, 0, ratio, 0, 1, 1, 1000);

        // Set the camera position (View matrix)
        // le centre de la cam�ra est en 0,0,-200 (oeuil)
        // la cam�ra regarde le centre de l'�cran 0,0,0
        // le vecteur UP indique l'orientation de la cam�ra (on peu tourner la
        // cam�ra)
        // Matrix.setLookAtM(rm, rmOffset, eyeX, eyeY, eyeZ, centerX, centerY,
        // centerZ, upX, upY, upZ)

        // attentios, si le z est n�gatif, la cam�ra se retrouve derri�re l'axe
        // et donc le X est invers�
        Matrix.setLookAtM(mVMatrix, 0, mCamera.centerX, mCamera.centerY, mCamera.centerZ,
                mCamera.eyeX, mCamera.eyeY, mCamera.eyeZ,
                mCamera.orientX, mCamera.orientY, mCamera.orientZ);

        // * pour un affichage Orthogonal *********************
        // le (0,0) est en bas � gauche.
        Matrix.orthoM(mProjectionORTH, 0, -0, width, 0, hight, -10.f, 10.f);
        Matrix.setIdentityM(mVMatrixORTH, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        /*********************************************************************************
         * Début du cycle de rendu
         ********************************************************************************/

        //on mémorise le moment où on commence le cycle
        float startDrawingTime = SystemClock.currentThreadTimeMillis();

        // on vide le buffer.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        /***************************************************************************************
         * Calcul de la Matrice de VIEW : mVMatrix
         *************************************************************************************/
        if (this.getViewMode() == VIEW_MODE.CAMERA) {
            Matrix.setLookAtM(mVMatrix, 0, mCamera.centerX, mCamera.centerY, mCamera.centerZ,
                    mCamera.eyeX, mCamera.eyeY, mCamera.eyeZ,
                    mCamera.orientX, mCamera.orientY, mCamera.orientZ);
        }

        // Calculate the projection and view transformation
        // Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        /** Initialisation du pointeur de touché */
        this.getGameObjectByTag(UserFinger.USER_FINGER_TAG).mainUpdate(mActivity);

        /**
         * jouer les animations si elles éxistent
         */

        this.animationManager.playAnimations();

        /** on check les colissions entre tous les éléments de la scène
         * =============================================================================
         * lorsque l'on traite la première frame tous les éléments sont en 0,0
         * car on est pas encore passé dans les initialisations de positions
         * du coup tout le monde est en colision.
         * pour éviter le problème, on ne chek pas les colissions sur la première Frame
         */
        if (!firstFrame) {
            CollisionControler.checkAllCollisions(mGameObjectList);
        }


        /************************************************************************
         *Pour chaques GameObject de la scène, on appelle
         * la mise à jour et on le dessine s'il est visible.
         */

        for (GameObject gameObject : this.mGameObjectList) {

            //Mises à jour
            gameObject.mainUpdate(mActivity);

            //Dessiner
            if (gameObject.isVisible) {
//                Log.e("draw", gameObject.getTagName());
                gameObject.draw();
            }
        }

/**
 if (this.animationManager.playInProgress()) {
 this.getActivity().mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

 } else {
 this.getActivity().mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

 }
 */

        /****************************************************************************
         * Pour une Animation fluide, 60 FPS sont sufisants
         * si on va plus vite, on temporise le prochain cycle de rendu
         * pour éviter de surcharger le GPU et le CPU
         */

        //Calcul du temps écoulé pour rendre la Frame
        float drawTimeElaps = SystemClock.currentThreadTimeMillis() - startDrawingTime;

        //Si le temps de rendu est inférieur à 1/60 de seconde
        //on attend le temps restant à faire pour atteindre les 60 FPS
        if (drawTimeElaps < (1000 / 60)) {
            SystemClock.sleep((long) ((1000 / 60) - drawTimeElaps));
        }

        /**
         * Fin du Cycle de rendu
         * on passe firstFrame à faux pour qu'au pochain cycle, on prenne en compte les colisions
         */
        firstFrame = false;
    }

    /**
     * fonction où on charge les objets de la scène dans la phase de Loading
     * game
     */
    public void loadGameObjects() {

    }

    /**
     * Initialisation des program Shader
     */
    public void initProgramShader() {

    }

    /**
     * Chargement des Textures
     */
    public void loadTextures() {

    }

    /**
     * Ajouter un GameObject dans la scène
     *
     * @param gameobject
     */
    public void addToScene(GameObject gameobject) {
        gameobject.setScene(this);
        this.mGameObjectList.add(gameobject);

    }

    /**
     * Retirer un GameObject de la scène
     *
     * @param gameobject
     */
    public void removeFromScene(GameObject gameobject) {
        gameobject.setScene(null);
        this.mGameObjectList.remove(gameobject);

    }

    /**
     * Ajouter une liste de GameObject dans une scène
     *
     * @param GameObjectList
     */
    public void addToScene(ArrayList<GameObject> GameObjectList) {
        for (GameObject go : GameObjectList) {
            go.setScene(this);
        }
        this.mGameObjectList.addAll(GameObjectList);
    }

    /**
     * Récupérer un GameObject de la scène via son TAG
     *
     * @param tagId
     * @return
     */
    public GameObject getGameObjectByTag(String tagId) {
        GameObject result = null;
        for (GameObject gameObject : this.mGameObjectList) {
            // Log.i("info : ", gameObject.getTagName());
            if (gameObject.getTagName() == tagId) {
                result = gameObject;
            }

        }
        return result;
    }

    /**
     * Récupérer l'Input UserFinger
     *
     * @return
     */
    public UserFinger getUserFinger() {
        GameObject result = null;
        for (GameObject gameObject : this.mGameObjectList) {

            if (gameObject.getTagName() == UserFinger.USER_FINGER_TAG) {
                result = gameObject;
            }

        }
        return (UserFinger) result;
    }

    /**
     * Récupérer la Hauteur de la scène
     *
     * @return
     */
    public int getHeight() {
        DisplayMetrics metrics = this.getActivity().getResources()
                .getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * Récupérer la largeur de la scène
     *
     * @return
     */
    public int getWidth() {
        DisplayMetrics metrics = this.getActivity().getResources()
                .getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * pour leq test indexbuffer est toujours à zéro vu que c'est un tableau de 1
     *
     * @param gameObject
     * @param indexBuffer
     */
    public void loadVBO(GameObject gameObject, int indexBuffer) {
        //on se place sur le premier buffer référencé dans le tableau
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.vbo[indexBuffer]);
        //on charge les données
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, gameObject.getFbVertices().capacity() *  FLOAT_SIZE, gameObject.getFbVertices(), GLES20.GL_STATIC_DRAW);

        //unbind
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //A TESTER : on peut normalement effacer fbVertices du gameobject pour libérer la mémoire

    }

    public void loadVBOi(GameObject gameObject, int indexBuffer) {
        //on se place sur le premier buffer référencé dans le tableau
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.vboi[indexBuffer]);
        //on charge les données
        //-------------------------------------------------------------------------------------------------
        //      target = un buffer ELEMENT_ARRAY dans mémoire graphique
        //      size = la taille du buffer = le nombre d'indices à stocker * la taille d'un SHORT
        //      data = les données
        //      usage :   GL_STATIC_DRAW  : les données sont lues une fois et sont réutilisée a chaque frame
        //             ou GL_DYNAMIC_DRAW : les données sont lues a chaque frame
        //-------------------------------------------------------------------------------------------------
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, gameObject.getIndices().capacity() *  SHORT_SIZE,
                gameObject.getIndices(), GLES20.GL_STATIC_DRAW);

        //unbind
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        //A TESTER : on peut normalement effacer fbVertices du gameobject pour libérer la mémoire

    }

    private void updateCamera() {
        /**
         * ici on joue sur les évènements déterminés par la SurfaceView pour
         * contrôler la position de la caméra
         */
        float incX = 0;
        float incY = 0;

        switch (this.getActivity().getSurfaceView().getScreenEvent()) {

            case SCROLL_H_RIGHT:
                incX = +2f;
                break;

            case SCROLL_H_LEFT:
                incX = -2f;
                break;


            case SCROLL_V_UP:
                incY = +2f;
                break;

            case SCROLL_V_DOWN:
                incY = -2f;
                break;

            default:
                break;

        }

        float limitX_RIGHT = +80;
        float limitX_LEFT = -80;

        this.mCamera.centerX += incX;
        this.mCamera.centerX = (this.mCamera.centerX > limitX_RIGHT) ? limitX_RIGHT : this.mCamera.centerX;
        this.mCamera.centerX = (this.mCamera.centerX < limitX_LEFT) ? limitX_LEFT : this.mCamera.centerX;
        this.mCamera.eyeX = this.mCamera.centerX;

        float limitY_UP = +100;
        float limitY_DOWN = -100;

        this.mCamera.centerY = (this.mCamera.centerY > limitY_UP) ? limitY_UP : this.mCamera.centerY;
        this.mCamera.centerY = (this.mCamera.centerY < limitY_DOWN) ? limitY_DOWN : this.mCamera.centerY;


        this.mCamera.centerY += incY;
        this.mCamera.eyeY = this.mCamera.centerY;


    }
}
