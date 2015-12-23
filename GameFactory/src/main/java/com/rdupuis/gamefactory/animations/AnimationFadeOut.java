package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.AbstractGameObject;

public class AnimationFadeOut extends Animation {

    public AnimationFadeOut(AbstractGameObject animatedGameObject) {
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

        if (this.getAnimatedGameObject().getAlpha() > 0.001) {
            this.getAnimatedGameObject().setAlpha(
                    this.getAnimatedGameObject().getAlpha() - (this.getSpeed()));
        } else {
            this.getAnimatedGameObject().setAlpha(1.0f);
            this.stop();

        }

    }


}
