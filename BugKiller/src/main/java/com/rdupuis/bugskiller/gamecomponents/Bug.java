package com.rdupuis.bugskiller.gamecomponents;


import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.rdupuis.gamefactory.animations.Animation;
import com.rdupuis.gamefactory.animations.AnimationFadeOut;
import com.rdupuis.gamefactory.animations.AnimationFadeOutMoveUp;
import com.rdupuis.gamefactory.components.GameObjectAnimationAdapater;
import com.rdupuis.gamefactory.components.Texture;
import com.rdupuis.gamefactory.components.Trajectory;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class Bug extends Rectangle2D {

    public static enum LifeState {
        ALIVE, DEAD
    }

    ;

    public Texture texture_alive;
    public Texture texture_dead;
    LifeState bugLifeState;
    Animation animFadeOut;
    Animation animFadeOutMoveUP;
    float changeTrajectoryDelai;
    float lastTrajectoryChange;
    float speedX;
    float speedY;

    public Bug(Texture texAlive, Texture texDead) {
        super(DrawingMode.FILL);

        // Par défaut un insecte est vivant
        this.bugLifeState = LifeState.ALIVE;

        //par defaut Bug change de trajectoire toutes les 2 secondes
        // et se déplace de 2 pixels
        changeTrajectoryDelai = 3000;
        lastTrajectoryChange = 0;
        this.speedX = 5;
        this.speedY = 5;
        //texture
        this.texture_alive = texAlive;
        this.texture_dead = texDead;
        this.setTexture(texture_alive);

        //si on souhaite activer la gestion des collisions
        this.enableColision();
        this.isStatic = false;
        this.setTagName("bug");
        this.textureEnabled = true;
        this.setAlpha(1f);

        //this.animFadeOut = new AnimationFadeOut(this);
        this.animFadeOutMoveUP = new AnimationFadeOutMoveUp(this);
    }

    @Override
    public void onUpdate() {

        if (this.isAlive()) {

            this.setCoord(this.getCoordX() + this.speedX, this.getCoordY() + this.speedY);

            float limit_x_min =  (this.getWidth() / 2);
            float limit_x_max = this.getScene().getWidth() - (this.getWidth() / 2);
            float limit_y_min =  (this.getHeight() / 2);
            float limit_y_max = this.getScene().getHeight() - (this.getHeight() / 2);


            if (this.getCoordX() <limit_x_min || (this.getCoordX() > limit_x_max)) {
                this.speedX = -this.speedX;

            }
            if (this.getCoordY() <limit_y_min || (this.getCoordY() > limit_y_max)) {
                this.speedY = -this.speedY;
            }


            if (this.isCollideWith(this.getScene().getUserFinger())) {
                this.setTexture(this.texture_dead);
                this.bugLifeState = LifeState.DEAD;

                this.getScene().getAnimationManager().addAnimation(new AnimationFadeOut(this));

                //this.setAnimation(animFadeOut);
                //this.getAnimation().start();
                //MediaPlayer mPlayer = null;
                //mPlayer = MediaPlayer.create(this.getScene().getActivity(), R.raw.scratch);
                //mPlayer.start();


            }

        }


        if (this.getAlpha() == 0) {
            this.setAlpha(1);
            this.setTexture(this.texture_alive);
            this.bugLifeState = LifeState.ALIVE;
            //this.setAnimation(null);
        }
    }

    public Boolean isAlive() {
        return (this.bugLifeState == LifeState.ALIVE);

    }

    public Boolean isOutsideScreen() {

        Boolean cond1 = (this.getCoordX() > this.getScene().getWidth() / 2);
        Boolean cond2 = (this.getCoordX() < -this.getScene().getWidth() / 2);
        Boolean cond3 = (this.getCoordY() > this.getScene().getHeight() / 2);
        Boolean cond4 = (this.getCoordY() < -this.getScene().getHeight() / 2);
        return (cond1 || cond2 || cond3 || cond4);

    }

}
