package com.rdupuis.gamefactory.utils;

import android.content.Context;
import android.opengl.Matrix;

import com.rdupuis.gamefactory.components.Vertex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;


public class Tools {

    /** read an asset file as Text File and return a string */
    public static String readShaderFile(Context context, String filename) {
		return filename;
      
    	/***
    	try {
            InputStream iStream = context.getAssets().open(
            		   		context.getString(R.string.shaderfolder) +"/" +            		
            		filename);
            return readStringInput(iStream);
        } catch (IOException e) {
            Log.e("Testgame", "Shader " + filename + " cannot be read");
            return "";
        }
*/    }

    /** read string input stream */
    public  static String readStringInput(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();

        byte[] buffer = new byte[4096];
        for (int n; (n = in.read(buffer)) != -1;) {
            sb.append(new String(buffer, 0, n));
        }
        return sb.toString();
    }

	// retourne un float aléatoire entre 0 et 1
	public static float getRamdom() {
		float value = (float) (Math.random() * 2. - 1.);
		return value;
	}

    public static void putXYZIntoFbVertices(FloatBuffer fbVertices,int index, Vertex vertex) {
    // la position physique en mémoire des bytes qui représentent le vertex
    // c'est la taille d'un vertex en bytes x l'index
    fbVertices.rewind();
    // ici on se positionne dans le buffer à l'endroit ou l'on va ecrire le
    // prochain vertex
    fbVertices.position(Vertex.Vertex_COORD_SIZE * index);
    fbVertices.put(vertex.x).put(vertex.y).put(vertex.z);
    // on se repositionne en 0 , prêt pour la relecture
    fbVertices.rewind();

    }

    /**
     * Cette fonction permet de récupérer les coordonées de vertex à l'écran
     * en multipliant le vertex par la matrice MVP
     *
     * @param modelView
     * @return
     */
    public static ArrayList<Vertex> applyModelView(ArrayList<Vertex> vertices,float[] modelView) {

        // on récupère les vertices de l'objet
        // et on calcule leur coordonées dans le monde
        float[] oldVerticesCoord = new float[4];
        float[] newVerticesCoord = new float[4];

        // définition d'un tableau de flotants
        ArrayList<Vertex> mModelViewVertices = new ArrayList<Vertex>();

        // je suis obligé de passer par un vecteur 4 pour la multiplication

        for (int i = 0; i < vertices.size(); i++) {
            oldVerticesCoord[0] = vertices.get(i).x; // x
            oldVerticesCoord[1] = vertices.get(i).y; // y
            oldVerticesCoord[2] = vertices.get(i).z; // z
            oldVerticesCoord[3] = 1.f;

            Matrix.multiplyMV(newVerticesCoord, 0, modelView, 0,
                    oldVerticesCoord, 0);
            mModelViewVertices.add(new Vertex(newVerticesCoord[0],
                    newVerticesCoord[1], newVerticesCoord[2]));

        }

        return mModelViewVertices;

    }


}
