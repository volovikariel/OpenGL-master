#version 330

layout (location=0) in vec3 position; //location 0 for positions, location 1 for colours, etc.
layout (location=1) in vec2 textCoord;
layout (location=2) in vec3 vertexNormal;

out vec2 outTextCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
    outTextCoord = textCoord;
}