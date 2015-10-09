package com.rdupuis.gamefactory.shaders;


import android.opengl.GLES20;

public class ProgramShader_forLines extends ProgramShader {
	
	// déclaration des attributs spécifiques au shader
	public final String VSH_ATTRIB_VERTEX_COORD = "aPosition";
	public final String VSH_ATTRIB_COLOR = "aColor";
	public final String VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

	// déclaration des uniforms spécifiques au shader
	public final String VSH_UNIFORM_MVP = "uMvp";
	

	
	public ProgramShader_forLines() {
		super();

	}

@Override	
	public void initCode() {
		this.mName = "forLines";
		this.attrib_vertex_coord_name= this.VSH_ATTRIB_VERTEX_COORD;
		this.attrib_color_name = this.VSH_ATTRIB_COLOR;
		this.uniform_mvp_name = this.VSH_UNIFORM_MVP;
	
		
		
		this.fragmentShaderSource = 
				  "#ifdef GL_ES \n"
				+ " precision highp float; \n" 
				+ " #endif \n"
 
				+ " varying vec2 vTexCoord; "
				+ " varying vec4 vColor;" 
				+ " varying vec3 pos;" 
				+ " void main() {"
			 //   + "    gl_FragColor =  vColor;"
			    + "    gl_FragColor =  vec4(1.,1.,1.,0.5);"

			    + "}";

		this.vertexShaderSource =
				  "uniform mat4 "	+ this.VSH_UNIFORM_MVP + ";" 
				+ "attribute vec3 " + this.VSH_ATTRIB_VERTEX_COORD + ";"
				+ "attribute vec4 " + this.VSH_ATTRIB_COLOR + ";"
				+ "varying vec4 vColor;" 
				
				+ "varying vec3 pos;" 
				+ "void main() {"
				// on calcule la position du point via la matrice de projection
				+ " pos = aPosition;"
				+ " vec4 position = uMvp * vec4(aPosition.xyz, 1.);"
				// vec4 position = vec4(aPosition.xyz, 1.);
				+ " vColor = aColor;" 
				+ " gl_PointSize = 1.;"
				// cette commande doit toujours �tre la derni�re du vertex shader.
				+ "	gl_Position =  position;"
				+ "}";
		
	}
	
@Override
	public void initLocations() {
		// les attribs
		this.attrib_vertex_coord_location = GLES20.glGetAttribLocation(
				mGLSLProgram_location, this.VSH_ATTRIB_VERTEX_COORD);
		this.attrib_color_location = GLES20.glGetAttribLocation(
				mGLSLProgram_location, this.VSH_ATTRIB_COLOR);
		

		// les Uniforms
		this.uniform_mvp_location = GLES20.glGetUniformLocation(
				this.mGLSLProgram_location, this.VSH_UNIFORM_MVP);
		
	}

	// *******************************************************************
	// Attention : il ne faut pas rendre enable un attribut non valoris�
	// sinon c'est ecran noir !
	public void enableShaderVar() {
		GLES20.glEnableVertexAttribArray(this.attrib_vertex_coord_location);
		//GLES20.glEnableVertexAttribArray(this.attrib_color_location);
		
	}

	// **************************************************************************
	public void disableShaderVar() {
		GLES20.glDisableVertexAttribArray(this.attrib_vertex_coord_location);
		GLES20.glDisableVertexAttribArray(this.attrib_color_location);
		GLES20.glDisableVertexAttribArray(this.uniform_mvp_location);
		

	}






}
