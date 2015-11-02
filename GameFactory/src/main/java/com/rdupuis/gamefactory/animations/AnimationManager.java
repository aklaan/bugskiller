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

    /**
     * Ajout d'un listener
     * @param listener
     */
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

    /**
     * mise à jour du statut du manager.
     */
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

    /**
     * actions à réaliser lorsque le manager commence à jouer
     * -> envoi d'un signal aux listener
     */
    public void onStartPlaying() {
        for (AnimationManagerListener listener : this.mEventListenerList) {
            listener.onStartPlaying();

        }
    }

    /**
     * actions à réaliser lorsque le manager s'arrête de jouer
     * -> envoi d'un signal aux listener
     */

    public void onStopPlaying() {
        for (AnimationManagerListener listener : this.mEventListenerList) {
            listener.onStopPlaying();

        }
    }


    /**
     * permet de savoir si des animations sont en cours.
     * Si au moins une animation est en cours, on renvoi vrai
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

    /**
     * Permet d'ajouter une animation à jouer
     * @param animation
     */
    public void addAnimation(Animation animation) {
        //si l'animation que l'on souhaite ajouter n'est pas déja référencée
        //dans la liste des animations à jouer, on peu l'ajouter, sinon, on ne fait rien
        if (!isAnimationLoaded(animation)) {
            this.getAnimationList().add(animation);
            //on vient d'ajouter une animation, on démarre le manager "player"
            this.onStartPlaying();
        }

    }

    /**
     * Permet de savoir si une animation est déjà présente dans la liste des animations jouée en cours
     * @param animation
     * @return
     */
    public boolean isAnimationLoaded(Animation animation) {
        boolean result = false;
        //on va regarder s'il n'existe pas déjà une animation pour l'objet
        for (Animation existingAnimation : this.getAnimationList()) {
            //l'animation à ajouter porte le même objet qu'une aniamtion déja existante
            if (animation.getAnimatedGameObject() == existingAnimation.getAnimatedGameObject()) {
                //le type de l'animation à ajouter est le même que celui qui est porté par l'objet
                if (animation.getClass() == existingAnimation.getClass()) {
                    result = true;
                }
            }
        }
        return result;


    }


}


