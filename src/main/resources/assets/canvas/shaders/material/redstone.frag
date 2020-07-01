#include canvas:shaders/api/fragment.glsl
#include canvas:shaders/lib/math.glsl

/******************************************************
  canvas:shaders/material/redstone.frag
******************************************************/

void cv_startFragment(inout cv_FragmentData fragData) {
	bool lit = (fragData.spriteColor.r - fragData.spriteColor.b) > 0.3f;
	fragData.emissivity = lit ? fragData.spriteColor.r * fragData.spriteColor.r : 0.0;
	fragData.diffuse = !lit;
	fragData.ao = !lit;
}
