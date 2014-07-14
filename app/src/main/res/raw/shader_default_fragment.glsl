#version 100

#ifdef GL_FRAGMENT_PRECISION_HIGH
  precision highp float;
#else
  precision mediump float;
#endif

varying vec2 var_texcoord;
varying vec3 var_normal;

uniform sampler2D diffuse_sampler;

uniform vec3 diffuse;
uniform vec3 ambient;
uniform vec3 specular;
uniform vec3 emisive;
uniform float transparent;
uniform float diffuse_texture_blending_factor;

uniform vec3 light_direction;

bool is_nan( float );

void main()
{
  gl_FragColor = vec4( diffuse, 1.0 );
  if ( diffuse_texture_blending_factor > 0.0 )
  {
    gl_FragColor.rgb *= 1.0 - diffuse_texture_blending_factor;
    gl_FragColor     += texture2D( diffuse_sampler, var_texcoord ) * diffuse_texture_blending_factor;
  }

  // lighting parallel, without specular in currently
  gl_FragColor.rgb += ambient;
  if ( length( var_normal ) > 0.0 )
    gl_FragColor.rgb *= dot( var_normal, light_direction );

  gl_FragColor.a *= 1.0 - transparent;
}

bool is_nan( float val )
{
  return (val <= 0.0 || 0.0 <= val) ? false : true;
}
