package com.rdupuis.gamefactory.components;

import android.opengl.Matrix;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class UserFinger extends Rectangle2D {

	float worldX;
	float worldY;
	float histoX;
	float histoY;

	public final static String USER_FINGER_TAG = "USER_FINGER_TAG";
	
	public UserFinger() {
		super(DrawingMode.EMPTY);
		this.setHeight(100);
		this.setWidth(100);
		this.enableColission();
		this.mCollisionBox.isVisible = true;
		this.setTagName(USER_FINGER_TAG);
		this.isStatic = false;
		// on fait exprès de définir le premier point loin de l'écran
		//pour éviter les colision au premier cycle
		this.X = 10000;
		
	}

	public void onUpdate() {
		// si l'utilisateur touche l'écran
		if (this.getScene().getActivity().mGLSurfaceView.touched) {

			// on met à jour les coordonées
			this.setCoord(
					this.getScene().getActivity().mGLSurfaceView.touchX,
					this.getScene().getHeight()
							- this.getScene().getActivity().mGLSurfaceView.touchY);

			this.histoX = this.getScene().getActivity().mGLSurfaceView.histoX;
			this.histoY = this.getScene().getActivity().mGLSurfaceView.histoY;

			// on active les colissions
			this.canCollide = true;
			//getWorldCoord();

			/**
			 * Log.i("UserFinger", String.valueOf(this.X) + "/" +
			 * String.valueOf(this.Y));
			 * 
			 * Log.i("UserFinger World", String.valueOf(this.worldX) + "/" +
			 * String.valueOf(this.worldY)); Log.i("--",
			 * "----------------------------------------------------------------"
			 * );
			 */
		} else {
			// sinon les collisions sont désactivées
			this.canCollide = false;
		}

	}

	public float[] getHomogenicCoord() {
		float[] homCoord = new float[4];

		homCoord[0] = (2.0f * this.X) / this.getScene().getWidth() - 1.0f;
		homCoord[1] = (2.0f * this.Y) / this.getScene().getHeight() - 1.0f;
		homCoord[2] = 1.0f;
		homCoord[3] = 1.0f;
		return homCoord;
	}

	// pour retourver les coordonées du pointeur dans le monde, il faut faire le
	// cheminement inverse
	// ecran -> coorconées homogènes -> coordonée projection -> coordonées model
	public void getWorldCoord() {
		float[] projCoord = new float[4];
		float[] viewCoord = new float[4];

		float[] reverseProjectionView = new float[16];
		float[] reverseVMatrix = new float[16];
		

		Matrix.invertM(reverseProjectionView, 0, this.getScene()
				.getProjectionView(), 0);
		Matrix.invertM(reverseVMatrix, 0, this.getScene().mVMatrix, 0);

		// je suis obligé de passer par un vecteur 4 pour la multiplication
		// calcul du point placé sur le premier plan de clipping

		// les coordonnée du pointeur doivent être normalisée
		// attention les coordonée ANDROID sont inversées par rapport à OpenGL
		// le 0,0 est en haut a gauche pour android tandis qu'il est en bas à
		// droite pour GL

		Matrix.multiplyMV(projCoord, 0, reverseProjectionView, 0,
				this.getHomogenicCoord(), 0);

		Matrix.multiplyMV(viewCoord, 0, reverseVMatrix, 0, projCoord, 0);

		// on divise par W pour revenir en coordonées World
		// le W correspond à la mise en perspective effectuée par la matrice de
		// projection.
		// comme on a utilisé la matrice inverse on a inversé aussi la
		// perspective. du coup il faut la remettre dans le bon sens.
		viewCoord[0] = viewCoord[0] / viewCoord[3];
		viewCoord[1] = viewCoord[1] / viewCoord[3];
		viewCoord[2] = viewCoord[2] / viewCoord[3];

		this.worldX = viewCoord[0];
		this.worldY = viewCoord[1];

	}

}
