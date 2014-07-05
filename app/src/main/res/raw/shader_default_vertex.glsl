#version 100

attribute vec4 position;
attribute vec2 texcoord;
attribute vec3 normal;

varying vec2 var_texcoord;
varying vec3 var_normal;

uniform mat4 world_transformation;
uniform mat4 view_transformation;
uniform mat4 projection_transformation;

void main()
{
  gl_Position  = projection_transformation * view_transformation * world_transformation * position;
  var_texcoord = texcoord;
  var_normal   = normal;
}