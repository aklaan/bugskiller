package com.rdupuis.gamefactory.components;

public class Vertex implements Cloneable{

	//Coordonnée dans l'espace tridimentionel
	public float x;
	public float y;
	public float z;
	//coordonnées de texture
	public float u;
	public float v;

	//on calcul la taille d'un float en mémoire
	//généralement c'est 4 octet, mais en fonction du matériel ça peut être différent
	//par sécurité, on le recalcule plutot que d'utiliser 4 par défaut.
	public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;

	//pour les coordonnées on a 3 float X,Y,Z
    public final static int Vertex_COORD_SIZE = 3;
	//la taille mémoire necessaire pour stocker les coordonées du vertex
    public final static int Vertex_COORD_SIZE_BYTES = Vertex_COORD_SIZE*FLOAT_SIZE;

	//Pour les coordonées de texture on a 2 Float U,V
    public final static int Vertex_TEXT_SIZE = 2;
	//la taille mémoire necessaire pour stocker les coordonées de texture
	public final static int Vertex_TEXT_SIZE_BYTES = Vertex_TEXT_SIZE*FLOAT_SIZE;

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
