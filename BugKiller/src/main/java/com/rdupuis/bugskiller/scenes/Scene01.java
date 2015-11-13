package com.rdupuis.bugskiller.scenes;

import android.net.Uri;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.rdupuis.bugskiller.R;
import com.rdupuis.bugskiller.gamecomponents.Bug;
import com.rdupuis.gamefactory.animations.AnimationFadeOut;
import com.rdupuis.gamefactory.animations.AnimationRotate;
import com.rdupuis.gamefactory.components.Button;
import com.rdupuis.gamefactory.components.GLButtonListener;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.OpenGLActivity;
import com.rdupuis.gamefactory.components.Scene;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.shaders.ProgramShader_forLines;
import com.rdupuis.gamefactory.shaders.ProgramShader_simple;

/**
 * GLES20Renderer: the OGLES 2.0 Thread.
 */
public class Scene01 extends Scene {

    private final String TAG_BUG = "scene1:bug";
    private final String TAG_BACKGROUND = "scene1:background";
    private final String TAG_BUTTON = "scene1:button";

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


        //   this.getBitmapProvider().linkTexture(R.string.mountains, background);
//        this.getBitmapProvider().linkTexture(R.string.textureisoland, background);
        //this.addToScene(background);

        //BUG
        for (int i = 1; i < 10; i++) {
            Bug bug = new Bug(this.getTextureProvider().getTextureById(R.string.bugalive),
                    this.getTextureProvider().getTextureById(R.string.bugdead));
            bug.setWidth(30);
            bug.setHeight(30);
            bug.setCoord((float) (this.getWidth() / 2) + i * 35, (float) (this.getHeight() / 2) + i * 35);
            bug.setTagName(TAG_BUG);

            //on charge les vertices de Bug dans le buffer 0 qui est dans la mémoire du GPU !!!!
            this.addToScene(bug);

        }
        //BUTTON
        //Button(float x, float y, float witdth, float hight, Texture textureUp, Texture textureDown)
        Button button;
        button = new Button(450, 150, 200, 100, this.getTextureProvider().getTextureById(R.string.circle),
                this.getTextureProvider().getTextureById(R.string.bugdead));
        button.setTagName(TAG_BUTTON);
        button.setCoord((float) this.getWidth() / 2, (float) this.getHeight() / 2);
        button.mCollisionBox.isVisible = true;
        this.addToScene(button);

        GLButtonListener toto = new GLButtonListener() {
            @Override
            public void onClick() {
                Log.e("debug", "click");
                GameObject bug = Scene01.this.getGameObjectByTag(TAG_BUG);
                Scene01.this.getAnimationManager().addAnimation(new AnimationRotate(bug));

            }

            @Override
            public void onLongClick() {
                Log.e("debug", "long click");
                GameObject bug = Scene01.this.getGameObjectByTag(TAG_BUG);
                Scene01.this.getAnimationManager().addAnimation(new AnimationFadeOut(bug));

            }


        };

        button.addGLButtonListener(toto);
    }


    @Override
    public void initProgramShader() {
        this.getProgramShaderProvider().catalogShader.clear();
        this.getProgramShaderProvider().shaderList.clear();
        this.getProgramShaderProvider().add(new ProgramShader_simple());
        this.getProgramShaderProvider().add(new ProgramShader_forLines());
    }

    @Override
    public void loadTextures() {

        this.getTextureProvider().add(R.string.bugalive);
        this.getTextureProvider().add(R.string.bugdead);
        this.getTextureProvider().add(R.string.circle);
    }


    @Override
    public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {
        super.onSurfaceCreated(gl2, eglConfig);

        GameObject go = this.getGameObjectByTag(this.TAG_BUG);
        this.loadVBO(go, 0);
        this.loadVBOi(go, 0);
    }
}
