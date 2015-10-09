package com.rdupuis.gamefactory.components;

public class Vertex implements Cloneable{

	//Coordonnée dans l'espace tridimentionel
	public float x;
	public float y;
	public float z;
	//coordonnées de texture
	public float u;
	public float v;

	
    public final static int Vertex_COORD_SIZE = 3;
    public final static int Vertex_COORD_SIZE_BYTES = Vertex_COORD_SIZE*4;

    public final static int Vertex_TEXT_SIZE = 2;
    public final static int Vertex_TEXT_SIZE_BYTES = Vertex_TEXT_SIZE*4;

public Vertex() {
	x=y=z=u=v=0f;
		
}

public Vertex(float x,float y,float z) {
	this.setXYZ(x, y, z);
		
}


public Vertex(float x,float y,float z,float u,float v) {
	this.setXYZ(x, y, z);
	this.setUV(u, v);
}


public void setXYZ(float a, float b, float c) {
	x=a;
	y=b;
	z=c;	
}

public void setUV(float a, float b) {
	u=a;
	v=b;
		
}

public Vertex clone() throws CloneNotSupportedException{
return (Vertex) super.clone();
}
}
