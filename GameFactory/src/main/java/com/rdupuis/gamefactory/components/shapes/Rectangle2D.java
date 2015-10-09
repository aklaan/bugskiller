package com.rdupuis.gamefactory.components.shapes;

import android.os.SystemClock;
import com.rdupuis.gamefactory.components.Shape;
import com.rdupuis.gamefactory.components.Vertex;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class Rectangle2D extends Shape {

	static final int NB_RECTANGLE_VERTEX = 4;
	private float width = 1;
	private float hight = 1;
	private boolean firstFrame = true;
	float startTime;

	public Rectangle2D(DrawingMode drawingMode) {
		super();

		// on ajoute les vertex (x,y,zu,v)
		this.mVertices.add(new Vertex(-1f, 1f, 0f, 0f, 0f));
		this.mVertices.add(new Vertex(-1f, -1f, 0f, 0f, 1f));
		this.mVertices.add(new Vertex(1f, -1f, 0f, 1f, 1f));
		this.mVertices.add(new Vertex(1f, 1f, 0, 1f, 0f));

		startTime = SystemClock.elapsedRealtime();
		switch (drawingMode) {
		// on dessine que les lignes de contour
		case EMPTY:

			this.initBuffers(8);
			this.putIndice(0, 0);
			this.putIndice(1, 1);

			this.putIndice(2, 1);
			this.putIndice(3, 2);

			this.putIndice(4, 2);
			this.putIndice(5, 3);

			this.putIndice(6, 3);
			this.putIndice(7, 0);

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
		
	}
	
	public void onUpdate2() {
		// TODO Auto-generated method stub

		/**
		 * 
		 * (0)*-----------*(3) | | | | | | | | (1)*-----------*(2)
		 */

		float decalage = 0.2f;
		float elapsedTime = SystemClock.elapsedRealtime() - startTime;

		if (elapsedTime >1000){
			startTime = SystemClock.elapsedRealtime();
		
		if (firstFrame) {
			this.mVertices.get(0).u = 0;
			this.mVertices.get(1).u = 0;
			this.mVertices.get(2).u = decalage;
			this.mVertices.get(3).u = decalage;
           firstFrame = false;
		} else {
		
		
		
		this.mVertices.get(0).u += decalage;
		this.mVertices.get(1).u += decalage;
		this.mVertices.get(2).u += decalage;
		this.mVertices.get(3).u += decalage;
		}
		//Si au prochain dcale on dépasse, on revient
		// sur la first frame
		if (this.mVertices.get(3).u + decalage > 1){
			
			this.firstFrame = true;
			
			}
			
		}
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

	private void updateVertices() {

		// comme le 0,0 est au milieu on divise par 2
		float w = (float) width / 2;
		float h = (float) hight / 2;

		this.mVertices.get(0).x = -w;
		this.mVertices.get(0).y = h;

		this.mVertices.get(1).x = -w;
		this.mVertices.get(1).y = -h;

		this.mVertices.get(2).x = w;
		this.mVertices.get(2).y = -h;

		this.mVertices.get(3).x = w;
		this.mVertices.get(3).y = h;

	}


}
