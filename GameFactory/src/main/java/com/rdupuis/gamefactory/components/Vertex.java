package com.rdupuis.gamefactory.components;

public class Vertex implements Cloneable {

    //Coordonnée dans l'espace tridimentionel
    public float x, y, z;
    //coordonnées de texture
    public float u, v;
    //couleur
    public float r, g, b, a;


    //on calcul la taille d'un float en mémoire
    //généralement c'est 4 octet, mais en fonction du matériel ça peut être différent
    //par sécurité, on le recalcule plutot que d'utiliser 4 par défaut.
    public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;

    //pour les coordonnées on a 3 float X,Y,Z
    public final static int Vertex_COORD_SIZE = 3;
    //la taille mémoire necessaire pour stocker les coordonées du vertex
    public final static int Vertex_COORD_SIZE_BYTES = Vertex_COORD_SIZE * FLOAT_SIZE;

    //Pour les coordonées de texture on a 2 Float U,V
    public final static int Vertex_TEXT_SIZE = 2;
    //la taille mémoire necessaire pour stocker les coordonées de texture
    public final static int Vertex_TEXT_SIZE_BYTES = Vertex_TEXT_SIZE * FLOAT_SIZE;

    //Pour la couleur on a 4 Float r,v,b,a
    public final static int Vertex_COLOR_SIZE = 4;
    //la taille mémoire necessaire pour stocker les coordonées de texture
    public final static int Vertex_COLOR_SIZE_BYTES = Vertex_COLOR_SIZE * FLOAT_SIZE;

    public static final int stride = (Vertex.Vertex_COORD_SIZE + Vertex.Vertex_TEXT_SIZE + Vertex.Vertex_COLOR_SIZE);
    // *Vertex.FLOAT_SIZE;

    public Vertex() {
        //tout est à zéro sauf l'alpha
        x = y = z = u = v = r = g = b = 0f;
        a = 1f;
    }

    /**
     * construire un vertex avec seulement des coordonnées
     * @param x
     * @param y
     * @param z
     */
    public Vertex(float x, float y, float z) {
        this.setXYZ(x, y, z);

    }

    /**
     * Construire un vertex avec les coordonnées espace + coordonées texture
     * @param x
     * @param y
     * @param z
     * @param u
     * @param v
     */
    public Vertex(float x, float y, float z, float u, float v) {
        this.setXYZ(x, y, z);
        this.setUV(u, v);
    }


    public Vertex(float x, float y, float z, float u, float v,float r, float g, float b, float a) {
        this.setXYZ(x, y, z);
        this.setUV(u, v);
        this.setRGBA(r,g,b,a);
    }

    public void setXYZ(float a, float b, float c) {
        x = a;
        y = b;
        z = c;
    }

    public void setUV(float a, float b) {
        u = a;
        v = b;

    }

    public void setRGBA(float red, float green,float blue,float alpha) {
        r = red;
        g = green;
        b = blue;
        a = alpha;

    }

    public Vertex clone() throws CloneNotSupportedException {
        return (Vertex) super.clone();
    }
}
