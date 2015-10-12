package com.rdupuis.bugskiller.scenes;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import javax.microedition.khronos.opengles.GL10;

import com.rdupuis.bugskiller.R;
import com.rdupuis.bugskiller.gamecomponents.Bug;
import com.rdupuis.gamefactory.components.Button;
import com.rdupuis.gamefactory.components.GLButtonListener;
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

    public Scene01(OpenGLActivity activity) {
        super(activity);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void loadGameObjects() {

        Rectangle2D background = new Rectangle2D(DrawingMode.FILL);
        background.setCoord((float) this.getWidth() / 2, (float) this.getHeight() / 2);
        background.setHeight((float) this.getHeight());
        background.setWidth((float) this.getWidth());

        background.setTagName(R.string.background);
        this.getBitmapProvider().linkTexture(R.string.textureisoland, background);
        this.addToScene(background);

        Bug bug = new Bug(this.getBitmapProvider().getTexture(R.string.bugalive), this.getBitmapProvider().getTexture(R.string.bugdead));
        bug.setWidth(50);
        bug.setHeight(50);
        bug.setCoord((float) this.getWidth() / 2, (float) this.getHeight() / 2);
        this.addToScene(bug);


//Button(float x, float y, float witdth, float hight, Texture textureUp, Texture textureDown)
        Button button;
        button = new Button(50, 50, 100, 100, this.getBitmapProvider().getTexture(R.string.bugalive),
                this.getBitmapProvider().getTexture(R.string.bugdead));

        this.addToScene(button);

        GLButtonListener toto = new GLButtonListener() {
            @Override
            public void onClick() {
                Log.e("tototot", "click");

            }

            @Override
            public void onLongClick() {
                Log.e("tototot", "long click");

            }


        };

        button.addGLButtonListener(toto);
    }


    public void domess() {
        Toast.makeText(Scene01.this.getActivity(), "coucou les amis", Toast.LENGTH_SHORT).show();

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

        String imageFolder = this.getActivity().getString(R.string.imagesfolder);
        this.getBitmapProvider().add(imageFolder, R.string.textureisoland);
        this.getBitmapProvider().add(imageFolder, R.string.texturespyro);
        this.getBitmapProvider().add(imageFolder, R.string.bugalive);
        this.getBitmapProvider().add(imageFolder, R.string.bugdead);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);


    }
}
