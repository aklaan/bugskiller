uniform mat4 uMvp;

attribute vec3 aPosition;
attribute vec2 aTexCoord; 
attribute vec4 aColor;

varying vec4 vColor;
varying vec2 vTexCoord;
varying vec3 pos;
void main() {
    // on calcule la position du point via la matrice de projection
    pos = aPosition;
   // vec4 position = uMvp * vec4(aPosition.xyz, 1.);
    vec4 position = vec4(aPosition.xyz, 1.);
    vColor = aColor;
    vTexCoord = aTexCoord;
   // gl_PointSize = 10.;
	
	// cette commande doit toujours être la dernière du vertex shader.
	gl_Position =  position;
	
}
