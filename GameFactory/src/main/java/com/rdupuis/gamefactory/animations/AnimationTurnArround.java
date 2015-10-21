package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

public class AnimationTurnArround extends Animation {

	GameObject mCible;
	float distance;
	float angle;

	public AnimationTurnArround(GameObject parent, GameObject cible, float distance) {
		super(parent);
		this.mCible = cible;
		this.angle = 0.0f;
		this.setSpeed(5.0f);
		this.distance = distance;
	}

	@Override
	public void start() {
		super.start();

	}

	@Override
	public void play() {

		if (this.mCible != null) {
			this.angle += 0.05f;
			this.getAnimatedGameObject().X = mCible.X
					+ (float) (Math.cos(this.angle * this.getSpeed()) * this.distance);
			this.getAnimatedGameObject().Y = mCible.Y
					+ (float) (Math.sin(this.angle * this.getSpeed()) * this.distance);

		}

	}

}
