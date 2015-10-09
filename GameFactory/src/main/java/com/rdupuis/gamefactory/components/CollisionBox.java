package com.rdupuis.gamefactory.components;

import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.shaders.ProgramShader_forLines;

import android.opengl.GLES20;

public class CollisionBox extends Rectangle2D {

	public float offsetX;
	public float offsetY;
	public GameObject parent;

	public CollisionBox(GameObject go) {
		super(DrawingMode.EMPTY);
		this.isVisible = false;
		this.drawMode = GLES20.GL_LINES;
		
		this.offsetX = -0.10f;
		this.offsetY = -0.10f;
		this.parent = go;

	}

	public String getTagName(){
	return this.parent.getTagName();
		
	}
	public Scene getScene(){
		return this.parent.mScene;
	}
	
	
	public void update() {
		// aller rechercher les points limite de la forme et en déduire
		// un rectangle avec un retrait edgelimit

		// naviguer dans le float buffer des x,y,z

		float xread = 0f;
		float yread = 0f;
		float xmin = 0f;
		float xmax = 0f;
		float ymin = 0f;
		float ymax = 0f;

		// pour chaque vertex composant la forme, on va en déterminer les
		// limites pour fabriquer une boite de colision
		for (int i = 0; i < this.parent.mVertices.size(); i++) {

			// lecture du X
			xread = this.parent.mVertices.get(i).x;
			xmin = (xread < xmin) ? xread : xmin;
			xmax = (xread > xmax) ? xread : xmax;

			// lecture du Y
			yread = this.parent.mVertices.get(i).y;
			ymin = (yread < ymin) ? yread : ymin;
			ymax = (yread > ymax) ? yread : ymax;

			/*
			 * Log.i("xy",String.valueOf(i) +" / " +String.valueOf(taillemax)
			 * +" : " +String.valueOf(xmin)+ "/" + String.valueOf(xmax));
			 */
		}

		//gestion des offset en taille
		xmin+=offsetX; 
		xmax+=offsetX;
		ymin+=offsetY;
		ymax+=offsetY;
		this.mVertices.clear();
		this.mVertices.add(new Vertex(xmin, ymax, 0));
		this.mVertices.add(new Vertex(xmin, ymin, 0));
		this.mVertices.add(new Vertex(xmax, ymin, 0));
		this.mVertices.add(new Vertex(xmax, ymax, 0));

		// on redéfinit les coordonées des vertices
		// pour avoir les coordonnées transformées

		this.mVertices = this.applyModelView(this.parent.mModelView);

		this.setCoord(this.parent.X, this.parent.Y);

	}

	@Override
	public void draw() {

		ProgramShader_forLines sh = (ProgramShader_forLines) this.getScene().mProgramShaderProvider
				.getShaderByName("forLines");
		this.getScene().mProgramShaderProvider.use(sh);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent étre dessinés
		this.getIndices().position(0);

		// on charge les coordon�es de texture
		sh.setTextureCoord(this.getTextCoord());

		// if (sh.attrib_color_location != -1) {
		// this.getVertices().position(0);
		// GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
		// GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		sh.enableShaderVar();

		// on charge les coordonn�es des vertices
		sh.setVerticesCoord(this.getFbVertices());

		// on alimente la donn�e UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false,
				this.getScene().mProjectionView, 0);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent �tre dessin�s
		this.getIndices().position(0);

		GLES20.glDrawElements(this.drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		// renderer.mProgramme1.disableVertexAttribArray();
		// �quivalent du POP
		// / renderer.mModelView = this.mBackupModelView;
		// renderer.mProgramme1.disableVertexAttribArray();

	}

}
