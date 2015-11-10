package com.rdupuis.gamefactory.components;

import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.shaders.ProgramShader;
import com.rdupuis.gamefactory.shaders.ProgramShader_forLines;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class CollisionBox extends Rectangle2D {

    public float offsetX;
    public float offsetY;
    public GameObject parent;
    //tableau de vertices pour les coordonées world de la boite
    public ArrayList<Vertex> mWorldVertices; // d�finition d'un tableau de vertex


    public CollisionBox(GameObject go) {
        super(DrawingMode.EMPTY);
        this.isVisible = false;
        this.drawMode = GLES20.GL_LINES;
        this.mWorldVertices = new ArrayList<Vertex>();
        this.offsetX = -3.0f;
        this.offsetY = -3.0f;
        this.parent = go;

    }

    public String getTagName() {
        return this.parent.getTagName();

    }

    public Scene getScene() {
        return this.parent.getScene();
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
        xmin += -offsetX;
        xmax += offsetX;
        ymin += -offsetY;
        ymax += offsetY;
        this.mVertices.clear();
        this.mWorldVertices.clear();
        this.mVertices.add(new Vertex(xmin, ymax, 0));
        this.mWorldVertices.add(new Vertex(xmin, ymax, 0));

        this.mVertices.add(new Vertex(xmin, ymin, 0));
        this.mWorldVertices.add(new Vertex(xmin, ymin, 0));

        this.mVertices.add(new Vertex(xmax, ymin, 0));
        this.mWorldVertices.add(new Vertex(xmax, ymin, 0));

        this.mVertices.add(new Vertex(xmax, ymax, 0));
        this.mWorldVertices.add(new Vertex(xmax, ymax, 0));

        // on redéfinit les coordonées des vertices
        // pour avoir les coordonnées transformées

        this.mVertices = this.applyModelView(this.parent.mModelView);

        this.setCoord(this.parent.X, this.parent.Y);

    }

    @Override
    public void draw() {

        ProgramShader sh = this.getScene().mProgramShaderProvider
                .getShaderByName(ProgramShader_forLines.SHADER_FOR_LINES);
        this.getScene().mProgramShaderProvider.use(sh);

        // on se positionne au debut du Buffer des indices
        // qui indiquent dans quel ordre les vertex doivent étre dessinés
        this.getIndices().position(0);

        // on charge les coordon�es de texture
        sh.setTextureCoord(this.getTextCoord());

//		 if (sh.attrib_color_location != -1) {
        // this.getVertices().position(0);
        float[] color = new float[4];
        color[0] = 1.f;
        color[1] = 1.f;
        color[2] = 1.f;
        color[3] = 1.f;

        FloatBuffer toto = ByteBuffer.allocateDirect(4 * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        toto.put(color);


        GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
                GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, toto);

        sh.enableShaderVar();

        // on charge les coordonnées des vertices
        //sh.setVerticesCoord(this.getFbVertices());
        this.mFbVertices.clear();
        for (int i = 0; i < this.mWorldVertices.size(); i++) {
            this.putVertex(i, this.mWorldVertices.get(i));
        }
        sh.setVerticesCoord(this.mFbVertices);


//        float[] reverseVMatrix = new float[16];
        //       Matrix.invertM(reverseVMatrix, 0, this.mModelView, 0);

        float[] mMvp = new float[16];

        Matrix.multiplyMM(mMvp, 0, this.getScene().mProjectionORTH, 0,
                this.parent.mModelView, 0);


        // on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec
        // une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, this.parent.mMvp, 0);

        // on se positionne au debut du Buffer des indices
        // qui indiquent dans quel ordre les vertex doivent être dessinés
        this.getIndices().rewind();

        GLES20.glDrawElements(this.drawMode, this.getIndices().capacity(),
                GLES20.GL_UNSIGNED_SHORT, this.getIndices());

        // renderer.mProgramme1.disableVertexAttribArray();
        // �quivalent du POP
        // / renderer.mModelView = this.mBackupModelView;
        // renderer.mProgramme1.disableVertexAttribArray();

    }

}
