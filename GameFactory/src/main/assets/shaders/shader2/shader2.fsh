#ifdef GL_ES
precision highp float;
#endif 
uniform sampler2D tex0;
varying vec2 vTexCoord; 
varying vec4 vColor;


void main() 
{
    //on n'accède jamais au fragment shader en direct.
    // on passe par le vertex shader pour véhiculer les varibales
    // vColor vient du vertexShader
    //dans le vertexShader, vColor est alimenté avec aColor
    // qui lui même reçoit des valeurs de la part du programme Java
    
    // ce fragment shader ne fait rien de particulier.
    // il retourne la couleur de la texture 
   // gl_FragColor = texture2D(tex0, gl_PointCoord) * vColor;
    //gl_FragColor = texture2D(tex0, gl_PointCoord);
    gl_FragColor = texture2D(tex0, vTexCoord);
   // gl_FragColor =  vec4(sin(pos.x), 0.0, 0.0, 1.0);
}

