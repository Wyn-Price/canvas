#include canvas:shaders/api/context.glsl

/******************************************************
  canvas:shaders/internal/fog.glsl
******************************************************/

#define  _CV_FOG_LINEAR 9729
#define  _CV_FOG_EXP    2048
#define  _CV_FOG_EXP2   2049

#define _CV_SUBTLE_FOG FALSE

uniform int _cvu_fogMode;

/**
 * Linear fog.  Is an inverse factor - 0 means full fog.
 */
float _cv_linearFogFactor() {
	float fogFactor = (gl_Fog.end - gl_FogFragCoord) * gl_Fog.scale;
	return clamp( fogFactor, 0.0, 1.0 );
}

/**
 * Exponential fog.  Is really an inverse factor - 0 means full fog.
 */
float _cv_expFogFactor() {
	float f = gl_FogFragCoord * gl_Fog.density;
    float fogFactor = _cvu_fogMode == _CV_FOG_EXP ? exp(f) : exp(f * f);
    return clamp( 1.0 / fogFactor, 0.0, 1.0 );
}

/**
 * Returns either linear or exponential fog depending on current uniform value.
 */
float _cv_fogFactor() {
	return _cvu_fogMode == _CV_FOG_LINEAR ? _cv_linearFogFactor() : _cv_expFogFactor();
}

vec4 _cv_fog(vec4 diffuseColor) {
#if CONTEXT_IS_GUI
	return diffuseColor;
#elif _CV_SUBTLE_FOG
	float f = 1.0 - _cv_fogFactor();
	f *= f;
	return mix(vec4(gl_Fog.color.rgb, diffuseColor.a), diffuseColor, 1.0 - f);
#else
	return mix(vec4(gl_Fog.color.rgb, diffuseColor.a), diffuseColor, _cv_fogFactor());
#endif
}
