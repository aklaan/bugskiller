package com.rdupuis.gamefactory.animations;


import java.util.ArrayList;

/**
 * l'animationManager exécute les animations d'une scène
 * Created by rodol on 16/10/2015.
 */
public class AnimationManager {

    private final ArrayList<AnimationManagerListener> mEventListenerList = new ArrayList<AnimationManagerListener>();
    private Animation.AnimationStatus mStatus;
    private ArrayList<Animation> mAnimationList;

    public AnimationManager() {
        this.mAnimationList = new ArrayList<Animation>();
        this.mStatus = Animation.AnimationStatus.STOPPED;
    }

    public ArrayList<Animation> getAnimationList() {
        return this.mAnimationList;
    }


    public void addListener(AnimationManagerListener listener) {
        this.mEventListenerList.add(listener);
    }


    /***********************************************
     * Traiter l'animation si elle existe
     **********************************************/
    public void playAnimations() {
        // -----------------------------------------
        // traiter les animations
        // --------------------------------------------------------
        for (Animation animation : this.getAnimationList()) {

            if (animation.getStatus() == Animation.AnimationStatus.PLAYING)

                animation.play();
            // traiter les actions suplémentaires lors de la lecture
            //   onAnimationPlay();


            if (animation.getStatus() == Animation.AnimationStatus.STOPPED) {
                this.getAnimationList().remove(animation);

                // this.setAnimation(null);
                // traiter les actions suplémentaires a la fin de la lecture
                //     onAnimationStop();
            }

        }
        updateEvent();

    }

    private void updateEvent() {
        if (playInProgress()) {
            if (this.mStatus != Animation.AnimationStatus.PLAYING) {
                this.mStatus = Animation.AnimationStatus.PLAYING;
                this.onStartPlaying();
            }
        } else if (this.mStatus != Animation.AnimationStatus.STOPPED) {
            this.mStatus = Animation.AnimationStatus.STOPPED;
            this.onStopPlaying();
        }
    }


    public void onStartPlaying() {
        for (AnimationManagerListener listener : this.mEventListenerList) {
            listener.onStartPlaying();

        }
    }


    public void onStopPlaying() {
        for (AnimationManagerListener listener : this.mEventListenerList) {
            listener.onStopPlaying();

        }
    }


    /**
     * Si au moins une animation est en cours, on renvoi Vrai
     *
     * @return
     */
    public boolean playInProgress() {
        for (Animation animation : this.getAnimationList()) {
            if (animation.getStatus() == Animation.AnimationStatus.PLAYING)
                return true;
        }
        return false;
    }

    public void addAnimation(Animation animation) {
        boolean exist = false;
        //on va regarder s'il n'existe pas déjà une animation pour l'objet
        for (Animation animation1 : this.getAnimationList()) {
            if (animation.getAnimatedGameObject() == animation1.getAnimatedGameObject()) {
                if (animation.getClass() == animation1.getClass()) {
                    exist = true;
                }
            }

        }

        if (!exist) {
            this.getAnimationList().add(animation);
            //on vient d'ajouter une animation, on Start
            this.onStartPlaying();
        }

    }

}


