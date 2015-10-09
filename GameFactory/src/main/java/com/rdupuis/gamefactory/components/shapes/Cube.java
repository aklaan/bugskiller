package com.rdupuis.gamefactory.components.shapes;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.rdupuis.gamefactory.components.Shape;
import com.rdupuis.gamefactory.components.Vertex;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class Cube extends Shape {

	static final int NB_RECTANGLE_VERTEX = 8;
	private float width = 1;
	private float hight = 1;
	private float depth = 1;
	
	float startTime;

	public Cube(DrawingMode drawingMode) {
		super();

		// on ajoute les vertex (x,y,zu,v)
		this.mVertices.add(new Vertex(-1f, 1f, -1f, 0f, 0f));
		this.mVertices.add(new Vertex(-1f, -1f, -1f, 0f, 1f));
		this.mVertices.add(new Vertex(1f, -1f, -1f, 1f, 1f));
		this.mVertices.add(new Vertex(1f, 1f, -1, 1f, 0f));

		this.mVertices.add(new Vertex(-1f, 1f, 1f, 0f, 0f));
		this.mVertices.add(new Vertex(-1f, -1f, 1f, 0f, 1f));
		this.mVertices.add(new Vertex(1f, -1f, 1f, 1f, 1f));
		this.mVertices.add(new Vertex(1f, 1f, 1, 1f, 0f));

		
		
		
		startTime = SystemClock.elapsedRealtime();
		switch (drawingMode) {
		// on dessine que les lignes de contour
		case EMPTY:
			this.drawMode = GLES20.GL_LINES;
			this.initBuffers(24);
			this.putIndice(0, 0);
			this.putIndice(1, 1);

			this.putIndice(2, 1);
			this.putIndice(3, 2);

			this.putIndice(4, 2);
			this.putIndice(5, 3);

			this.putIndice(6, 3);
			this.putIndice(7, 0);

			//face arrière
			
			this.putIndice(8, 4);
			this.putIndice(9, 5);

			this.putIndice(10, 5);
			this.putIndice(11, 6);

			this.putIndice(12, 6);
			this.putIndice(13, 7);

			this.putIndice(14, 7);
			this.putIndice(15, 4);


            //cotées
			
			this.putIndice(16, 0);
			this.putIndice(17, 4);

			this.putIndice(18, 1);
			this.putIndice(19, 5);

			this.putIndice(20, 2);
			this.putIndice(21, 6);

			this.putIndice(22, 3);
			this.putIndice(23, 7);

			
			break;
		// on dessine des triangles plein
		case FILL:

			this.initBuffers(6);

			// on indique l'ordre dans lequel on doit affichier les vertex
			// pour dessiner les 2 triangles qui vont former le carré.
			this.putIndice(0, 0);
			this.putIndice(1, 1);
			this.putIndice(2, 2);

			this.putIndice(3, 0);
			this.putIndice(4, 2);
			this.putIndice(5, 3);
			break;
		}

	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
//this.angleRADY += 0.5f;
//this.angleRADX+= 0.5f;

	
	}

	public void setHeight(float h) {
		this.hight = h;
		updateVertices();
		if (this.canCollide) {
			this.mCollisionBox.update();
		}
	}

	public float getHeight() {
		return this.hight;
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float w) {
		this.width = w;
		updateVertices();
		if (this.canCollide) {
			this.mCollisionBox.update();
		}
	}

	
	public float getDepth() {
		return this.depth;
	}

	public void setDepth(float d) {
		this.depth = d;
		updateVertices();
		if (this.canCollide) {
			this.mCollisionBox.update();
		}
	}

	
	
	private void updateVertices() {

		// comme le 0,0 est au milieu on divise par 2
		float w = (float) width / 2;
		float h = (float) hight / 2;
		float d = (float) depth / 2;
		
		this.mVertices.get(0).x = -w;
		this.mVertices.get(0).y = h;
		this.mVertices.get(0).z = -d;
		
		this.mVertices.get(1).x = -w;
		this.mVertices.get(1).y = -h;
		this.mVertices.get(1).z = -d;
		
		this.mVertices.get(2).x = w;
		this.mVertices.get(2).y = -h;
		this.mVertices.get(2).z = -d;
		
		this.mVertices.get(3).x = w;
		this.mVertices.get(3).y = h;
		this.mVertices.get(3).z = -d;

		this.mVertices.get(4).x = -w;
		this.mVertices.get(4).y = h;
		this.mVertices.get(4).z = d;

		this.mVertices.get(5).x = -w;
		this.mVertices.get(5).y = -h;
		this.mVertices.get(5).z = d;
		
		this.mVertices.get(6).x = w;
		this.mVertices.get(6).y = -h;
		this.mVertices.get(6).z = d;
		
		this.mVertices.get(7).x = w;
		this.mVertices.get(7).y = h;
		this.mVertices.get(7).z = d;

	
	
	}


}
