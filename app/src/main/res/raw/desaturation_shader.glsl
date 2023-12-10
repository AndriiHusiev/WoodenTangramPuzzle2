precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

void main()
{
    vec4 this_colour = texture2D( u_TextureUnit, v_TextureCoordinates );
    float new_colour = (this_colour.r + this_colour.g + this_colour.b) / 3.0;
    gl_FragColor = vec4(vec3(new_colour), this_colour.a);
}