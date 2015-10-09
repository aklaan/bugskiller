package com.rdupuis.gamefactory.utils;

import java.util.ArrayList;

import com.rdupuis.gamefactory.components.CollisionBox;
import com.rdupuis.gamefactory.components.GameObject;

public class CollisionControler {

    public CollisionControler() {

    }

    public static void checkAllCollisions(ArrayList<GameObject> mGameObjectList) {

        for (GameObject gameObject : mGameObjectList) {

            // on ne contrôle les colisions que pour les objets en mouvement
            if (gameObject.isStatic == false && gameObject.canCollide) {
                //on efface la liste des objets avec lequel il était en colision
                // comme les objets on bougé, il faut tout recalculer

                gameObject.mCollideWithList.clear();

                //pour tous les objets de la scène, on vérifie s'ils rentrent en colision
                for (GameObject go : mGameObjectList) {
                    if (go != gameObject && go.canCollide) {

                        if (checkCollision(go.mCollisionBox,
                                gameObject.mCollisionBox)) {

                            //gameObject.mCollideWithList.add(go.mCollisionBox);
                            gameObject.mCollideWithList.add(go);
                        }

                    }

                }
            }

        }
    }

    static boolean checkCollision(CollisionBox a, CollisionBox b) {
        return SAT.isColide(a, b);
        /**
         * if (a == b || (a.getCoordX() >= b.getCoordX() + b.getWidth())
         * // trop à droite || (a.getCoordX() + a.getWidth() <= b.getCoordX()) //
         * trop à // gauche || (a.getCoordY() >= b.getCoordY() + b.getHeight())
         * // trop en // bas || (a.getCoordY() + a.getHeight() <= b.getCoordY())
         * // trop en ) // haut return false; else return true;
         */
    }


}
