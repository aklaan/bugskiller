package com.rdupuis.gamefactory.utils;

public class Vector2D {

	// Coordonnée de vecteur
	public float x;
	public float y;

	// conctructeur du vecteur nul
	public Vector2D() {
		x = y = 0f;

	}

	public Vector2D(float x, float y) {
		this.setXY(x, y);

	}

	public void setXY(float a, float b) {
		x = a;
		y = b;

	}

	public void normalise() {

	}

	public float dot(Vector2D vector) {
		return (this.x * vector.x) + (this.y * vector.y);

	}

}
