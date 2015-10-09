package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

public class AnimationRotate extends Animation {


	public AnimationRotate(GameObject parent) {
		super(parent);

	}

	@Override
	public void start() {
		super.start();
		
	}

	@Override
	public void play() {
    this.getParent().angleRADZ += 5.5f;
    
	}

}
