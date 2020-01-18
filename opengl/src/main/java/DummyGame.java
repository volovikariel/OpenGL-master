import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class DummyGame implements IGameLogic {

    private final float CAMERA_POS_STEP = 0.05f;

    private final float MOUSE_SENSITIVITY = 0.3f;

    private final Renderer renderer;

    private GameItem[] gameItems;

    private Camera camera;

    private Vector3f cameraInc;

    //Each game has its own renderer (depending on the types of graphics wanted)
    //For now, it just clears the screen buffer
    public DummyGame() throws Exception {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        Mesh mesh = OBJLoader.loadMesh("models/bunny.obj");
        Texture texture = new Texture("grassblock.png");
        mesh.setTexture(texture);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(1.5f);
        gameItem.setPosition(0, 0, -10);
        gameItems = new GameItem[]{gameItem};
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        if(window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if(window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraInc.z = 1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraInc.x = 1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
            // Update camera position
            camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
                    cameraInc.y * CAMERA_POS_STEP,
                    cameraInc.z * CAMERA_POS_STEP);
            // Update camera based on mouse
        if(mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gameItems, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for(GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }


}
