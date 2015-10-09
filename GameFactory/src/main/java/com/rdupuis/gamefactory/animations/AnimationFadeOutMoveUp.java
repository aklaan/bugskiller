package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

public class AnimationFadeOutMoveUp extends Animation {

	private float speed;

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
		this.getParent().Y= (float) (this.getParent().getCoordY() + 1);
		if (this.getParent().getAlpha() > 0.001) {
			this.getParent().setAlpha(
					this.getParent().getAlpha() - (this.speed));
		} else {
		this.getParent().setAlpha(0.0f);
		this.stop();
		
		}

	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
