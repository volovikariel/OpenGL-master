#version 330

in vec2 outTextCoord;
out vec4 fragColour;

uniform sampler2D textureSampler;
uniform vec3 colour;
uniform int useColour;

void main() {
    if(useColour == 1) {
        fragColour = vec4(colour, 1);
    } else {
        fragColour = texture(textureSampler, outTextCoord);
    }
}
