package com.rdupuis.gamefactory.components;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView {

	public float touchX = 0;
	public float touchY = 0;

	public float startX = 0;
	public float startY = 0;

	public float histoX = 0;
	public float histoY = 0;

	public boolean touched = false;

	public float lastTouch = 0.f;

	public static enum ScreenEvent {
		SCROLL_H_RIGHT, SCROLL_H_LEFT, SCROLL_V_UP, SCROLL_V_DOWN, SHORT_PRESS, LONG_PRESS, UNKNOWN
	};

	private ScreenEvent event;

	public MySurfaceView(Context context) {
		super(context);
		this.event = ScreenEvent.UNKNOWN;
		// TODO Auto-generated constructor stub
	}

	public float getLastTouchTime() {
		return this.lastTouch;
	}

	private void setLastTouchTime(float time) {
		this.lastTouch = time;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_UP:
			touched = false;
			this.histoX = this.touchX;
			this.histoY = this.touchY;
			this.event = ScreenEvent.UNKNOWN;
			
			break;

		case MotionEvent.ACTION_DOWN:
			touched = true;
			
			//on vient de toucher l'écran en fonction du temps resté sur l'écran
			// par défaut on considère que l'on est dans le cas d'un SHORT_PRESS
			
			this.event = ScreenEvent.SHORT_PRESS;
			this.setLastTouchTime(SystemClock.elapsedRealtime());

			this.startX = e.getX();
			this.startY = e.getY();

			this.touchX = e.getX();
			this.touchY = e.getY();

			break;

		case MotionEvent.ACTION_MOVE:
			// touched = true;
			this.touchX = e.getX();
			this.touchY = e.getY();

			float distanceX = this.startX - this.touchX;
			float distanceY = this.startY - this.touchY;

		
			
			float duree = SystemClock.elapsedRealtime()
					- this.getLastTouchTime();

			
			// si cela fait plus de 500 ms que l'on a appuyé sur l'écran
			// on considère que l'on est dans le cas d'un LONG_PRESS
			if (duree > 50) {
				this.event = ScreenEvent.LONG_PRESS;
			}
			

			if (this.event == ScreenEvent.UNKNOWN) {

				if (distanceX < 0 && distanceX < -200) {
					this.event = ScreenEvent.SCROLL_H_LEFT;

				} else {

					if (distanceX > 0 && distanceX > 200) {
						this.event = ScreenEvent.SCROLL_H_RIGHT;
					}
				}

				if (distanceY < 0 && distanceY < -200) {
					this.event = ScreenEvent.SCROLL_V_UP;

				} else {

					if (distanceY > 0 && distanceY > 200) {
						this.event = ScreenEvent.SCROLL_V_DOWN;
					}
				}

			}

			if (e.getHistorySize() > 0) {
				this.histoX = e.getHistoricalX(e.getHistorySize() - 1);
				this.histoY = e.getHistoricalY(e.getHistorySize() - 1);
			}

		}

		return true;

	}


public ScreenEvent getScreenEvent(){
	return this.event;
}


}
