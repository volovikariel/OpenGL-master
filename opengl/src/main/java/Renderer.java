import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {


    // Rendering pipeline
    // 1. Have a list of Vertex Arrays (the coordinates) contained in a Vertex Buffer
    // 2. Vertex Buffers are processed by the Vertex Shader which turns 3D coords -> 2D coords
    // Can also generate other outputs, such as Colour & Texture coords
    // 3. Geometry process takes in the output of the Vertex Shader (Points), and links them up to make Triangles
    // Can also be done by using a specific Shader
    // 4. Rasterization takes the triangles and clips them, then transforms them into pixel-sized fragments.
    // 5. The fragment shader takes these pixel-sized fragments and assigns colours to each pixel
    //    it then stores it in a framebuffer which is then displayed on the screen.

    /**
     * Steps to start using the shaders
     * 1. Create a OpenGL Program
     * 2. Load the vertex and fragment shader code files.
     * 3. For each shader, create a new shader program and specify its type (vertex, fragment).
     * 4. Compile the shader.
     * 5. Attach the shader to the program.
     * 6. Link the program.
     */

    ShaderProgram shaderProgram;

    private int vaoId;

    private int vboId;

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private Matrix4f projectionMatrix;

    private Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("fragment.fs"));
        shaderProgram.link();

        // Create projection matrix
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().setPerspective(Renderer.FOV, aspectRatio, Renderer.Z_NEAR, Renderer.Z_FAR);
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("textureSampler");
        shaderProgram.createUniform("useColour");
        shaderProgram.createUniform("colour");


    }

    public void render(Window window, GameItem[] gameItems, Camera camera) {
        clear();

        if(window.isResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("textureSampler", 0);

        for(GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();

            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            shaderProgram.setUniform("colour", mesh.getColour());
            shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);

            mesh.render();
        }
        shaderProgram.unbind();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }

    public void cleanup() {
        if(shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
