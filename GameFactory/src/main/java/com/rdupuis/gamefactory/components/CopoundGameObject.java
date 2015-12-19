package com.rdupuis.gamefactory.components;

import java.util.ArrayList;

/**
 * Created by rodol on 14/12/2015.
 */
public class CopoundGameObject extends GameObject {

    private ArrayList<GameObject> mGameObjectList;

    /****************************************************************
     * Getter & setter
     ***************************************************************/
    public ArrayList<GameObject> getGameObjectList() {
        return mGameObjectList;
    }

    public void setGameObjectList(ArrayList<GameObject> mGameObjectList) {
        this.mGameObjectList = mGameObjectList;
    }

    /******************************************************************
     * Constructeur
     ***************************************************************/
    public CopoundGameObject() {
        this.setGameObjectList(new ArrayList<GameObject>());
    }

    public void update() {
        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.update();
        }
    }


}
