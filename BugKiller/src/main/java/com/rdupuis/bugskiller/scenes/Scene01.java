package com.rdupuis.bugskiller.scenes;

import android.os.Message;
import android.util.Log;

import com.rdupuis.bugskiller.MainActivity;
import com.rdupuis.bugskiller.R;
import com.rdupuis.bugskiller.gamecomponents.Bug;
import com.rdupuis.gamefactory.animations.AnimationFadeOut;
import com.rdupuis.gamefactory.animations.AnimationRotate;
import com.rdupuis.gamefactory.components.AbstractGameObject;
import com.rdupuis.gamefactory.components.Button;
import com.rdupuis.gamefactory.components.ButtonA;
import com.rdupuis.gamefactory.components.ButtonB;
import com.rdupuis.gamefactory.components.CopoundGameObject;
import com.rdupuis.gamefactory.components.GLButtonListener;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.OpenGLActivity;
import com.rdupuis.gamefactory.components.Scene;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.shaders.ProgramShader;
import com.rdupuis.gamefactory.shaders.ProgramShader_forLines;
import com.rdupuis.gamefactory.shaders.ProgramShader_simple;

/**
 * GLES20Renderer: the OGLES 2.0 Thread.
 */
public class Scene01 extends Scene {

    private final String TAG_BUG = "scene1:bug";
    private final String TAG_BACKGROUND = "scene1:background";
    private final String TAG_BUTTON = "scene1:button";
    public String aaa;

    public Scene01(OpenGLActivity activity) {
        super(activity);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void loadGameObjects() {

        //BACKGROUND
        Rectangle2D background = new Rectangle2D(DrawingMode.FILL);
        background.setCoord((float) this.getWidth() / 2, (float) this.getHeight() / 2);
        background.setHeight((float) this.getHeight());
        background.setWidth((float) this.getWidth());

        background.setTagName(TAG_BACKGROUND);
        background.disableColision();

        CopoundGameObject copoundGameObject = new CopoundGameObject();
        copoundGameObject.setTagName(TAG_BUG);
        //   this.getBitmapProvider().linkTexture(R.string.mountains, background);
        //   this.getBitmapProvider().linkTexture(R.string.textureisoland, background);
        //this.addToScene(background);

        //BUG
        for (int i = 1; i < 50; i++) {
            Bug bug = new Bug(this.getTexManager().getTextureById(R.string.spaceship),
                    this.getTexManager().getTextureById(R.string.bugdead));
            bug.setWidth(10);
            bug.setHeight(10);
            bug.setCoord((float) 5 * i,
                    (float) 5 * i);
            bug.setTagName(TAG_BUG + ":" + i);
            bug.setScene(this);


            //on charge les vertices de Bug dans le buffer 0 qui est dans la mémoire du GPU !!!!
            copoundGameObject.add(bug);
            //this.addToScene(bug);

        }

        this.addToScene(copoundGameObject);

        //BUTTON
        //Button(float x, float y, float witdth, float hight, Texture textureUp, Texture textureDown)

        ButtonA button = new ButtonA(450, 150, 200, 200,
                this.getTexManager().getTextureById(R.string.circle),
                this.getTexManager().getTextureById(R.string.spaceship),
                this.getTexManager().getTextureById(R.string.emptycircle));
        button.setTagName(TAG_BUTTON);
        //on place le bouton au centre de la vue
        button.setX((float) this.getWidth() / 2);
        button.setY((float) this.getHeight() / 2);
        this.addToScene(button);

        GLButtonListener toto = new GLButtonListener() {
            @Override
            public void onClick() {
                Log.e("debug", "click");
                //       GameObject bug = (GameObject) Scene01.this.getGOManager().getGameObjectByTag(TAG_BUG);
                //       Scene01.this.getAnimationManager().addAnimation(new AnimationRotate(bug));


                MainActivity toto = (MainActivity) Scene01.this.getActivity();

                toto.texte = "Click:" + String.valueOf(Math.random());

                //je crée un message vide juste pour focer l'utilisation du Handler
                // qui est géré par la MainActivity.
                //ceci me permet de pouvoir mettre à jour les éléments UI, autrement c'est impossible
                //seul le thead Maiactivity peu modifier les UI dont il a la gerstion

                Message completeMessage =
                        toto.mHandler.obtainMessage();

                //sendToTarget va actionner la fonction handleMessage du Handle géré par Mainactivity
                completeMessage.sendToTarget();


            }

            @Override
            public void onLongClick() {
                Log.e("debug", "long click");
                AbstractGameObject bug = Scene01.this.getGOManager().getGameObjectByTag(TAG_BUG);
                Scene01.this.getAnimationManager().addAnimation(new AnimationFadeOut(bug));

                MainActivity toto = (MainActivity) Scene01.this.getActivity();

                toto.texte = "Long Click:" +
                        String.valueOf(Math.random());

                //je crée un message vide juste pour focer l'utilisation du Handler
                // qui est géré par la MainActivity.
                //ceci me permet de pouvoir mettre à jour les éléments UI, autrement c'est impossible
                //seul le thead Maiactivity peu modifier les UI dont il a la gerstion

                Message completeMessage =
                        toto.mHandler.obtainMessage();
                completeMessage.sendToTarget();
            }

        };


        button.addGLButtonListener(toto);

    }


    @Override
    public void loadProgramShader() {
        this.getPSManager().catalogShader.clear();
        this.getPSManager().shaderList.clear();
        ProgramShader ps = new ProgramShader_simple();
        this.getPSManager().add(ps);
        this.getPSManager().add(new ProgramShader_forLines());

        //on défini le simple comme shader par defaut.
        this.getPSManager().setDefaultSader(ps);
        ;

    }

    @Override
    public void loadTextures() {

        this.getTexManager().add(R.string.bugalive);
        this.getTexManager().add(R.string.bugdead);
        this.getTexManager().add(R.string.circle);
        this.getTexManager().add(R.string.spaceship);
        this.getTexManager().add(R.string.emptycircle);
    }


}
