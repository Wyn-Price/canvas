#include canvas:shaders/internal/process/header.glsl

/******************************************************
  canvas:shaders/internal/process/bloom.frag
******************************************************/
uniform sampler2D _cvu_base;
uniform sampler2D _cvu_bloom0;
uniform sampler2D _cvu_bloom1;
uniform sampler2D _cvu_bloom2;
uniform ivec2 _cvu_size;

varying vec2 _cvv_texcoord;

vec3 saturate(vec3 x)
{
    return clamp(x, vec3(0.0), vec3(1.0));
}

vec4 cubic(float x)
{
    float x2 = x * x;
    float x3 = x2 * x;
    vec4 w;
    w.x =   -x3 + 3.0*x2 - 3.0*x + 1.0;
    w.y =  3.0*x3 - 6.0*x2       + 4.0;
    w.z = -3.0*x3 + 3.0*x2 + 3.0*x + 1.0;
    w.w =  x3;
    return w / 6.0;
}

vec4 BicubicTexture(in sampler2D tex, in vec2 coord)
{
	vec2 resolution = _cvu_size;

	coord *= resolution;

	float fx = fract(coord.x);
    float fy = fract(coord.y);
    coord.x -= fx;
    coord.y -= fy;

    fx -= 0.5;
    fy -= 0.5;

    vec4 xcubic = cubic(fx);
    vec4 ycubic = cubic(fy);

    vec4 c = vec4(coord.x - 0.5, coord.x + 1.5, coord.y - 0.5, coord.y + 1.5);
    vec4 s = vec4(xcubic.x + xcubic.y, xcubic.z + xcubic.w, ycubic.x + ycubic.y, ycubic.z + ycubic.w);
    vec4 offset = c + vec4(xcubic.y, xcubic.w, ycubic.y, ycubic.w) / s;

    vec4 sample0 = texture2D(tex, vec2(offset.x, offset.z) / resolution);
    vec4 sample1 = texture2D(tex, vec2(offset.y, offset.z) / resolution);
    vec4 sample2 = texture2D(tex, vec2(offset.x, offset.w) / resolution);
    vec4 sample3 = texture2D(tex, vec2(offset.y, offset.w) / resolution);

    float sx = s.x / (s.x + s.y);
    float sy = s.z / (s.z + s.w);

    return mix( mix(sample3, sample2, sx), mix(sample1, sample0, sx), sy);
}

vec3 ColorFetch(vec2 coord)
{
	vec4 c = BicubicTexture(_cvu_base, coord);
	return c.rgb;
}

vec3 BloomFetch(vec2 coord)
{
	vec4 c = BicubicTexture(_cvu_bloom0, coord);
 	return c.rgb;
}

vec3 Grab(vec2 coord, const float octave, const vec2 offset)
{
 	float scale = exp2(octave);

    coord /= scale;
    coord -= offset;

    return BloomFetch(coord);
}

vec2 CalcOffset(float octave)
{
    vec2 offset = vec2(0.0);

    vec2 padding = vec2(10.0) / _cvu_size; //iResolution.xy;

    offset.x = -min(1.0, floor(octave / 3.0)) * (0.25 + padding.x);

    offset.y = -(1.0 - (1.0 / exp2(octave))) - padding.y * octave;

	offset.y += min(1.0, floor(octave / 3.0)) * 0.35;

 	return offset;
}

vec3 GetBloom(vec2 coord)
{
 	vec3 bloom = vec3(0.0);

    //Reconstruct bloom from multiple blurred images
    bloom += Grab(coord, 1.0, vec2(CalcOffset(0.0))) * 1.0;
    bloom += Grab(coord, 2.0, vec2(CalcOffset(1.0))) * 1.5;
	bloom += Grab(coord, 3.0, vec2(CalcOffset(2.0))) * 1.0;
    bloom += Grab(coord, 4.0, vec2(CalcOffset(3.0))) * 1.5;
    bloom += Grab(coord, 5.0, vec2(CalcOffset(4.0))) * 1.8;
    bloom += Grab(coord, 6.0, vec2(CalcOffset(5.0))) * 1.0;
    bloom += Grab(coord, 7.0, vec2(CalcOffset(6.0))) * 1.0;
    bloom += Grab(coord, 8.0, vec2(CalcOffset(7.0))) * 1.0;

	return bloom;
}

void main()
{

    vec2 uv = _cvv_texcoord;

    vec3 color = ColorFetch(uv);


    color += clamp(GetBloom(uv) * 0.12, 0.0, 1.0);

//    color *= 200.0;


    //Tonemapping and color grading
//    color = pow(color, vec3(1.5));
//    color = color / (1.0 + color);
//    color = pow(color, vec3(1.0 / 1.5));
//
//
//    color = mix(color, color * color * (3.0 - 2.0 * color), vec3(1.0));
//    color = pow(color, vec3(1.3, 1.20, 1.0));
//
//	color = saturate(color * 1.01);
//
//    color = pow(color, vec3(0.7 / 2.2));

    gl_FragData[0] = vec4(color, 1.0);

}
//
//#define F 0.2
//
//void main() {
//	vec4 base = texture2D(_cvu_base, _cvv_texcoord);
//	vec4 b0 = texture2D(_cvu_bloom0, _cvv_texcoord) * F;
//	vec4 b1 = texture2D(_cvu_bloom1, _cvv_texcoord) * F;
//	vec4 b2 = texture2D(_cvu_bloom2, _cvv_texcoord) * F;                                                                                                                                                                                           ;
//
//	vec3 c = clamp(base.rgb + b0.rgb + b1.rgb + b2.rgb, 0.0, 1.0);
//	gl_FragData[0] = vec4(c, base.a);
//}
