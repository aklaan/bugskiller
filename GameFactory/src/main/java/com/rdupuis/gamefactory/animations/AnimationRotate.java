package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

public class AnimationRotate extends Animation {


	public AnimationRotate(GameObject parent) {
		super(parent);
this.start();
	}

	@Override
	public void start() {
		super.start();
		this.setStatus(AnimationStatus.PLAYING);
	}

	@Override
	public void play() {
    this.getAnimatedGameObject().angleRADZ += 5.5f;
    
	}

}
