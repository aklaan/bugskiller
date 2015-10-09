package com.rdupuis.gamefactory.components;

import android.os.SystemClock;


import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.interfaces.Clikable;


public abstract class Button extends Rectangle2D implements Clikable {
    public enum ButtonStatus {
        UP, DOWN
    }

    public Texture textureUp;
    public Texture textureDown;
    public ButtonStatus status;
    private float lastClick;
    private final float DELAY_BTWN_CLICK = 200; //200ms

    public Button(float x, float y, float witdth, float hight, Texture textureUp, Texture textureDown) {
        super(DrawingMode.FILL);

        this.status = ButtonStatus.UP;
        this.setCoord(x, y);
        this.setHeight(hight);
        this.setWidth(witdth);

        this.textureUp = textureUp;
        this.textureDown = textureDown;
        this.enableColission();
        this.isStatic = false;
        this.textureEnabled = true;
    }

    @Override
    public void onUpdate() {
        if (SystemClock.elapsedRealtime() - this.lastClick > DELAY_BTWN_CLICK) {

            if (this.isCollideWith(this.getScene().getGameObjectByTag(UserFinger.USER_FINGER_TAG))) {
                this.setTexture(this.textureDown);
                this.status = ButtonStatus.DOWN;
                lastClick = SystemClock.elapsedRealtime();
                this.onClick();
            } else {
                this.setTexture(textureUp);
                this.status = ButtonStatus.UP;
            }
        }
    }

    public void onClick() {

    }
}
