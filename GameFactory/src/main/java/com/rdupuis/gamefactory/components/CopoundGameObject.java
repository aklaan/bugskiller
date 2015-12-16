package com.rdupuis.gamefactory.components;

import java.util.ArrayList;

/**
 * Created by rodol on 14/12/2015.
 */
public class CopoundGameObject extends GameObject {
    public ArrayList<GameObject> getGameObjectList() {
        return mGameObjectList;
    }

    public void setGameObjectList(ArrayList<GameObject> mGameObjectList) {
        this.mGameObjectList = mGameObjectList;
    }

    private ArrayList<GameObject> mGameObjectList;

    public CopoundGameObject() {

        this.setGameObjectList(new ArrayList<GameObject>());
    }

    public void update() {
        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.update();
        }
    }

    public void draw() {
        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.draw();
        }
    }
}
