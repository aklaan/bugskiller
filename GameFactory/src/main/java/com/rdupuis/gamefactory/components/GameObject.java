package com.rdupuis.gamefactory.components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.rdupuis.gamefactory.animations.Animation;
import com.rdupuis.gamefactory.interfaces.Drawable;
import com.rdupuis.gamefactory.shaders.ProgramShader_simple;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class GameObject implements Drawable, Cloneable {
	
	private String mTagName;
	public Texture mTexture;
	public int newTextureId;
	public Boolean textureEnabled;
	public Boolean isVisible;
	public Scene mScene;
	public String viewMode = "ORTHO";
	private float alpha ;

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
	public Animation mAnimation;

	public float[] mRotationMatrix = new float[16];
	public float[] mModelView = new float[16];
	public float[] mTransformUpdateView = new float[16];

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

		//par d�faut l'Alpha est � 100% 
		this.setAlpha(1);
		textureEnabled = false;
		mTagName = "";
		isVisible = true;
		Matrix.setIdentityM(this.mRotationMatrix, 0);

		Matrix.setIdentityM(this.mTransformUpdateView, 0);

		this.mCollideWithList = new ArrayList<GameObject>();
		this.mGameObjectToListenList = new ArrayList<GameObject>();

		this.mVertices = new ArrayList<Vertex>();
	
	
	
	
	}

	public ArrayList<GameObject> getGameObjectToListenList() {
		return this.mGameObjectToListenList;
	}

	public Animation getAnimation() {
		return this.mAnimation;
	}

	public void setAnimation(Animation anim) {
		this.mAnimation = anim;
	}

	public void initBuffers(int nbIndex) {
		int nbVertex = mVertices.size();

		mFbVertices = ByteBuffer.allocateDirect(nbVertex * 3 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		mTextCoord = ByteBuffer.allocateDirect(nbVertex * 2 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

	}

	public void enableColission() {
		this.mCollisionBox = new CollisionBox(this);
		this.canCollide = true;
	}

	public void disableColission() {
		this.mCollisionBox = null;
		this.canCollide = false;
	}

	// setter vertices
	public void putVertex(int index, Vertex vertex) {
		// la position physique en m�moire des bytes qui repr�sentent le vertex
		// c'est la taille d'un vertex en bytes x l'index
		mFbVertices.rewind();
		// ici on se positionne dans le buffer � l'endroit o� l'on va ecrire le
		// prochain vertex
		mFbVertices.position(Vertex.Vertex_COORD_SIZE * index);
		mFbVertices.put(vertex.x).put(vertex.y).put(vertex.z);
		// on se repositionne en 0 , pr�t pour la relecture
		mFbVertices.rewind();

		mTextCoord.position(Vertex.Vertex_TEXT_SIZE * index);
		mTextCoord.put(vertex.u).put(vertex.v);
		// on se repositionne en 0 , pr�t pour la relecture
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

	// getter vertices
	public FloatBuffer getFbVertices() {

		for (int i = 0; i < this.mVertices.size(); i++) {
			this.putVertex(i, this.mVertices.get(i));
		}

		return mFbVertices;
	}

	// getter TextCoord
	public FloatBuffer getTextCoord() {
		return mTextCoord;
	}

	// getter indices
	public ShortBuffer getIndices() {
		return mIndices;
	}

	public void setTexture(Texture texture) {
		mTexture = texture;

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
	 * 
	 * public void setWidth(float width) { // this.width = width; //
	 * updateModelMatrix(); }
	 * 
	 * public float getHeight() { return hight; }
	 * 
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
		ProgramShader_simple sh = (ProgramShader_simple) this.getScene()
				.getProgramShaderProvider().getShaderByName("simple");
		
		this.getScene().getProgramShaderProvider().use(sh);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent �tre dessin�s
		this.getIndices().rewind();

		// on charge les coordonn�es des vertices
		sh.setVerticesCoord(this.getFbVertices());
		this.getFbVertices().rewind();

		// on charge les coordon�es de texture

		sh.setTextureCoord(this.getTextCoord());

		// if (sh.attrib_color_location != -1) {
		// this.getVertices().position(0);
		// GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
		// GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		sh.enableShaderVar();

		float[] mMvp = new float[16];

		if (this.viewMode == "ORTHO") {
			Matrix.multiplyMM(mMvp, 0, this.getScene().mProjectionORTH, 0,
					this.mModelView, 0);

		} else
			Matrix.multiplyMM(mMvp, 0, this.getScene().getProjectionView(), 0,
					this.mModelView, 0);

		// On alimente la donn�e UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, mMvp, 0);

		
		GLES20.glUniform1f(sh.uniform_alpha_location, this.getAlpha());
		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent �tre dessin�s
		this.getIndices().rewind();

		GLES20.glDrawElements(this.drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

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
	// apport�es.
	public void updateModelView() {
		float[] wrkModelView = new float[16];
		float[] wrkRotationMatrix = new float[16];

		if (this.viewMode == "ORTHO") {
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
	 * Fonction de mise � jour g�n�rale
	 * 
	 * @param openGLActivity
	 */

	public void mainUpdate(OpenGLActivity openGLActivity) {

		// traiter les op�rations diverses � effectuer lors de
		// la mise � jour
		this.onUpdate();

		// traiter les actions a faire en cas de colissions
		this.applyCollisions();

		// traiter la lecture de l'animation si elle existe
		this.applyAnimation();

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
		if (textureEnabled) {
			this.getScene().getBitmapProvider()
					.putTextureToGLUnit(this.mTexture, 0);
		}

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

	/***********************************************
	 * Traiter l'animation si elle existe
	 **********************************************/
	public void applyAnimation() {
		// -----------------------------------------
		// traiter l'animation
		// --------------------------------------------------------
		if (this.getAnimation() != null) {

			if (this.getAnimation().getStatus() == Animation.AnimationStatus.PLAYING) {
				this.getAnimation().play();
				// traiter les actions supl�mentaires lors de la lecture
				onAnimationPlay();
			}
			if (this.getAnimation().getStatus() == Animation.AnimationStatus.STOPPED) {
				// this.setAnimation(null);
				// traiter les actions supl�mentaires a la fin de la lecture
				onAnimationStop();
			}

		}
	}

	/********************************************************
	 * On �coute les objets note : on d�pend de l'ordre dans lequel sont trait�
	 * les objets
	 * 
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
	 * 
	 *************************************************************************/
	public void onAnimationPlay() {

	}

	/**************************************************************************
	 * Actions effectuer lorsque l'animation s'arr�te
	 * 
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
		if (gameobject.getAnimation() != null) {
			Animation anim = (Animation) gameobject.getAnimation().clone();

			anim.setParent(gameobject);

			gameobject.setAnimation(anim);
		}

		// si l'objet source peu entrer en collision on
		// red�fini un nouvelle boite de colision pour la cible
		// sinon elle va avoir la m�me que la source
		if (gameobject.canCollide) {
			gameobject.enableColission();

		}
		return gameobject;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		// on s'assure que l'alpha est toujours compris enre 0 et 1;
		this.alpha = (alpha <0)? 0: alpha;
		this.alpha = (alpha >1)? 1: alpha;
	}
}
