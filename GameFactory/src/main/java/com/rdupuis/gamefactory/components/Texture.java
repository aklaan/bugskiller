package com.rdupuis.gamefactory.components;

import java.nio.ByteBuffer;

public class Texture {

	private int width;
	private int height;
	private float alpha;
	private ByteBuffer bufferTexture;
	private int ressourceId;
	private int glBufferId;

	public int getGlBufferId() {
		return glBufferId;
	}

	public void setGlBufferId(int glBufferId) {
		this.glBufferId = glBufferId;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int hight) {
		this.height = hight;
	}

	//-----------------------------------------------
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	//-----------------------------------------------
	public ByteBuffer getBufferTexture() {
		return bufferTexture;
	}
	public void setBufferTexture(ByteBuffer bufferTexture) {
		this.bufferTexture = bufferTexture;
	}

	//-----------------------------------------------
	public int getRessourceId() {
		return ressourceId;
	}

	public void setRessourceId(int ressourceID) {
		this.ressourceId = ressourceID;
	}


	public Texture() {
		this.alpha = 1;
	}
}
