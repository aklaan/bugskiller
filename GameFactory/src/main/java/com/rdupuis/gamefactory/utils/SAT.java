package com.rdupuis.gamefactory.utils;

import java.util.ArrayList;

import com.rdupuis.gamefactory.components.GameObject;

public class SAT {
	// ArrayList<Vector2D> mTempVertices = new ArrayList<Vector2D>();
	// ArrayList<Vector2D> mSortedVertices = new ArrayList<Vector2D>();
	// ArrayList<Vector2D> mNormals = new ArrayList<Vector2D>();

	// reconstituer la liste des vextex dans le bon ordre à partir du
	// floatbuffer mvertice et shortbuffer mindice de la forme
	public static ArrayList<Vector2D> getVerticesInOrder(GameObject mShape) {
		ArrayList<Vector2D> result = new ArrayList<Vector2D>();

		int size = mShape.mVertices.size(); // on fait moins 1 car on commence a
											// zéro

		for (int i = 0; i < size; i++) {

			result.add(new Vector2D(mShape.mVertices.get(i).x, mShape.mVertices
					.get(i).y));

		}

		return result;
	}

	public static ArrayList<Vector2D> prepareVector(GameObject mShape) {
		ArrayList<Vector2D> result = new ArrayList<Vector2D>();

		int size = mShape.mVertices.size() - 1; // on fait moins 1 car on
												// commence a zéro

		for (int i = 0; i < size; i++) {

			result.add(new Vector2D(mShape.mVertices.get(i + 1).x
					- mShape.mVertices.get(i).x, mShape.mVertices.get(i + 1).y
					- mShape.mVertices.get(i).y));

		}

		result.add(new Vector2D(mShape.mVertices.get(0).x
				- mShape.mVertices.get(size).x, mShape.mVertices.get(0).y
				- mShape.mVertices.get(size).y));

		return result;
	}

	//Calcul des vecteurs Normal aux faces
	//pour que le calcul SAT fonctionne, il faut que le vecteur normal soit 
	//unitaire (magnitude de 1) sinon ça ne fonctionne pas.
	public static ArrayList<Vector2D> getNormals(GameObject mShape) {

		ArrayList<Vector2D> result = new ArrayList<Vector2D>();
		ArrayList<Vector2D> preparedVector = prepareVector(mShape);

		// le vecteur normal à(X,Y) est (-Y,X)

		int size = preparedVector.size();

		//calcul des vecteur Normal droite
		for (int i = 0; i < size; i++) {

			float x = -preparedVector.get(i).y;
			float y = preparedVector.get(i).x;
			
			//calcul de la magnitude du vecteur pour normailser le vecteur 
			float mag = (float) Math.sqrt(x * x + y * y);

			result.add(new Vector2D(x / mag, y / mag));

		}

		return result;

	}

	public static float getMinProj(ArrayList<Vector2D> vectorlist, Vector2D axis) {
		float minProj = vectorlist.get(0).dot(axis);
		float proj = 0.f;

		for (int i = 1; i < vectorlist.size(); i++) {
			proj = vectorlist.get(i).dot(axis);

			minProj = (proj < minProj) ? proj : minProj;
		}

		return minProj;
	}

	public static float getMaxProj(ArrayList<Vector2D> vectorlist, Vector2D axis) {

		float maxProj = vectorlist.get(0).dot(axis);
		float proj = 0.f;
		for (int i = 1; i < vectorlist.size(); i++) {
			proj = vectorlist.get(i).dot(axis);
			maxProj = (proj > maxProj) ? proj : maxProj;

		}

		return maxProj;
	}

	public static boolean isColide(GameObject shape1, GameObject shape2) {
		Boolean isSperarated = true;

		ArrayList<Vector2D> shape1_SortedVertices = new ArrayList<Vector2D>();
		ArrayList<Vector2D> shape2_SortedVertices = new ArrayList<Vector2D>();

		ArrayList<Vector2D> shape1_Normals = new ArrayList<Vector2D>();
		ArrayList<Vector2D> shape2_Normals = new ArrayList<Vector2D>();

		shape1_SortedVertices = getVerticesInOrder(shape1);
		shape1_Normals = getNormals(shape1);

		shape2_SortedVertices = getVerticesInOrder(shape2);
		shape2_Normals = getNormals(shape2);

		float shape1_minproj = 0.0f;
		float shape1_maxproj = 0.0f;

		float shape2_minproj = 0.0f;
		float shape2_maxproj = 0.0f;

		// pour chaque normal de la shape1, on va regarder s'il y a intersection
		for (int i = 0; i < shape1_Normals.size(); i++) {
			shape1_minproj = getMinProj(shape1_SortedVertices,
					shape1_Normals.get(i));
			shape1_maxproj = getMaxProj(shape1_SortedVertices,
					shape1_Normals.get(i));

			shape2_minproj = getMinProj(shape2_SortedVertices,
					shape1_Normals.get(i));
			shape2_maxproj = getMaxProj(shape2_SortedVertices,
					shape1_Normals.get(i));

			
			// si shape1_maxproj < shape2_minproj
			// c'est qu'il existe un �cart entre le point le plus � droite de la shape 1
			// et le point le plus � gauche de la shape2
			// on test l'inverse aussi.
			isSperarated = shape1_maxproj < shape2_minproj
					|| shape2_maxproj < shape1_minproj;
			
			// d�s que l'on d�t�cte le moindre �cart c'est qu'il n'ya pas colission
			 
			if (isSperarated) {
				break;
			}
			;

			// on a pas d�cel� d'�cart entre la shape1 et shape2 avec en utilisant les normale
			// de la shape1. on v�rifie si c'est toujours le cas avec les normales de la shape2
			for (int j = 0; j < shape2_Normals.size(); j++) {
				shape1_minproj = getMinProj(shape1_SortedVertices,
						shape2_Normals.get(j));
				shape1_maxproj = getMaxProj(shape1_SortedVertices,
						shape2_Normals.get(j));

				shape2_minproj = getMinProj(shape2_SortedVertices,
						shape2_Normals.get(j));
				shape2_maxproj = getMaxProj(shape2_SortedVertices,
						shape2_Normals.get(j));

				isSperarated = shape1_maxproj < shape2_minproj
						|| shape2_maxproj < shape1_minproj;
				// si a ce stade on d�tecte une s�paration alors la r�gle
				// stipulant
				// que toutes
				// les normales doivent .............
				if (isSperarated) {
					break;
				}
				;
			}
		}
		if (isSperarated) {
			// Log.i("s�par�?", String.valueOf(isSperarated));
		}

		return !isSperarated;

	}

}
