package com.rdupuis.gamefactory.utils;

import java.util.ArrayList;

import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class ArrayGameObject {

	public ArrayGameObject() {

	}

	public static ArrayList<GameObject> make(float sceneWidth,
			float scenehight, int dimX, int dimY, Rectangle2D gameObject,
			float margin) {

		ArrayList<GameObject> result = new ArrayList<GameObject>();

		float taillex = (sceneWidth - ((dimX + 1) * margin)) / dimX;
		float tailley = (scenehight - ((dimY + 1) * margin)) / dimY;

		float x = 0;
		float y = 0;
		y = margin + (tailley / 2);
		for (int i = 1; i <= dimY; i++) {

			x = margin + (taillex / 2);
			for (int j = 0; j < dimX; j++) {

				Rectangle2D go = new Rectangle2D(DrawingMode.FILL);
				try {
					go = (Rectangle2D) gameObject.clone();

					go.setCoord(x, y);
					go.setHeight(tailley);
					go.setWidth(taillex);
					result.add(go);

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				x += (margin + taillex);
			}
			y += (margin + tailley);

		}
		return result;
	}

}
