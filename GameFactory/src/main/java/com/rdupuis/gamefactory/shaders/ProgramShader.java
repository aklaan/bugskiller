package com.rdupuis.gamefactory.shaders;

import java.io.IOException;
import java.nio.FloatBuffer;
import com.rdupuis.gamefactory.components.Vertex;

import android.opengl.GLES20;
import android.util.Log;

public class ProgramShader {

	public String mName;

	// ! GLES20 Program
	public int mGLSLProgram_location;

	// ! Vertex shader
	public String vertexShaderSource;
	public int VertexShader_location;

	// Fragment shader
	public String fragmentShaderSource;
	public int FragmentShader_location;

	// déclaration des attributs spécifiques au shader
	public String attrib_vertex_coord_name;
	public int attrib_vertex_coord_location;

	public String attrib_color_name;
	public int attrib_color_location;

	public String attrib_texture_coord_name;
	public int attrib_texture_coord_location;

	public String uniform_mvp_name;
	public int uniform_mvp_location;

	public String uniform_texture_buffer_name;
	public int uniform_texture_location;

	public String uniform_alpha_name;
	public int uniform_alpha_location;

	
	
	public ProgramShader() {
		mGLSLProgram_location = 0;
		VertexShader_location = 0;
		FragmentShader_location = 0;
		this.attrib_vertex_coord_location = -1;
		this.attrib_color_location = -1;
		this.attrib_texture_coord_location = -1;
		this.uniform_mvp_location = -1;
		this.uniform_texture_location = -1;
		this.uniform_alpha_location = -1;
		this.initCode();
		this.make();
	}

	public void make() {
		mGLSLProgram_location = GLES20.glCreateProgram();
		this.loadShaders(vertexShaderSource, fragmentShaderSource);
		this.initLocations();
	}

	public void initLocations() {

	}

	public void initCode() {

	}

	public void delete() {
		// delete Vertex shader
		if (VertexShader_location != 0) {
			GLES20.glDeleteShader(VertexShader_location);
		}
		// delete Fragment shader
		if (FragmentShader_location != 0) {
			GLES20.glDeleteShader(FragmentShader_location);
		}
		// delete a GLES20 program
		if (mGLSLProgram_location != 0) {
			GLES20.glDeleteProgram(mGLSLProgram_location);
		}
	}

	/**
	 * 
	 * @param vertexFilename
	 * @param fragmentFilename
	 * @return
	 */
	public boolean loadShaders(String vertexCode, String fragmentCode) {
		if (this.mGLSLProgram_location == 0) {
			Log.e(this.getClass().getName(), "No GLSL Program created!");
			return false;
		}

		this.FragmentShader_location = this.loadShader(
				this.fragmentShaderSource, GLES20.GL_FRAGMENT_SHADER);

		this.VertexShader_location = this.loadShader(this.vertexShaderSource,
				GLES20.GL_VERTEX_SHADER);

		// if one of shader cannot be read return false
		if (this.FragmentShader_location == 0
				|| this.VertexShader_location == 0) {
			Log.e(this.getClass().getName(), "Shader doesn' compile");
			return false;
		}

		GLES20.glAttachShader(this.mGLSLProgram_location,
				this.VertexShader_location);
		
		GLES20.glAttachShader(this.mGLSLProgram_location,
				this.FragmentShader_location);
		link();
		return true;
	}

	/**
	 * 
	 * /* load a Vertex or Fragment shader
	 * 
	 * @throws IOException
	 */
	private int loadShader(String source, int shaderType) {

		int shader = GLES20.glCreateShader(shaderType);

		if (shader != 0) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			int[] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {
				Log.e(this.getClass().getName(), "Could not compile shader "
						+ shaderType + ":");
				Log.e(this.getClass().getName(),
						GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		Log.i(this.getClass().getName(), this.mName + " : " + shaderType
				+ " shader compiled");
		return shader;
	}

	public boolean link() {
		if (this.mGLSLProgram_location == 0) {
			Log.e(this.getClass().getName(),
					"Please create a GL program before Link shaders!");
			return false;
		}

		GLES20.glLinkProgram(this.mGLSLProgram_location);

		int[] linkStatus = new int[1];

		GLES20.glGetProgramiv(this.mGLSLProgram_location,
				GLES20.GL_LINK_STATUS, linkStatus, 0);

		if (linkStatus[0] != GLES20.GL_TRUE) {
			Log.e(this.getClass().getName(), "Could not link program: ");
			Log.e(this.getClass().getName(),
					"logs:"
							+ GLES20.glGetProgramInfoLog(this.mGLSLProgram_location));
			GLES20.glDeleteProgram(this.mGLSLProgram_location);
			this.mGLSLProgram_location = 0;
			return false;
		}

		Log.i("Shader.link()", "Shader linkded");
		return true;
	}

	public void setVerticesCoord(FloatBuffer fb) {
		GLES20.glVertexAttribPointer(this.attrib_vertex_coord_location, 3,
				GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES, fb);

	}

	public void setTextureCoord(FloatBuffer fb) {
		GLES20.glVertexAttribPointer(this.attrib_texture_coord_location, 2,
				GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, fb);

	}

	public void enableShaderVar() {

	}

	public void disableShaderVar() {

	}
}
