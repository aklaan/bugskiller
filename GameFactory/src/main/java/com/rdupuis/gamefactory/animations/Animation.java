package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;

import android.os.SystemClock;

public abstract class Animation implements Cloneable {

	float startTime;
	float endTime;
	
	public static  enum playingMode {
		NONE, ONCE, REPEAT_N, LOOP
	};

	public static  enum AnimationStatus {
		PLAYING, STOPPED
	};

	private GameObject parent;
	private AnimationStatus status;

	
	/****************************************************************
	 * getters / setters 
	 * 
	 *********************************************************/
	
	public GameObject getParent(){
		return this.parent;
		
	}
	
	public void setParent(GameObject gameObject){
		this.parent = gameObject;
		
	}
	
	
	public AnimationStatus getStatus(){
		return this.status;
		
	}
	
	public void setStatus(AnimationStatus status){
		this.status = status;
		
	}
	
	//constructeur
	public Animation(GameObject parent) {
		this.parent = parent;

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

	
	public Animation clone() throws CloneNotSupportedException{
		return (Animation)super.clone();
	
	}
}
