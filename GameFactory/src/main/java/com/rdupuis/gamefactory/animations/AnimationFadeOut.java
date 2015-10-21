package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

public class AnimationFadeOut extends Animation {


    public AnimationFadeOut(GameObject animatedGameObject) {
        super(animatedGameObject);
        start();
    }

    @Override
    public void start() {
        super.start();
        this.setStatus(AnimationStatus.PLAYING);
        this.setSpeed(0.01f);

    }

    @Override
    public void play() {

        //Log.i("bugAlpha",String.valueOf(this.getParent().getAlpha()));

        if (this.getAnimatedGameObject().getAlpha() > 0.001) {
            this.getAnimatedGameObject().setAlpha(
                    this.getAnimatedGameObject().getAlpha() - (this.getSpeed()));
        } else {
            this.getAnimatedGameObject().setAlpha(0.0f);
            this.stop();

        }

    }


}
