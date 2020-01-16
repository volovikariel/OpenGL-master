#version 330

in vec2 outTextCoord;
out vec4 fragColour;

uniform sampler2D textureSampler;

void main() {
    fragColour = texture(textureSampler, outTextCoord);
}
