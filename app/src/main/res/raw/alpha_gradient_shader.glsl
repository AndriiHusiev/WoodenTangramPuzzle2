precision mediump float;

uniform sampler2D u_TextureUnit;
uniform vec2 u_resolution;
uniform float u_offsetFromTop;
uniform float u_gradientHeight;
varying vec2 v_TextureCoordinates;

void main()
{
    vec4 this_colour = texture2D( u_TextureUnit, v_TextureCoordinates );
    float yPos = gl_FragCoord.y / u_resolution.y;
    float alpha = smoothstep(u_gradientHeight, u_offsetFromTop, yPos);
    gl_FragColor = vec4(this_colour.rgb, 1.0-alpha);
}