package com.rdupuis.bugskiller.scenes;

import javax.microedition.khronos.opengles.GL10;

import com.rdupuis.bugskiller.R;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.OpenGLActivity;
import com.rdupuis.gamefactory.components.Scene;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.shaders.ProgramShader_simple;

import android.os.SystemClock;

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
		background.setCoord((float) this.getWidth() / 2,
				(float) this.getHeight() / 2);
		background.sethight((float) this.getHeight());
		background.setWidth((float) this.getWidth());
		;
		background.setTagName(R.string.background);
		this.getBitmapProvider().linkTexture(R.string.textureisoland,
				background);
		this.addToScene(background);

	}

	@Override
	public void initProgramShader() {
		this.getProgramShaderProvider().catalogShader.clear();
		this.getProgramShaderProvider().shaderList.clear();
		this.getProgramShaderProvider().add(new ProgramShader_simple());

	}

	@Override
	public void loadTextures() {

		String imageFolder = this.getActivity().getString(R.string.imagesfolder);
		this.getBitmapProvider().add(imageFolder,R.string.textureisoland);
		this.getBitmapProvider().add(imageFolder,R.string.texturespyro);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		super.onDrawFrame(gl);

		

	}
}
