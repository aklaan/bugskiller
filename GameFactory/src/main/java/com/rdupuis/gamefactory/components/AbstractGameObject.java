package com.rdupuis.gamefactory.components;

import java.util.ArrayList;

/**
 * Created by rodol on 15/12/2015.
 */
public abstract class AbstractGameObject {

    public abstract void update();

    public abstract void setWidth(float width);

    public abstract float getWidth();

    public abstract void setHeight(float height);

    public abstract float getHeight();

    public abstract void setScene(Scene scene);

    public abstract Scene getScene();

    public abstract void updateModelView();

    public abstract String getTagName();

    public abstract void setTagName(String name);

    public abstract void setVisibility(Boolean visibility);

    public abstract Boolean getVisibility();

    public abstract void setAlpha(float alpha);

    public abstract float getAlpha();

    //public float getAngleRADZ();

public abstract void setX(float x);
public abstract float getX();

    public abstract void setY(float y);
    public abstract float getY();

    public abstract void setZ(float z);
    public abstract float getZ();

}
