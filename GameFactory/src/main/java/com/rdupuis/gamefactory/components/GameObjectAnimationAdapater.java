package com.rdupuis.gamefactory.components;

import com.rdupuis.gamefactory.animations.Animation;

import java.util.ArrayList;

/**
 * Created by rodol on 19/10/2015.
 */
public class GameObjectAnimationAdapater extends GameObject {
    private GameObject mGameObject;
    private Animation animation;

    public GameObjectAnimationAdapater(GameObject gameObject) {
        this.mGameObject = gameObject;
    }


    public Animation getAnimation() {
        return this.animation;
    }

    public void setAnimationList(Animation animation) {
        this.animation = animation;
    }


}



