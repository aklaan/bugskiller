package com.rdupuis.gamefactory.utils;


import java.util.ArrayList;
import java.util.HashMap;

import com.rdupuis.gamefactory.components.CollisionBox;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.Vertex;
import com.rdupuis.gamefactory.providers.GameObjectManager;

public class ColliderManager {


    //liste des boites de collision
    private ArrayList<CollisionBox> mCollisionBoxList;

    //Liste des objets en collision
    //cette liste est réévaluée à chaque Frame
    private HashMap<GameObject, GameObject> mCollisionList;

    /**
     * Constructeur
     */
    public ColliderManager() {
        mCollisionBoxList = new ArrayList<CollisionBox>();
        mCollisionList = new HashMap<GameObject, GameObject>();
    }

    /**
     * Initialisation des boites de collision d'après
     * les objets chargés dans la scène
     *
     * @param gom
     */
    public void initBoxes(GameObjectManager gom) {
        //on vide la liste
        mCollisionBoxList.clear();

        //on crée une boite de collision pour chaque objet qui en necessitent une
        for (GameObject gameObject : gom.GOList()) {
            if (gameObject.canCollide) {
                //gameObject, default Offset X, default Offset Y
                CollisionBox box = new CollisionBox(gameObject);
                this.mCollisionBoxList.add(box);
            }
        }
    }


    private void updateWorldVertices(){
        for (CollisionBox box : this.mCollisionBoxList){
            box.updateWorldVertices();
        }
    }

    /**
     * mise à jour de la liste des objets entrant en collision
     */
    public void updateCollisionsList() {

        //on met à jour les WorldVertices
        updateWorldVertices();

        //on commence par effacer la liste
        this.mCollisionList.clear();

        //pour toutes les boites, on vérifie si elles entrent en collision
        for (CollisionBox box1 : this.mCollisionBoxList) {
            for (CollisionBox box2 : this.mCollisionBoxList) {
                //on évite que la boite se compare à elle-même
                if (box1 != box2) {
                    if (SAT.isColide(box1, box2)) {
                        this.mCollisionList.put(box1.getGameObject(), box2.getGameObject());
                    }

                }

            }
        }

    }

    public boolean isCollide(GameObject gameObjectA, GameObject gameObjectB){
        return this.mCollisionList.get(gameObjectA) == gameObjectB;

    }


}
