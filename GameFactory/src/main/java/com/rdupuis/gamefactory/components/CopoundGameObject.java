package com.rdupuis.gamefactory.components;

import java.util.ArrayList;

/**
 * Created by rodol on 14/12/2015.
 */
public class CopoundGameObject extends AbstractGameObject {

    private ArrayList<GameObject> mGameObjectList;
    private float width;
    private float height;
    private Scene mScene;
    private Boolean mVisibility;

    @Override
    public float getX() {
        return X;
    }

    @Override
    public void setX(float x) {
        X = x;
        this.updateX();
    }

    private float X;

    @Override
    public float getY() {
        return Y;
    }

    @Override
    public void setY(float y) {
        Y = y;
    this.updateY();
    }

    private float Y;

    @Override
    public float getZ() {
        return Z;
    }

    @Override
    public void setZ(float z) {
        Z = z;
    }

    private float Z;

    public float getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float alpha) {
        float ratio = alpha/getAlpha();
        this.updateAlpha(ratio);
        this.mAlpha = alpha;
    }

    private float mAlpha;

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String mTagName) {
        this.mTagName = mTagName;
    }

    private String mTagName;

    /****************************************************************
     * Getter & setter
     ***************************************************************/
    public ArrayList<GameObject> getGameObjectList() {
        return mGameObjectList;
    }

    public void setGameObjectList(ArrayList<GameObject> mGameObjectList) {
        this.mGameObjectList = mGameObjectList;
    }

    @Override
    public float getWidth() {
        return width;
    }


    @Override
    public void setHeight(float height) {
        float ratio = height / this.getHeight();
        updateHeight(ratio);
        this.height = height;
    }


    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setWidth(float width) {
        float ratio = width / this.getWidth();
        //on applique le ratio a tous les enfants
        this.updateWidth(ratio);
        //on mémorise la nouvelle valeur
        this.width = width;
    }

    public void setScene(Scene scene) {
        this.mScene = scene;
    }

    public Scene getScene() {
        return mScene;

    }


    public void setVisibility(Boolean visibility) {
        this.mVisibility = visibility;
        //Je ne pense pas qu'il soit necessaire de gérer la visibilité
        // de tous les composant. si l'objet n'est pas visible, on ne
        // doit pas le rendre en théorie

    }


    public Boolean getVisibility() {
        return this.mVisibility;
    }

    /******************************************************************
     * Constructeur
     ***************************************************************/
    public CopoundGameObject() {
        this.setGameObjectList(new ArrayList<GameObject>());
        this.setWidth(10);
        this.setHeight(10);
        this.setVisibility(true);
        this.setAlpha(1.f);
    }


    public void update() {
        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.update();
        }
    }


    public void updateX() {
        //TODO pur chaque objet on applique le ratio necessaire

        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.setX(this.getX());
        }
    }



    public void updateY() {
        //TODO pur chaque objet on applique le ratio necessaire

        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.setY(this.getY());
        }
    }

    public void updateWidth(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire

        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.setWidth(gameObject.getWidth() * ratio);
        }
    }

    public void updateHeight(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire
        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.setHeight(gameObject.getHeight() * ratio);
        }
    }

    public void updateAlpha(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire
        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.setAlpha(gameObject.getAlpha() * ratio);
        }
    }


    public void updateModelView() {
        for (GameObject gameObject : this.getGameObjectList()) {
            gameObject.updateModelView();
        }
    }

    public void add(GameObject gameObject) {

        this.getGameObjectList().add(gameObject);


    }

}
