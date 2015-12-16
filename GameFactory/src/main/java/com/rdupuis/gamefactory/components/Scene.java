package com.rdupuis.gamefactory.components;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.DisplayMetrics;

import com.rdupuis.gamefactory.animations.AnimationManager;
import com.rdupuis.gamefactory.providers.GameObjectManager;
import com.rdupuis.gamefactory.providers.ProgramShaderManager;
import com.rdupuis.gamefactory.providers.TextureManager;
import com.rdupuis.gamefactory.utils.ColliderManager;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Scene implements GLSurfaceView.Renderer {
    public enum VIEW_MODE {ORTHO, CAMERA}

    public final static String TAG_ERROR = "CRITICAL ERROR";

    private VIEW_MODE mViewMode;
    private OpenGLActivity mActivity;


    private ColliderManager mColliderManager;
    private TextureManager mTextureManager;
    private AnimationManager animationManager;
    private GameObjectManager mGameObjectManager;
    private ProgramShaderManager mProgramShaderManager;

    public final float[] mVMatrix = new float[16];
    public final float[] mVMatrixORTH = new float[16];

    // Matrice de projection de la vue
    public float[] mProjectionView = new float[16];

    // matrice de projection orthogonale
    public float[] mProjectionORTH = new float[16];

    // public ShaderProvider mShaderProvider;
    private boolean firstFrame;
    public Camera mCamera;

    /**
     * @return
     */
    public TextureManager getTexManager() {
        return mTextureManager;
    }

    /**
     * @param mTextureManager
     */
    public void setTexManager(TextureManager mTextureManager) {
        this.mTextureManager = mTextureManager;
    }

    /**
     * @return
     */
    public GameObjectManager getGOManager() {
        return mGameObjectManager;
    }

    /**
     * @param mGameObjectManager
     */
    public void setGOManager(GameObjectManager mGameObjectManager) {
        this.mGameObjectManager = mGameObjectManager;
    }

    /**
     * @return
     */
    public ProgramShaderManager getPSManager() {
        return mProgramShaderManager;
    }

    /**
     * @param mProgramShaderManager
     */
    public void setPSManager(ProgramShaderManager mProgramShaderManager) {
        this.mProgramShaderManager = mProgramShaderManager;
    }

    /**
     * @param animationManager
     */
    public void setAnimationManager(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    /**
     * @return
     */
    public VIEW_MODE getViewMode() {
        return mViewMode;
    }

    /**
     * @param mViewMode
     */
    public void setViewMode(VIEW_MODE mViewMode) {
        this.mViewMode = mViewMode;
    }

    /**
     * @return
     */
    public AnimationManager getAnimationManager() {
        return this.animationManager;
    }

    /**
     * @return
     */
    public OpenGLActivity getActivity() {
        return this.mActivity;
    }

    public ColliderManager getColliderManager() {
        return mColliderManager;
    }

    public void setColliderManager(ColliderManager colliderManager) {
        this.mColliderManager = colliderManager;
    }

    /**
     * Constructeur
     *
     * @param activity
     */
    public Scene(OpenGLActivity activity) {

        this.mActivity = activity;
        //par defaut on est en mode de vue Orthogonale
        this.mViewMode = VIEW_MODE.ORTHO;

        setTexManager(new TextureManager(activity));
        setPSManager(new ProgramShaderManager(this));
        setAnimationManager(new AnimationManager());
        setGOManager(new GameObjectManager(this));
        setColliderManager(new ColliderManager());

        this.mCamera = new Camera();
        this.mCamera.centerZ = 100;
        this.firstFrame = true;

        this.preLoading();

        UserFinger userFinger = new UserFinger();

        // TODO : il faut sortir ce composant de la liste des objets pour
        // TODO : en faire un contrôleur
        this.addToScene(userFinger);

    }


    /**
     * on préchage les éléménts qui ne nécéssitent pas d'avoir un contexte openGl
     * de créé.
     */
    private void preLoading() {
        // on charge les textures necessaires à la scène
        loadTextures();
        // on initialise la liste des objets qui serront contenus dans
        // la scène.
        loadGameObjects();
    }

    /**
     * @param gl2
     * @param eglConfig
     */
    @Override
    public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

        // on ne peux pas créer de programe Shader en dehors du contexte
        // opengl. donc le provider est à recréer à chaque fois que l'on contruit la scène
        // c'est à dire : au démarrage et à chaque fois que l'on incline l'écran

        loadProgramShader();

        //chargement des textures dans le contexte OpenGl
        this.getTexManager().initializeGLContext();

        //chargement des objets le contexte OpenGl
        this.getGOManager().initializeGLContext();

        //on initialise les boites de colision
        this.getColliderManager().initBoxes(this.getGOManager());

        // on défini la couleur de base pour initialiser le BUFFER de rendu
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


        // Activation du Culling
        // nb : je l'ai désactivé ici car on ne s'en sert pas
        //      c'est ça en moins à calculer donc c'est un gain de perf.
        //  GLES20.glEnable(GL10.GL_CULL_FACE);

        // on indique quelle face à oculter (par défaut c'est BACK)
        // GLES20.glCullFace(GL10.GL_BACK);

        // Taille des lignes pour l'affichage en mode GL_LINES
        GLES20.glLineWidth(1.f);


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

    public float[] getProjectionView() {
        if (this.getViewMode() == Scene.VIEW_MODE.ORTHO) {
            return this.mProjectionORTH;
        }
        return this.mProjectionView;

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        /*********************************************************************************
         * Début du cycle de rendu
         ********************************************************************************/
        //on mémorise le moment où on commence le cycle
        float startDrawingTime = SystemClock.currentThreadTimeMillis();

        // on vide le buffer de rendu en remplacant tout le contenu par la couleur
        // qui a été prédéfinie.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        /***************************************************************************************
         * Mise à jour de la Matrice de projetction en fonction de la position de la caméra
         *************************************************************************************/
        if (this.getViewMode() == VIEW_MODE.CAMERA) {
            Matrix.setLookAtM(mVMatrix, 0, mCamera.centerX, mCamera.centerY, mCamera.centerZ,
                    mCamera.eyeX, mCamera.eyeY, mCamera.eyeZ,
                    mCamera.orientX, mCamera.orientY, mCamera.orientZ);
        }

        /** Mise à jour des objets*/
        this.getGOManager().update();

        /** jouer les animations si elles éxistent */
        this.animationManager.playAnimations();

        /** on check les colissions entre tous les éléments de la scène
         * =============================================================================
         * lorsque l'on traite la première frame tous les éléments sont en 0,0
         * car on est pas encore passé dans les initialisations de positions
         * du coup tout le monde est en colision.
         * pour éviter le problème, on ne chek pas les colissions sur la première Frame
         */
        //if (!firstFrame) {
            this.getColliderManager().updateCollisionsList();
        //}

        /**
         * Rendu des objets à dessiner*/
        this.getPSManager().render(this.getGOManager().GOList());

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
    //TODO : rendre des fonction obligatoire en override en passant pas une classe abstraite
    public void loadGameObjects() {
    }

    /**
     * Initialisation des program Shader
     */
    public void loadProgramShader() {
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
        this.getGOManager().add(gameobject);

    }

    /**
     * Retirer un GameObject de la scène
     *
     * @param gameobject
     */
    public void removeFromScene(GameObject gameobject) {
        gameobject.setScene(null);
        this.getGOManager().remove(gameobject);

    }



    /**
     * Récupérer l'Input UserFinger
     *
     * @return
     */
    public UserFinger getUserFinger() {
        GameObject result = null;
        for (GameObject gameObject : this.getGOManager().GOList()) {

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
