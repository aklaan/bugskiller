package com.rdupuis.gamefactory.components.shapes;

import android.os.SystemClock;

import com.rdupuis.gamefactory.components.Shape;
import com.rdupuis.gamefactory.components.Vertex;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.utils.CONST;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class Rectangle2D extends Shape {

    static final int NB_RECTANGLE_VERTEX = 4;
    //private float width = 1;
    //private float height = 1;
    private boolean firstFrame = true;
    float startTime;
    private DrawingMode mDrawingMode;


    public Rectangle2D(DrawingMode drawingMode) {
        super();

        this.mDrawingMode = drawingMode;
        this.setNbvertex(NB_RECTANGLE_VERTEX);
        this.setNbIndex((drawingMode == DrawingMode.FILL) ? 6 : 8);
    }


    @Override
    public ArrayList<Vertex> getVertices() {

        ArrayList<Vertex> temp = new ArrayList<Vertex>();
        // on ajoute les vertex (x,y,zu,v)
        temp.add(new Vertex(-0.5f, 0.5f, 0f, 0f, 0f, 1f, 1f, 1f, 0.5f));
        temp.add(new Vertex(-0.5f, -0.5f, 0f, 0f, 1f, 1f, 1f, 1f, 0.5f));
        temp.add(new Vertex(0.5f, -0.5f, 0f, 1f, 1f, 1f, 1f, 1f, 0.5f));
        temp.add(new Vertex(0.5f, 0.5f, 0, 1f, 0f, 1f, 1f, 1f, 0.5f));

        return temp;

    }


    @Override
    public ShortBuffer getIndices() {

        ShortBuffer result = null;

        switch (this.mDrawingMode) {
            // on dessine que les lignes de contour
            case EMPTY:
                result = getIndicesForEmptyRec();
                break;
            // on dessine des triangles plein
            case FILL:

                result = getIndicesForFillRec();

                // on indique l'ordre dans lequel on doit afficher les vertex
                // pour dessiner les 2 triangles qui vont former le carré.

        }
        result.rewind();
        return result;

    }

    public static ShortBuffer getIndicesForEmptyRec() {
        ShortBuffer indices;
        indices = ByteBuffer.allocateDirect(8 * CONST.SHORT_SIZE)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        indices.rewind();

        indices.put((short) 0).put((short) 1)
                .put((short) 1).put((short) 2)
                .put((short) 2).put((short) 3)
                .put((short) 3).put((short) 0);
        indices.rewind();

        return indices;

    }


    public static ShortBuffer getIndicesForFillRec() {
        ShortBuffer indices;
        indices = ByteBuffer.allocateDirect(6 * CONST.SHORT_SIZE)
                .order(ByteOrder.nativeOrder()).asShortBuffer();

        indices.put((short) 0).put((short) 1).put((short) 2)
                .put((short) 0).put((short) 2).put((short) 3);

        return indices;

    }


    @Override
    public void onUpdate() {

    }
/*
    public void onUpdate2() {

        float decalage = 0.2f;
        float elapsedTime = SystemClock.elapsedRealtime() - startTime;

        if (elapsedTime > 1000) {
            startTime = SystemClock.elapsedRealtime();

            if (firstFrame) {
                this.mVertices.get(0).u = 0;
                this.mVertices.get(1).u = 0;
                this.mVertices.get(2).u = decalage;
                this.mVertices.get(3).u = decalage;
                firstFrame = false;
            } else {


                this.mVertices.get(0).u += decalage;
                this.mVertices.get(1).u += decalage;
                this.mVertices.get(2).u += decalage;
                this.mVertices.get(3).u += decalage;
            }
            //Si au prochain dcale on dépasse, on revient
            // sur la first frame
            if (this.mVertices.get(3).u + decalage > 1) {

                this.firstFrame = true;

            }

        }
    }
*/

    /*
        public void setHeight(float h) {
            this.height = h;
            //updateVertices();
            if (this.canCollide) {
                this.mCollisionBox.update();
            }
        }

        public float getHeight() {
            return this.height;
        }

        public float getWidth() {
            return this.width;
        }

        public void setWidth(float w) {
            this.width = w;
            //updateVertices();
            if (this.canCollide) {
                this.mCollisionBox.update();
            }
        }
    */
    private void updateVertices() {

        // comme le 0,0 est au milieu on divise par 2

		/*
        float w = (float) width / 2;
		float h = (float) height / 2;

		this.mVertices.get(0).x = -w;
		this.mVertices.get(0).y = h;

		this.mVertices.get(1).x = -w;
		this.mVertices.get(1).y = -h;

		this.mVertices.get(2).x = w;
		this.mVertices.get(2).y = -h;

		this.mVertices.get(3).x = w;
		this.mVertices.get(3).y = h;
*/
    }


}
