package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

public class AnimationTurnArround extends Animation {

	GameObject mCible;
	float speed;
	float distance;
	float angle;

	public AnimationTurnArround(GameObject parent, GameObject cible, float distance) {
		super(parent);
		this.mCible = cible;
		this.angle = 0.0f;
		this.speed = 5.0f;
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
			this.getParent().X = mCible.X
					+ (float) (Math.cos(this.angle * this.speed) * this.distance);
			this.getParent().Y = mCible.Y
					+ (float) (Math.sin(this.angle * this.speed) * this.distance);

		}

	}

}
