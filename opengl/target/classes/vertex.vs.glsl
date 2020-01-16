#version 330

layout (location=0) in vec3 position; //location 0 for positions, location 1 for colours, etc.
layout (location=1) in vec2 textCoord;

out vec2 outTextCoord;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

void main() {
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
    outTextCoord = textCoord;
}