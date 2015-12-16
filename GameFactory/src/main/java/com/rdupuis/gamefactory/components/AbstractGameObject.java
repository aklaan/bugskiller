package com.rdupuis.gamefactory.components;

import java.util.ArrayList;

/**
 * Created by rodol on 15/12/2015.
 */
public abstract class AbstractGameObject {

    public abstract void update();
    public abstract void draw();
    public abstract ArrayList<Vertex> getVertices();
    public abstract int getGlVBoId();
}
