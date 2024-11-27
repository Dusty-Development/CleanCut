#version 460

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
layout(location = 0) in vec3 Position;

void main() {
    vec4 pos = ModelViewMat * vec4(Position.x, 0.0, Position.z, 1.0); // Flatten Y to ground
    gl_Position = ProjMat * pos;
}