package com.rdupuis.gamefactory.providers;

import java.util.ArrayList;
import java.util.HashMap;

import com.rdupuis.gamefactory.shaders.ProgramShader;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

/**
 * le shader provider va référencer les program Shader a utiliser
 * 
 * @author NC10
 * 
 */
public class ProgramShaderProvider {

	// ! activity
	public Activity mActivity;
	public Context glContext;
	public ArrayList<ProgramShader> shaderList;
	public HashMap<String, Integer> catalogShader;
	public ProgramShader mCurrentActiveShader;

	// déclaration des attributs du shader : default
	public final String DEFAULT_VSH_ATTRIB_VERTEX_COORD = "aPosition";
	public final String DEFAULT_VSH_ATTRIB_COLOR = "aColor";
	public final String DEFAULT_VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

	public final String DEFAULT_VSH_UNIFORM_MVP = "uMvp";
	public final String DEFAULT_FSH_UNIFORM_TEXTURE = "tex0";

	/***
	 * 
	 * @param activity
	 */
	public ProgramShaderProvider(Activity activity) {
		this.mActivity = activity;
		this.mCurrentActiveShader = null;

		catalogShader = new HashMap<String, Integer>();
		shaderList = new ArrayList<ProgramShader>();

	}

	/***
 * 
 */

	public void add(ProgramShader shader) {
		shader.getClass().getName();
		int newindex = catalogShader.size() + 1;
		catalogShader.put(shader.mName, newindex);
		shaderList.add(shader);
	}

	public ProgramShader getShaderByName(String shaderName) {
		ProgramShader result = null;
		if (catalogShader.get(shaderName) == null) {
			Log.e(this.getClass().getName(), "Shader " + shaderName
					+ " unknow on Catalog");
		} else {
			result = shaderList.get(catalogShader.get(shaderName) - 1);
		}
		return result;
	}

	public void use(ProgramShader shader) {

		// use program
		if (this.mCurrentActiveShader != shader
				|| this.mCurrentActiveShader == null) {

			if (this.mCurrentActiveShader != null) {
				this.mCurrentActiveShader.disableShaderVar();
			}
			GLES20.glUseProgram(shader.mGLSLProgram_location);
			this.mCurrentActiveShader = shader;

		}

		/**
		 * if (shader.mName == "default") { if
		 * (shader.getAdressOfUniform(this.DEFAULT_FSH_UNIFORM_TEXTURE) != -1) {
		 * 
		 * GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		 * GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, GLES20Renderer.mTex0);
		 * GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		 * 
		 * // on alimente la donnée UNIFORM mAdressOf_Texture0 avc un // integer
		 * 0 GLES20.glUniform1i(shader.getAdressOfUniform(this.
		 * DEFAULT_FSH_UNIFORM_TEXTURE), 0); }
		 * 
		 * }
		 */
	}

}
