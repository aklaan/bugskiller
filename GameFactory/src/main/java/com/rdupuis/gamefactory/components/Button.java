package com.rdupuis.gamefactory.components;

import android.os.SystemClock;
import android.util.Log;


import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.interfaces.Clikable;

import java.util.ArrayList;
import java.util.EventListener;


public class Button extends Rectangle2D implements Clikable {
    public enum ButtonStatus {
        UP, DOWN
    }

    public Texture textureUp;
    public Texture textureDown;
    public ButtonStatus status;
    private float lastTap;
    private float elapsedTime;
    private boolean listening;
    private boolean ON_CLICK_FIRE;
    private final float DELAY_BTWN_TAP = 200; //200ms
    private final float ON_LONG_CLICK_DELAY = 2000;

    private final ArrayList<GLButtonListener> eventListenerList = new ArrayList<GLButtonListener>();

    public Button(float x, float y, float witdth, float hight, Texture textureUp, Texture textureDown) {
        super(DrawingMode.FILL);

        this.status = ButtonStatus.UP;
        this.setCoord(x, y);
        this.setHeight(hight);
        this.setWidth(witdth);

        this.listening = false;
        this.textureUp = textureUp;
        this.textureDown = textureDown;
        this.enableColision();
        this.isStatic = false;
        this.textureEnabled = true;
    }


    public void addGLButtonListener(GLButtonListener glButtonListener) {
        this.eventListenerList.add(glButtonListener);
    }

    @Override
    public void onUpdate() {
        this.setTexture(this.textureUp);
      //  Log.e("button", "on update");
        if (SystemClock.elapsedRealtime() - this.lastTap != DELAY_BTWN_TAP) {

            if (this.isCollideWith(this.getScene().getGOManager().getGameObjectByTag(UserFinger.USER_FINGER_TAG))) {
        //        Log.e("button", "set texture down");
                this.setTexture(this.textureDown);
                this.status = ButtonStatus.DOWN;

                // si je n'étais en train d'écouler, j'initialise le compteur delai
                if (!this.listening) {
                    lastTap = SystemClock.elapsedRealtime();
                    this.elapsedTime = 0f;
                    this.ON_CLICK_FIRE = false;
                    //on commence à écouter ce que fait l'utilisateur
                    this.listening = true;
                } else {

                    // si je suis en train d'écouter, l'incrémente le compteur
                    this.elapsedTime = SystemClock.elapsedRealtime() - this.lastTap;
                }


            } else {
                this.setTexture(textureUp);
                this.status = ButtonStatus.UP;

            }
        }
        //avec les nouvelle données, je check si on vient de faire un click
        this.checkClick();
    }

    private void checkClick() {

        //si on est en train d'écouter ce que fait l'utilisateur
        if (this.listening) {
            //si l'utilisateur a levé le doigt
            if (this.status == ButtonStatus.UP) {
                //on a levé le doigt avant de délai d'un long click
                if (elapsedTime < ON_LONG_CLICK_DELAY) {
                    onClick();
                    this.listening = false;
                }
                //sinon, on arrête d'écouter
                this.listening = false;
            }

            //si l'utilisateur a toujours le doigt appuyé sur l'écran
            // et qu'il a le doit appuyé depuis le temps necessaire pour un longClick
            if ((this.status == ButtonStatus.DOWN) && elapsedTime > ON_LONG_CLICK_DELAY && !ON_CLICK_FIRE) {

                ON_CLICK_FIRE = true;
                //on appel la méthode onclick
                onLongClick();

                this.elapsedTime = 0f;
            }


        }


    }


    public void onClick() {
        for (GLButtonListener listener : eventListenerList) {
            listener.onClick();

        }
    }


    public void onLongClick() {
        for (GLButtonListener listener : eventListenerList) {
            listener.onLongClick();
        }
    }


}
