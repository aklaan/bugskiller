package com.rdupuis.gamefactory.scene01.components;

import android.opengl.GLES20;

import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.Vertex;

public class Line2D extends GameObject{

	public Line2D(int nbVertex, int nbIndex) {
		super();
		this.initBuffers( 2);
		this.putVertex(0, new Vertex(0, 1, 0));
		this.putVertex(1, new Vertex(0, -1, 0));
		
	/*	updateVertices();*/
		// on indique l'ordre dans lequel on doit affichier les vertex
		// pour dessiner les 2 triangles qui vont former le carré.
		this.putIndice(0, 0);
		this.putIndice(1, 1);
		this.drawMode = GLES20.GL_LINES;
	}

	
	
}
