#ifdef GL_ES
precision highp float;
#endif 
uniform sampler2D tex0;
varying vec2 vTexCoord; 
varying vec4 vColor;


void main() 
{
    //on n'acc�de jamais au fragment shader en direct.
    // on passe par le vertex shader pour v�hiculer les varibales
    // vColor vient du vertexShader
    //dans le vertexShader, vColor est aliment� avec aColor
    // qui lui m�me re�oit des valeurs de la part du programme Java
    
    // ce fragment shader ne fait rien de particulier.
    // il retourne la couleur de la texture 
   // gl_FragColor = texture2D(tex0, gl_PointCoord) * vColor;
    //gl_FragColor = texture2D(tex0, gl_PointCoord);
    gl_FragColor = texture2D(tex0, vTexCoord);
   // gl_FragColor =  vec4(sin(pos.x), 0.0, 0.0, 1.0);
}

