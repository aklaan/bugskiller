package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

import android.os.SystemClock;

public abstract class Animation implements Cloneable {

    float startTime;
    float endTime;
    private float speed;

    public static enum playingMode {
        NONE, ONCE, REPEAT_N, LOOP
    }

    public static enum AnimationStatus {
        PLAYING, STOPPED
    }

    private GameObject animatedGameObject;
    private AnimationStatus status;


    /****************************************************************
     * getters / setters
     *********************************************************/

    public GameObject getAnimatedGameObject() {
        return this.animatedGameObject;

    }

    public void setAnimatedGameObject(GameObject gameObject) {
        this.animatedGameObject = gameObject;

    }

    public float getSpeed() {

        return speed;
    }

    public void setSpeed(float speed) {

        this.speed = speed;
    }

    public AnimationStatus getStatus() {
        return this.status;

    }

    public void setStatus(AnimationStatus status) {
        this.status = status;

    }

    //constructeur
    public Animation(GameObject animatedGameObject) {
        this.animatedGameObject = animatedGameObject;

    }

    public void start() {
        if (this.status != AnimationStatus.PLAYING) {
            startTime = SystemClock.elapsedRealtime();
            this.status = AnimationStatus.PLAYING;
        }
    }

    public void stop() {
        this.status = AnimationStatus.STOPPED;
        endTime = SystemClock.elapsedRealtime();
    }

    public void play() {

    }


    public Animation clone() throws CloneNotSupportedException {
        return (Animation) super.clone();

    }
}
