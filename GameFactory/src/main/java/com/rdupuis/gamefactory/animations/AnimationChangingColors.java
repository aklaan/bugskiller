package com.rdupuis.gamefactory.animations;

import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.Vertex;

public class AnimationChangingColors extends Animation {
    private boolean P1 = true;

    private boolean P2, P3, P4, P5, P6 = false;


    public AnimationChangingColors(GameObject animatedGameObject) {
        super(animatedGameObject);
        start();
    }

    @Override
    public void start() {
        super.start();
        this.setStatus(AnimationStatus.PLAYING);
        this.setSpeed(0.01f);

    }

    @Override
    public void play() {

        Vertex vertex = new Vertex();
        for (int i = 0; i < this.getAnimatedGameObject().getVertices().size(); i++) {

            vertex = this.getAnimatedGameObject().getVertices().get(i);


            if (P1 || P6) {
                vertex.r += 0.01F;
            }
            if (vertex.r > 1) {
                P1 = false;
                P2 = true;
            }

            if (P2) {
                vertex.g += 0.01F;
            }
            if (vertex.g > 1) {
                P2 = false;
                P3 = true;
            }

            if (P3) {
                vertex.r -= 0.01f;
            }

            if (P3 && vertex.r < 0) {
                P3 = false;
                P4 = true;
            }

            if (P4) {
                vertex.b += 0.01f;
            }

            if (P4 && vertex.b > 1) {
                P4 = false;
                P5 = true;
            }

            if (P5) {
                vertex.g -= 0.01f;
            }

            if (P5 && vertex.g < 0) {
                P5 = false;
                P6 = true;
            }

            if (P6 && vertex.r > 1) {
                vertex.r += 0.01f;
            }

            if (vertex.r > 1 && vertex.g > 1 && vertex.b > 1) {
                P1 = true;
                P2 = P3 = P4 = P5 = P6 = false;
                vertex.r = vertex.g = vertex.b = 0f;
            }

        }
    }


}
