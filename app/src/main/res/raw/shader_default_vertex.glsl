#version 100

attribute vec4 position;
attribute vec2 texcoord;
attribute vec3 normal;

varying vec2 var_texcoord;
varying vec3 var_normal;
varying vec3 var_wp;

uniform mat4 world_transformation;
uniform mat4 view_transformation;
uniform mat4 projection_transformation;

void main()
{
  vec4 wp      = world_transformation * position;
  gl_Position  = projection_transformation * view_transformation * wp;
  var_texcoord = texcoord;
  var_normal   = normal;
  var_wp       = wp.xyz;
}
