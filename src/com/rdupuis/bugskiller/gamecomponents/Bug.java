package com.rdupuis.bugskiller.gamecomponents;

import android.util.Log;

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
		
		
	}

	@Override
	public void onUpdate() {
		
		if (this.isAlive()) {
			
			if (this.isCollideWith(this.getScene().getUserFinger()))
					 {
				this.setTexture(this.texture_dead);
				this.bugLifeState = LifeState.DEAD;
			
			}

		}
	}

	public Boolean isAlive() {
		return (this.bugLifeState == LifeState.ALIVE);

	}

}
