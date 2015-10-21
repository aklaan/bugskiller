package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

public class AnimationFadeOutMoveUp extends Animation {


	public AnimationFadeOutMoveUp(GameObject parent) {
		super(parent);

	}

	@Override
	public void start() {
		super.start();
		this.setSpeed(0.01f);

	}

	@Override
	public void play() {

		//Log.i("bugAlpha",String.valueOf(this.getParent().getAlpha()));
		this.getAnimatedGameObject().Y= (float) (this.getAnimatedGameObject().getCoordY() + 1);
		if (this.getAnimatedGameObject().getAlpha() > 0.001) {
			this.getAnimatedGameObject().setAlpha(
					this.getAnimatedGameObject().getAlpha() - (this.getSpeed()));
		} else {
		this.getAnimatedGameObject().setAlpha(0.0f);
		this.stop();
		
		}

	}


}
