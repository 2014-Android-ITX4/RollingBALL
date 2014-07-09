#version 100

attribute vec4 position;
attribute vec2 texcoord;
attribute vec3 normal;

varying vec2 var_texcoord;
varying vec3 var_normal;
varying vec4 vPosition;
varying vec3 vNormal;

uniform mat4 world_transformation;
uniform mat4 view_transformation;
uniform mat4 projection_transformation;

void main(void)
{
  vPosition = gl_ModelViewMatrix * gl_Vertex;
  vNormal = normalize(gl_NormalMatrix * gl_Normal);
  gl_Position = ftransform();
}