#version 100

#ifdef GL_FRAGMENT_PRECISION_HIGH
  precision highp float;
#else
  precision mediump float;
#endif

varying vec2 var_texcoord;
varying vec3 var_normal;
varying vec3 var_wp;

uniform sampler2D diffuse_sampler;

uniform vec3 diffuse;
uniform vec3 ambient;
uniform vec3 specular;
uniform vec3 emisive;
uniform float transparent;
uniform float diffuse_texture_blending_factor;

uniform vec3 light_position;
uniform float light_constant_attenuation;
uniform float light_linear_attenuation;
uniform float light_quadratic_attenuation;

bool is_nan( float );

void main()
{
  gl_FragColor = vec4( diffuse, 1.0 );
  if ( diffuse_texture_blending_factor > 0.0 )
  {
    gl_FragColor.rgb *= 1.0 - diffuse_texture_blending_factor;
    gl_FragColor     += texture2D( diffuse_sampler, var_texcoord ) * diffuse_texture_blending_factor;
  }

  if ( length( var_normal ) > 0.0 )
  {
    vec3 light_ray = light_position - var_wp;
    float d = max( 0.0, dot( normalize( light_ray ), normalize( var_normal ) ) );
    
    float l = length( light_ray );
    gl_FragColor.rgb *= d;
    
    float attenuation = min( 1.0, 1.0 / ( light_constant_attenuation + l * light_linear_attenuation + l * l * light_quadratic_attenuation ) );
    gl_FragColor.rgb *= attenuation;
  }
  
  gl_FragColor.rgb += ambient;
  
  gl_FragColor.a *= 1.0 - transparent;
}

bool is_nan( float val )
{
  return (val <= 0.0 || 0.0 <= val) ? false : true;
}
