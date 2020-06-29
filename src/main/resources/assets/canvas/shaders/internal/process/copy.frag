#include canvas:shaders/internal/process/header.glsl

/******************************************************
  canvas:shaders/internal/process/copy.frag
******************************************************/
uniform sampler2D _cvu_input;
uniform int _cvu_width;
uniform int _cvu_height;

varying vec2 _cvv_texcoord;

void main() {
	vec4 color = texture2D(_cvu_input, _cvv_texcoord);
	gl_FragData[0] = vec4(color.rgb, 1.0);
}