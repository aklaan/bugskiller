package com.rdupuis.bugskiller.gamecomponents;


import android.util.Log;

import com.rdupuis.gamefactory.animations.Animation;
import com.rdupuis.gamefactory.animations.AnimationFadeOut;
import com.rdupuis.gamefactory.animations.AnimationFadeOutMoveUp;
import com.rdupuis.gamefactory.components.Texture;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class Bug extends Rectangle2D {

	public static enum LifeState {
		ALIVE, DEAD
	};

	public Texture texture_alive;
	public Texture texture_dead;
	LifeState bugLifeState;
	Animation animFadeOut;
	Animation animFadeOutMoveUP;

	public Bug(Texture texAlive, Texture texDead) {
		super(DrawingMode.FILL);

		// Par défaut un insecte est vivant
		this.bugLifeState = LifeState.ALIVE;

		this.texture_alive = texAlive;
		this.texture_dead = texDead;
		this.setTexture(texture_alive);
		//si on souhaite activer la gestion des collisions
		this.enableColission();
		this.isStatic = false;
		
		this.textureEnabled = true;
		this.setAlpha(1f);
		
		this.animFadeOut = new AnimationFadeOut(this);
		this.animFadeOutMoveUP = new AnimationFadeOutMoveUp(this);
	}

	@Override
	public void onUpdate() {
		
		if (this.isAlive()) {
			
			if (this.isCollideWith(this.getScene().getUserFinger()))
					 {
				this.setTexture(this.texture_dead);
				this.bugLifeState = LifeState.DEAD;
			
				this.setAnimation(animFadeOut);
				this.getAnimation().start();
				//MediaPlayer mPlayer = null;
				//mPlayer = MediaPlayer.create(this.getScene().getActivity(), R.raw.scratch);
				//mPlayer.start();
				
				
			}

		}
	
		
	if (this.getAlpha()==0){
		this.setAlpha(1);
		this.setTexture(this.texture_alive);
		this.bugLifeState=LifeState.ALIVE;
		this.setAnimation(null);
	}
	}

	public Boolean isAlive() {
		return (this.bugLifeState == LifeState.ALIVE);

	}

	
	
}
