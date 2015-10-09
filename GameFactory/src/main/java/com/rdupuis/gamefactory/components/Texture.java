package com.rdupuis.gamefactory.components;

import java.nio.ByteBuffer;

public class Texture {

	public int width;
	public int hight;
	public float alpha;
	public ByteBuffer texture;
	public int textureNameID;

	public Texture() {
		this.alpha = 1;
	}
}
