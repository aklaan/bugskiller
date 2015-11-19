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

    //Tag de l'objet
    private String mTagName;

    //Actuelle texture utilisé pour rendre l'objet
    private Texture mTexture;

    //Top pour activer/désactiver le rendu des textures
    public Boolean textureEnabled;

    //top pour activer/désactiver le rendu de l'objet
    public Boolean isVisible;

    //scène auquel appartient l'objet
    private Scene mScene;

    //Alpha général de l'objet
    private float alpha;

    // top permettant de savoir si l'objet est statique à l'écran ou s'il
    // a la possibilité d'être en mouvement. ceci va servir
    // pour le calcul des collisions
    public Boolean isStatic = true;

    //Top pour activer/désactiver la gestion des colissions
    public Boolean canCollide = false;

    // coordonnées du centre de l'objet
    public float X = 0;
    public float Y = 0;
    public float Z = 0;

    //Taille de l'objet
    private float width;

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    private float height;
    //boite de colission de l'objet
    public CollisionBox mCollisionBox;

    // tableau des objets avec lesquel le gameobject rentre en collision
    public ArrayList<GameObject> mCollideWithList;

    //liste des objets à écouter
    public ArrayList<GameObject> mGameObjectToListenList;

    public float[] mRotationMatrixZZZ = new float[16];
    public float[] mModelView = new float[16];

    public int drawMode = GLES20.GL_TRIANGLES;

    public float angleRADX = 0.0f;
    public float angleRADY = 0.0f;
    public float angleRADZ = 0.0f;

    // public static final int FLOAT_SIZE = 4;
    // on indique qu'il faut 4 byte pour repésenter un float
    // 00000000 00000000 00000000 00000000

    //plutôt que de l'écrire en dur 4, on le calcul.
    //comme ça si jamais le système n'utilise pas 4 bytes on
    //reste bon.
    public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;

    // un byte n'est pas obligatoirement égal à 8 bit
    // cela dépend du matériel. en général il est très souvant egal à
    // 8 bit ce qui fait qu'un byte est trés souvent égal à un Octet
    // mais comme ce n'est pas toujours le cas, on choisi volontairement
    // de parler en byte et non en octet pour être plus précis.

    //    public static final int SHORT_SIZE = 2;
    public static final int SHORT_SIZE = Short.SIZE / Byte.SIZE;

    // ici on indique qu'un short est codé sur 2 byte
    // soit généralement 2 octets
    // soit : 00000000 00000000

    //liste des vertex composant l'objet
    public ArrayList<Vertex> mVertices;


    // TODO: ce buffer n'est pas censé rester en mémoire client !!!
    // définition d'un buffer conteant les coordonées des vetices
    public FloatBuffer mStrideBuffer;

    // ! Vertices
    // définition d'un buffer conteant les coordonées des vertices
    // TODO: ce buffer n'est pas censé rester en mémoire client
    public FloatBuffer mFbVertices;

    // ! indices
    //Définition d'un buffer contenant les indices représentant l'ordre dans lequel
    // doivent être dessinés chaque vertex
    // TODO: ce buffer n'est pas censé rester en mémoire client
    private ShortBuffer mIndices;

    // ! coordonées de texture
    // définition d'un buffer avec les coordonées de textures.
    // TODO: ce buffer n'est pas censé rester en mémoire client
    private FloatBuffer mTextCoord;


    /**
     * Constructeur
     */
    public GameObject() {

        //Initalisation des Valeurs par défaut :
        //l'Alpha est à 100%
        this.setAlpha(1);

        //taille par défaut
        this.width = 1;
        this.height = 1;
        //pas de rendu des texture par défaut
        textureEnabled = false;

        //pas de tagname
        mTagName = "";

        //visible par défaut
        isVisible = true;

        //la matrice de rotation est nulle
        //Matrix.setIdentityM(this.mRotationMatrix, 0);

        this.mCollideWithList = new ArrayList<GameObject>();
        this.mGameObjectToListenList = new ArrayList<GameObject>();
        this.mVertices = new ArrayList<Vertex>();


    }


    public Texture getTexture() {
        return mTexture;
    }

    public void setTexture(Texture mTexture) {
        this.mTexture = mTexture;
    }

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }


    /**
     * @return
     */


    public ArrayList<GameObject> getGameObjectToListenList() {
        return this.mGameObjectToListenList;
    }

    /**
     * on crée les buffers dans la mémoire cliente
     *
     * @param nbIndex
     */
    public void initBuffers(int nbIndex) {
        int nbVertex = mVertices.size();

        //on alloue un espace mémoire pour stocker les coordonnées des vertex
        //la taille de cet espace est
        //   le nombre de vertex
        // x 3 car on a 3 coordonnées XYZ par vertex
        // x la taille d'un Float puisque chaque coordonnée est exprimée en float.
        mFbVertices = ByteBuffer.allocateDirect(nbVertex * 3 * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        //on alloue un espace mémoire pour stocker les indices servant à dessiner les ertex
        //la taille de cet espace est
        // le nombre d'indices
        // x la taille d'un short car les indices sont exprimés en short
        mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE)
                .order(ByteOrder.nativeOrder()).asShortBuffer();

        //on alloue un espace mémoire pour stocker les coordonées de texture
        //la taille de cet espace est
        // le nombre de vertex
        // x 2 car on a 2 coordonnées UV
        // x la taille d'un float car les coordonées sont exprimés en float

        mTextCoord = ByteBuffer.allocateDirect(nbVertex * 2 * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        //le stride buffer est le la taille du buffer des vertices + buffer des coordonnées de texture
        mStrideBuffer = ByteBuffer.allocateDirect(nbVertex * 5 * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /**
     *
     */
    public void enableColision() {
        this.mCollisionBox = new CollisionBox(this);
        this.canCollide = true;
    }

    /**
     *
     */
    public void disableColision() {
        this.mCollisionBox = null;
        this.canCollide = false;
        this.mCollideWithList.clear();

    }

    /**
     * @param index
     */
    public void putIntoStrideBuffer(int index, Vertex vertex) {
        mStrideBuffer.rewind();
        mStrideBuffer.position((Vertex.Vertex_COORD_SIZE + Vertex.Vertex_TEXT_SIZE) * index);
        mStrideBuffer.put(vertex.x).put(vertex.y).put(vertex.z).put(vertex.u).put(vertex.v);
        mStrideBuffer.rewind();
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

    /**
     * @param x
     * @param y
     * @param anglRAD
     */
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

    /**
     * @return
     */
    public Scene getScene() {
        return this.mScene;
    }

    /**
     * @return
     */
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

    /**
     * @return
     */
    public FloatBuffer getStrideBuffer() {

        for (int i = 0; i < this.mVertices.size(); i++) {
            this.putIntoStrideBuffer(i, this.mVertices.get(i));
        }

        return mStrideBuffer;
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

    /**
     * @param tagid
     */
    public void setTagName(int tagid) {
        mTagName = String.valueOf(tagid);
    }

    /**
     * @param tagid
     */
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

    /**
     * @param x
     * @param y
     * @param z
     */
    public void setCoord(float x, float y, float z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    /**
     * @return
     */
    public float getCoordX() {
        return X;

    }

    /**
     * @return
     */
    public float getCoordY() {
        return Y;

    }

    /**
     * Dessiner l'objet
     */
    public void draw() {
        // j'utilise le shader prévu
        ProgramShader sh = this.getScene().getProgramShaderProvider().getShaderByName("simple");
        this.getScene().getProgramShaderProvider().use(sh);


        if (this.textureEnabled) {

            //on active l'unité de traitement des textures 0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

            // on demande à opengl d'utiliser la texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture().getGlBufferId());
            //on fait pointer  uniform_texture_location sur le buffer où est la texture
            GLES20.glUniform1i(sh.uniform_texture_location, 0);

            //initialisation du buffer des coodonnées de texture
            getFbTextCood();
            //Chargement des coodonées de texture
            sh.setTextureCoord(getTextCoord());


        }

        //--------------------------------------------
        sh.enableShaderVar();


        //Calcul de la matrice model-view-projection
        float[] mMvp = new float[16];


        if (this.getScene().getViewMode() == Scene.VIEW_MODE.ORTHO) {
            Matrix.multiplyMM(mMvp, 0, this.getScene().mProjectionORTH, 0,
                    this.mModelView, 0);

        } else
            Matrix.multiplyMM(mMvp, 0, this.getScene().getProjectionView(), 0,
                    this.mModelView, 0);

        // On alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, mMvp, 0);

        //on alimente la donnée Alpha
        GLES20.glUniform1f(sh.uniform_alpha_location, this.getAlpha());

        //je me place sur le vbo
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getScene().vbo[0]);

        //on active l'utilisation de la variable aPostion dans le shader
        GLES20.glEnableVertexAttribArray(sh.attrib_vertex_coord_location);

        //plutôt que de passer des valeur au shader, on indique une position en int.
        // OpenGl comprend alors qu'il faut passer par le pointeur sur lequel on viens de
        // se positionner pour aller chercher les valeurs contenue dans la mémoire graphique
        // et il doit lire le buffer à partir de la position zéro
        GLES20.glVertexAttribPointer(sh.attrib_vertex_coord_location, 3,
                GLES20.GL_FLOAT, false, 0, 0);


        //je me place sur le buffer des index
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.getScene().vboi[0]);

        //je dessine les vertex selon l'ordre indiqué dans l'index.
        // au lieu de fournir des valeurs on indique une position en int ce qui permet à opengl de comprendre
        // qu'il faut passer par le pointeur sur lequel on viens de se postionner
        // et il doit lire le buffer à partir de la position zéro
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
     *
     */

    public void drawWithStride() {
        // j'utilise le shader prévu
        ProgramShader sh = this.getScene().getProgramShaderProvider().getShaderByName("simple");
        this.getScene().getProgramShaderProvider().use(sh);

        if (this.textureEnabled) {

            //on active l'unité de traitement des textures 0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

            // on demande à opengl d'utiliser la texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture().getGlBufferId());
            //on fait pointer  uniform_texture_location sur le buffer où est la texture
            GLES20.glUniform1i(sh.uniform_texture_location, 0);
        }

        final int stride = (Vertex.Vertex_COORD_SIZE + Vertex.Vertex_TEXT_SIZE) * Vertex.FLOAT_SIZE;

        //je me place sur le buffer stride qui contient x,y,z,u,v
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getScene().strideBuffer[0]);

        //lecture des coordonées x,y,z:
        // plutôt que de passer des valeur au shader, on indique une position en int.
        // OpenGl comprend alors qu'il faut passer par le pointeur sur lequel on vient de
        // se positionner pour aller chercher les valeurs contenue dans la mémoire graphique
        // ici on commence à lire le buffer à partir de la position zéro et on fait des saut
        // de "stride=5" pour passer aux coordonnées suivantes
        GLES20.glVertexAttribPointer(sh.attrib_vertex_coord_location, 3,
                GLES20.GL_FLOAT, false, stride, 0);

        //Chargement des coodonées de texture
        //ici on commence la lecture des coordonées de texture juste après les premier x,y,z
        // ensuite on fait des saut pour lire les suivantes
        GLES20.glVertexAttribPointer(sh.attrib_texture_coord_location, 2,
                GLES20.GL_FLOAT, false, stride, Vertex.Vertex_TEXT_SIZE * Vertex.FLOAT_SIZE);


        //--------------------------------------------
        sh.enableShaderVar();


        //Calcul de la matrice model-view-projection
        float[] mMvp = new float[16];

        if (this.getScene().getViewMode() == Scene.VIEW_MODE.ORTHO) {
            Matrix.multiplyMM(mMvp, 0, this.getScene().mProjectionORTH, 0,
                    this.mModelView, 0);

        } else
            Matrix.multiplyMM(mMvp, 0, this.getScene().getProjectionView(), 0,
                    this.mModelView, 0);

        // On alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, mMvp, 0);

        //on alimente la donnée Alpha
        GLES20.glUniform1f(sh.uniform_alpha_location, this.getAlpha());

        //je me place sur le buffer des index
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.getScene().vboi[0]);

        //je dessine les vertex selon l'ordre indiqué dans l'index.
        // au lieu de fournir des valeurs on indique zéro. ce qui permet à opengl de comprendre
        // qu'il faut passer par le pointeur sur lequel on viens de se postionner
        GLES20.glDrawElements(this.drawMode, this.getIndices().capacity(),
                GLES20.GL_UNSIGNED_SHORT, 0);

        //je pointe les buffer sur "rien" pour ne pas les réutiliser par erreur
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        if (this.canCollide) {
            this.mCollisionBox.update();
            if (mCollisionBox.isVisible) {
                //on dessine la colission Box en réutilisant la même MVP
                this.mCollisionBox.draw(mMvp);
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
        float[] wrkRotationMatrix = new float[16];
        float[] modelView = new float[16];

        Matrix.setIdentityM(modelView,0);

        /**
         * translation*rotation*scale
         */

        //déplacement vers les coordonnées x,y,z
        Matrix.translateM(modelView, 0, this.X, this.Y, this.Z);

        //on fabrique une matrice de rotation
        Matrix.setRotateEulerM(wrkRotationMatrix, 0, this.angleRADX,
                this.angleRADY, this.angleRADZ);

        //Calcul de la matrice ModelView
        Matrix.multiplyMM(this.mModelView, 0,modelView , 0,
                wrkRotationMatrix, 0);

        //Scales matrix m in place by sx, sy, and sz.
        //on divide par 2 car le centre de l'objet est au milieu
        //concrètement pour le coté haut du rectangle , on a un vertex à -1,1 et l'autre à 1,1
        //si on scale en x30 on va avoir des vertex -30,30 et 30,30 , soit une ditance de 60 entre les
        // 2 vertex
        Matrix.scaleM(this.mModelView, 0, this.getWidth()/2, this.getHeight()/2, 0.f);

    }

    /**
     * Fonction de mise à jour générale
     *
     * @param openGLActivity
     */

    public void mainUpdate(OpenGLActivity openGLActivity) {

        // traiter les opérations diverses à effectuer lors de
        // la mise à jour
        this.onUpdate();

        // traiter les actions a faire en cas de colissions
        this.applyCollisions();

        // A la fin des mises à jour on connais les nouvelles coordonées
        // on peut calculer la nouvelle matrice modelView
        this.updateModelView();

        // -----------------------------------------
        // Mettre a jour la boite de colision si elle existe
        // ------------------------------------------
        if (this.canCollide) {
            this.mCollisionBox.update();
        }

        // -----------------------------------------------------
        // Traiter les évènements écoutés sur les autres objets
        // -----------------------------------------------------
        updateListerners();


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
     * On écoute les objets
     * note : on dépend de l'ordre dans lequel sont traité
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
     * Actions effectuer lorsque l'on écoute les objets
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
     * Actions effectuer lorsque l'animation s'arrête
     *************************************************************************/
    public void onAnimationStop() {

    }

    /**
     * Retourne la liste des objets avec lesquels le GameObject est en colission
     *
     * @param gameobject
     * @return
     */
    public boolean isCollideWith(GameObject gameobject) {

        return this.mCollideWithList.contains(gameobject);

    }

    /**
     * @return
     * @throws CloneNotSupportedException
     */
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
