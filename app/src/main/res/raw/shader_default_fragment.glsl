#version 100

#ifdef GL_FRAGMENT_PRECISION_HIGH
  precision highp float;
#else
  precision mediump float;
#endif

varying vec2 var_texcoord;
varying vec3 var_normal;
varying vec4 vPosition;
varying vec3 vNormal;

uniform sampler2D diffuse_sampler;
uniform vec3 diffuse;
uniform vec3 ambient;
uniform vec3 specular;
uniform vec3 emisive;
uniform float transparent;
uniform float diffuse_texture_blending_factor;

bool is_nan( float );

void main(void)
{
  vec3 light = normalize((gl_LightSource[0].position * vPosition.w - gl_LightSource[0].position.w * vPosition).xyz);
  vec3 fnormal = normalize(vNormal);
  float diffuse = max(dot(light, fnormal), 0.0);

  vec3 view = -normalize(vPosition.xyz);
  vec3 halfway = normalize(light + view);
  float specular = pow(max(dot(fnormal, halfway), 0.0), gl_FrontMaterial.shininess);
  gl_FragColor = gl_FrontLightProduct[0].diffuse * diffuse
                + gl_FrontLightProduct[0].specular * specular
                + gl_FrontLightProduct[0].ambient;
}

bool is_nan( float val )
{
  return (val <= 0.0 || 0.0 <= val) ? false : true;
}