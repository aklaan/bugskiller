package com.rdupuis.gamefactory.shaders;


import android.opengl.GLES20;

public class ProgramShader_grille extends ProgramShader {

	// déclaration des attributs spécifiques au shader
	public final String VSH_ATTRIB_VERTEX_COORD = "aPosition";
	public final String VSH_ATTRIB_COLOR = "aColor";
	public final String VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

	// déclaration des uniforms spécifiques au shader
	public final String VSH_UNIFORM_MVP = "uMvp";
	public final String FSH_UNIFORM_TEXTURE = "tex0";

	public ProgramShader_grille() {
		super();
	}

	@Override
	public void initCode() {
		this.mName = "grille";
		this.attrib_vertex_coord_name = this.VSH_ATTRIB_VERTEX_COORD;
		this.attrib_color_name = this.VSH_ATTRIB_COLOR;
		this.attrib_texture_coord_name = this.VSH_ATTRIB_TEXTURE_COORD;
		this.uniform_mvp_name = this.VSH_UNIFORM_MVP;
		this.uniform_texture_buffer_name = this.FSH_UNIFORM_TEXTURE;

		this.fragmentShaderSource = "#ifdef GL_ES \n"
				+ " precision highp float; \n" + " #endif \n"
				+ " uniform sampler2D " + this.FSH_UNIFORM_TEXTURE + ";"
				+ " varying vec2 vTexCoord; " + " varying vec4 vColor;"
				+ " varying vec3 pos;"
				+ " void main() {"
				// + "    gl_FragColor =  vColor;"
				// + "    gl_FragColor = texture2D(tex0, vTexCoord);"
				+ "    gl_FragColor =  vec4(sin(pos.x), sin(pos.y), 0.0, 1.0);"
				+ "}";

		this.vertexShaderSource = "uniform mat4 " + this.VSH_UNIFORM_MVP + ";"
				+ "attribute vec3 " + this.VSH_ATTRIB_VERTEX_COORD + ";"
				+ "attribute vec2 " + this.VSH_ATTRIB_TEXTURE_COORD + ";"
				+ "attribute vec4 " + this.VSH_ATTRIB_COLOR + ";"
				+ "varying vec4 vColor;" + "varying vec2 vTexCoord;"
				+ "varying vec3 pos;" + "void main() {"
				// on calcule la position du point via la matrice de projection
				+ " pos = aPosition;"
				+ " vec4 position = uMvp * vec4(aPosition.xyz, 1.);"
				// vec4 position = vec4(aPosition.xyz, 1.);
				+ " vColor = aColor;" + " vTexCoord = aTexCoord;"
				// gl_PointSize = 10.;
				// cette commande doit toujours être la dernière du vertex
				// shader.
				+ "	gl_Position =  position;" + "}";

	}

	/**
	 * Initialisation des pointeurs de localisation des varibales
	 * propres au Shader
	 */
	@Override
	public void initLocations() {
		// les attribs
		this.attrib_vertex_coord_location = GLES20.glGetAttribLocation(
				mGLSLProgram_location, this.VSH_ATTRIB_VERTEX_COORD);
		this.attrib_color_location = GLES20.glGetAttribLocation(
				mGLSLProgram_location, this.VSH_ATTRIB_COLOR);
		this.attrib_texture_coord_location = GLES20.glGetAttribLocation(
				this.mGLSLProgram_location, this.VSH_ATTRIB_TEXTURE_COORD);

		// les Uniforms

		this.uniform_mvp_location = GLES20.glGetUniformLocation(
				this.mGLSLProgram_location, this.VSH_UNIFORM_MVP);
		this.uniform_texture_location = GLES20.glGetUniformLocation(
				this.mGLSLProgram_location, this.FSH_UNIFORM_TEXTURE);
	}

	// *******************************************************************
	//

	
	/**
	 * Activation des varibales propres au Shader
	 * ATTENTION : Il ne faut pas rendre enable un attribut non valorisé
	 *             sinon on obtient un ecran noir !	
	 */
	@Override
	public void enableShaderVar() {
		GLES20.glEnableVertexAttribArray(this.attrib_vertex_coord_location);
		// GLES20.glEnableVertexAttribArray(this.attrib_color_location);
		GLES20.glEnableVertexAttribArray(this.attrib_texture_coord_location);

		// /les uniforms ne sont pas attrib !!
		// GLES20.glEnableVertexAttribArray(this.uniform_mvp_location);
		// GLES20.glEnableVertexAttribArray(this.uniform_texture_location);
	}

	// **************************************************************************
	/**
	 * Désactivation des attributs propres au Shader
	 */
	@Override
	public void disableShaderVar() {
		GLES20.glDisableVertexAttribArray(this.attrib_vertex_coord_location);
		GLES20.glDisableVertexAttribArray(this.attrib_color_location);
		GLES20.glDisableVertexAttribArray(this.attrib_texture_coord_location);
		GLES20.glDisableVertexAttribArray(this.uniform_mvp_location);
		GLES20.glDisableVertexAttribArray(this.uniform_texture_location);

	}

}
