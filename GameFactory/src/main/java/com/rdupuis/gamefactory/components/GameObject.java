package com.rdupuis.gamefactory.components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.rdupuis.gamefactory.animations.Animation;
import com.rdupuis.gamefactory.interfaces.Drawable;
import com.rdupuis.gamefactory.shaders.ProgramShader;
import com.rdupuis.gamefactory.shaders.ProgramShader_forLines;
import com.rdupuis.gamefactory.shaders.ProgramShader_simple;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

public class GameObject implements Drawable, Cloneable {

    private String mTagName;
    private Texture mTexture;

    public Texture getTexture() {
        return mTexture;
    }

    public void setTexture(Texture mTexture) {
        this.mTexture = mTexture;
    }

    public Boolean textureEnabled;
    public Boolean isVisible;

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }

    private Scene mScene;
    //    public String viewMode = "ORTHO";
    private float alpha;

    // top permettant de savoir si l'objet est statique ou qu'il
    // a la possibilité d'être en mouvement. ceci va servir
    // pour le calcul des collisions
    public Boolean isStatic = true;
    public Boolean canCollide = false;

    // coordonnées du centre de l'objet
    public float X = 0;
    public float Y = 0;
    public float Z = 0;

    public boolean rotation = false;

    public float rotationAxisX = 0.f;
    public float rotationAxisY = 0.f;
    public float rotationAngl = 0.f; // en radian

    public CollisionBox mCollisionBox;
    // tableau des objets avec lesquel le gameobject rentre en collision
    public ArrayList<GameObject> mCollideWithList;

    public ArrayList<GameObject> mGameObjectToListenList;
    //public Animation mAnimation;

    public float[] mRotationMatrix = new float[16];
    public float[] mModelView = new float[16];
    public float[] mMvp = new float[16];


    public int drawMode = GLES20.GL_TRIANGLES;

    public float angleRADX = 0.0f;
    public float angleRADY = 0.0f;
    public float angleRADZ = 0.0f;

    // public static final int FLOAT_SIZE = 4;
    public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;

    // on indique qu'il faut 4 byte pour repésenter un float
    // 00000000 00000000 00000000 00000000

    // un byte n'est pas obligatoirement égal à 8 bit
    // cela d�pend du mat�riel. en g�n�ral il est tr�s souvant egal �
    // 8 bit ce qui fait qu'un byte est tr�s souvent �gal � un Octet
    // mais comme ce n'est pas toujours le cas, on parle en byte et non en octet
    // pour �tre plus pr�cis.

    public static final int SHORT_SIZE = 2;
    // ici on indique qu'un short est cod� sur 2 byte
    // soit g�n�ralement 2 octets
    // soit : 00000000 00000000

    // ! Vertices
    public FloatBuffer mFbVertices; // d�finition d'un tableau de flotants

    public ArrayList<Vertex> mVertices; // d�finition d'un tableau de vertex

    // ! indices
    private ShortBuffer mIndices;

    // ! coordon�es de texture
    private FloatBuffer mTextCoord;

    // private ByteBuffer mTexture;
    public int mTextureWidth;
    public int mTexturehight;

    // constructeur
    public GameObject() {

        //Valeurs par défaut :
        //l'Alpha est à 100%
        this.setAlpha(1);

        //pas de texture
        textureEnabled = false;

        //pas de tagname
        mTagName = "";

        //visible
        isVisible = true;

        //la matrice de rotation est nulle
        Matrix.setIdentityM(this.mRotationMatrix, 0);

        this.mCollideWithList = new ArrayList<GameObject>();
        this.mGameObjectToListenList = new ArrayList<GameObject>();
        this.mVertices = new ArrayList<Vertex>();


    }

    /**
     * @return
     */
    public ArrayList<GameObject> getGameObjectToListenList() {
        return this.mGameObjectToListenList;
    }

    /**
     * @param nbIndex
     */
    public void initBuffers(int nbIndex) {
        int nbVertex = mVertices.size();

        mFbVertices = ByteBuffer.allocateDirect(nbVertex * 3 * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE)
                .order(ByteOrder.nativeOrder()).asShortBuffer();

        mTextCoord = ByteBuffer.allocateDirect(nbVertex * 2 * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

    }

    public void enableColision() {
        this.mCollisionBox = new CollisionBox(this);
        this.canCollide = true;
    }

    public void disableColision() {
        this.mCollisionBox = null;
        this.canCollide = false;
        this.mCollideWithList.clear();

    }

    /**
     * @param index
     * @param vertex
     */
    public void putXYZIntoFbVertices(int index, Vertex vertex) {
        // la position physique en mémoire des bytes qui représentent le vertex
        // c'est la taille d'un vertex en bytes x l'index
        mFbVertices.rewind();
        // ici on se positionne dans le buffer à l'endroit ou l'on va ecrire le
        // prochain vertex
        mFbVertices.position(Vertex.Vertex_COORD_SIZE * index);
        mFbVertices.put(vertex.x).put(vertex.y).put(vertex.z);
        // on se repositionne en 0 , prêt pour la relecture
        mFbVertices.rewind();

    }


    /**
     * insérer des bytes "u,v" dans le buffer des coordonnées de texture
     *
     * @param index
     * @param vertex
     */
    public void putUVIntoFbTextCoord(int index, Vertex vertex) {
        //on se place au debut du buffer
        mTextCoord.rewind();
        //on avance dans le buffer à l'endroit où on souhaite écrire
        mTextCoord.position(Vertex.Vertex_TEXT_SIZE * index);
        //on écrit les coordonées de texture
        mTextCoord.put(vertex.u).put(vertex.v);
        // on se repositionne en 0 , prêt pour la lecture
        mTextCoord.rewind();
    }


    public void rotate(float x, float y, float anglRAD) {

        X = X + (float) (Math.cos(anglRAD));
        Y = Y + (float) (Math.sin(anglRAD));
        // Matrix.translateM(wrkresult, 0, x, y, 0);
        Log.i("debug", String.valueOf(X) + " / " + String.valueOf(Y));

        Log.i("debug", String.valueOf(Math.cos(anglRAD)));

    }

    // setter indices
    public void putIndice(int index, int indice) {
        // on se positionne a l'index dans le buffer
        // comme on a qu'un seul short a placer on ne fait pas comme dans
        // putvertice
        mIndices.position(index);
        // on ecrit le short
        mIndices.put((short) indice);
        // on se repositionne en z�ro
        mIndices.position(0);
    }

    public Scene getScene() {
        return this.mScene;
    }


    public FloatBuffer getFbVertices() {

        for (int i = 0; i < this.mVertices.size(); i++) {
            this.putXYZIntoFbVertices(i, this.mVertices.get(i));
        }

        return mFbVertices;
    }


    /**
     * @return
     */
    public FloatBuffer getFbTextCood() {

        for (int i = 0; i < this.mVertices.size(); i++) {
            this.putUVIntoFbTextCoord(i, this.mVertices.get(i));
        }

        return mTextCoord;
    }


    // getter TextCoord
    public FloatBuffer getTextCoord() {
      return mTextCoord;
    }

    // getter indices
    public ShortBuffer getIndices() {
        return mIndices;
    }


    public void onUpdate() {

    }

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(int tagid) {
        mTagName = String.valueOf(tagid);
    }

    public void setTagName(String tagid) {
        mTagName = tagid;
    }

    /**
     * public float getWidth() { return width; }
     * <p/>
     * public void setWidth(float width) { // this.width = width; //
     * updateModelMatrix(); }
     * <p/>
     * public float getHeight() { return hight; }
     * <p/>
     * public void setHeight(float hight) { this.hight = hight;
     * updateModelMatrix(); }
     */
    public void setCoord(float x, float y) {
        this.X = x;
        this.Y = y;

    }

    public void setCoord(float x, float y, float z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public float getCoordX() {
        return X;

    }

    public float getCoordY() {
        return Y;

    }

    // dessiner l'objet
    public void draw() {
        // j'utilise le shader prévu
        ProgramShader sh = this.getScene()
                .getProgramShaderProvider().getShaderByName("simple");

        this.getScene().getProgramShaderProvider().use(sh);

        getFbTextCood();
        if (this.textureEnabled) {
            // on demande à opengl d'utiliser la texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture().getGlBufferId());
            sh.setTextureCoord(getTextCoord());
            //GLES20.glEnableVertexAttribArray(sh.attrib_texture_coord_location);

        }


        // on charge les coordon�es de texture


        //--------------------------------------------
        sh.enableShaderVar();


        if (this.getScene().getViewMode() == Scene.VIEW_MODE.ORTHO) {
            Matrix.multiplyMM(this.mMvp, 0, this.getScene().mProjectionORTH, 0,
                    this.mModelView, 0);

        } else
            Matrix.multiplyMM(this.mMvp, 0, this.getScene().getProjectionView(), 0,
                    this.mModelView, 0);

        // On alimente la donn�e UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec
        // une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, this.mMvp, 0);

        GLES20.glUniform1f(sh.uniform_alpha_location, this.getAlpha());

        //je me place sur le vbo
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getScene().vbo[0]);

        //on active l'utilisation de la variable aPostion dans le shader
        GLES20.glEnableVertexAttribArray(sh.attrib_vertex_coord_location);

        //plutôt qur de passer des valeur au shader, on passe un pointeur vers le buffer
        GLES20.glVertexAttribPointer(sh.attrib_vertex_coord_location, 3,
                GLES20.GL_FLOAT, false, 0, 0);


        //je me place sur le buffer des index
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.getScene().vboi[0]);

        //je dessine les vertex selon l'ordre indiqué dans l'index
        GLES20.glDrawElements(this.drawMode, this.getIndices().capacity(),
                GLES20.GL_UNSIGNED_SHORT, 0);

        //je pointe les buffer sur "rien" pour ne pas les réutiliser par erreur
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        // renderer.mProgramme1.disableVertexAttribArray();
        // �quivalent du POP
        // renderer.mModelView = this.mBackupModelView;
        // renderer.mProgramme1.disableVertexAttribArray();

        if (this.canCollide) {
            this.mCollisionBox.update();
            if (mCollisionBox.isVisible) {
                this.mCollisionBox.draw();
            }
        }

    }

    /**
     * @param modelView
     * @return
     */
    public ArrayList<Vertex> applyModelView(float[] modelView) {

        // on r�cup�re les vertices de l'objet
        // et on calcule leur coordon�es dans le monde
        float[] oldVerticesCoord = new float[4];
        float[] newVerticesCoord = new float[4];

        ArrayList<Vertex> mModelViewVertices; // d�finition d'un tableau de
        // flotants
        mModelViewVertices = new ArrayList<Vertex>();

        // je suis oblig� de passer par un vecteur 4 pour la multiplication

        for (int i = 0; i < this.mVertices.size(); i++) {
            oldVerticesCoord[0] = this.mVertices.get(i).x; // x
            oldVerticesCoord[1] = this.mVertices.get(i).y; // y
            oldVerticesCoord[2] = this.mVertices.get(i).z; // z
            oldVerticesCoord[3] = 1.f;

            Matrix.multiplyMV(newVerticesCoord, 0, modelView, 0,
                    oldVerticesCoord, 0);
            mModelViewVertices.add(new Vertex(newVerticesCoord[0],
                    newVerticesCoord[1], newVerticesCoord[2]));

        }

        return mModelViewVertices;

    }

    // fabrique la nouvelle ModelView en fonction des modifications
    // apportées.
    public void updateModelView() {
        float[] wrkModelView = new float[16];
        float[] wrkRotationMatrix = new float[16];
        //
        // pas bien...on doit avoit une conftion directiement dans cene pour récupérér
        //
        if (this.getScene().getViewMode() == Scene.VIEW_MODE.ORTHO) {
            wrkModelView = this.getScene().mVMatrixORTH.clone();

        } else
            wrkModelView = this.getScene().mVMatrix.clone();

        Matrix.translateM(wrkModelView, 0, this.X, this.Y, this.Z);

        Matrix.setRotateEulerM(wrkRotationMatrix, 0, this.angleRADX,
                this.angleRADY, this.angleRADZ);

        Matrix.multiplyMM(this.mModelView, 0, wrkModelView, 0,
                wrkRotationMatrix, 0);

    }

    /**
     * Fonction de mise à jour générale
     *
     * @param openGLActivity
     */

    public void mainUpdate(OpenGLActivity openGLActivity) {

        // traiter les op�rations diverses � effectuer lors de
        // la mise � jour
        this.onUpdate();

        // traiter la lecture de l'animation si elle existe
        //this.applyAnimation();

        // traiter les actions a faire en cas de colissions
        this.applyCollisions();


        // A la fin des mises � jour on connais les nouvelles coordon�es
        // on peut calculer la nouvelle matrice modelView
        this.updateModelView();

        // -----------------------------------------
        // Mettre a jour la boite de colision si elle existe
        // ------------------------------------------
        if (this.canCollide) {
            this.mCollisionBox.update();
        }

        // -----------------------------------------------------
        // Traiter les �v�nements �cout�s sur les autres objets
        // -----------------------------------------------------
        updateListerners();

        // -----------------------------------------------------
        // Gestion des modifications de la texture
        // ------------------------------------------------------

        /**
         * if (this.textureEnabled && this.mTexture.textureNameID !=
         * newTextureId) {
         * this.getScene().getBitmapProvider().linkTexture(newTextureId, this);
         *
         * this.mTexture.textureNameID = newTextureId; }
         */
        /*
        if (textureEnabled) {
            this.getScene().getTextureProvider()
                    .putTextureToGLUnit(this.mTexture, 0);
        }*/

    }

    /***************************************************
     * Traiter des colisions avec les autres objets
     ***************************************************/
    public void applyCollisions() {

        if (!this.mCollideWithList.isEmpty()) {
            for (GameObject go : this.mCollideWithList) {

                onCollideWith(go);// newTextureId = R.string.textureRobot;
            }
        }

    }


    /********************************************************
     * On �coute les objets note : on d�pend de l'ordre dans lequel sont trait�
     * les objets
     *******************************************************/
    public void updateListerners() {

        if (!this.getGameObjectToListenList().isEmpty()) {
            for (GameObject go : this.getGameObjectToListenList()) {

                onListenGameObject(go);

            }
        }

    }

    /**************************************************************************
     * Actions a effectuer en cas de colission avec un autre Objet
     *
     * @param gameObject
     *************************************************************************/
    public void onCollideWith(GameObject gameObject) {

    }

    /**************************************************************************
     * Actions effectuer lorsque l'on �coute les objets
     *
     * @param go
     *************************************************************************/
    public void onListenGameObject(GameObject go) {

    }

    /**************************************************************************
     * Actions effectuer lorsque l'animation joue
     *************************************************************************/
    public void onAnimationPlay() {

    }

    /**************************************************************************
     * Actions effectuer lorsque l'animation s'arr�te
     *************************************************************************/
    public void onAnimationStop() {

    }

    public boolean isCollideWith(GameObject gameobject) {

        return this.mCollideWithList.contains(gameobject);

    }

    public GameObject clone() throws CloneNotSupportedException {
        GameObject gameobject = (GameObject) super.clone();

        gameobject.mCollideWithList = new ArrayList<GameObject>();
        gameobject.mGameObjectToListenList = new ArrayList<GameObject>();

        gameobject.mVertices = new ArrayList<Vertex>();

        for (Vertex v : this.mVertices) {
            Vertex vv = v.clone();

            gameobject.mVertices.add(vv);

        }

        // on r�initialise le lien de parent� avec l'animation
        /**
         if (gameobject.getAnimation() != null) {
         Animation anim = (Animation) gameobject.getAnimation().clone();

         anim.setParent(gameobject);

         gameobject.setAnimation(anim);
         }

         // si l'objet source peu entrer en collision on
         // red�fini un nouvelle boite de colision pour la cible
         // sinon elle va avoir la m�me que la source
         if (gameobject.canCollide) {
         gameobject.enableColision();

         }
         */
        return gameobject;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        // on s'assure que l'alpha est toujours compris enre 0 et 1;
        this.alpha = (alpha < 0) ? 0 : alpha;
        this.alpha = (alpha > 1) ? 1 : alpha;
    }


}
