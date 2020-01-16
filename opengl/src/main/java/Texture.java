import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.glDeleteTextures;


public class Texture {

    private int id;

    public Texture(String fileName) throws Exception {
        try {
            PNGDecoder pngDecoder = new PNGDecoder(Texture.class.getResourceAsStream(fileName));
            // Each pixel has 4 bytes!
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * pngDecoder.getWidth() * pngDecoder.getHeight());
            pngDecoder.decode(byteBuffer, pngDecoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();

            id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            // It's RGBA, 4 bytes, R is 1, G is 1, etc.
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, pngDecoder.getWidth(), pngDecoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texture(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void bind() {

    }
    public void cleanup() {
        glDeleteTextures(id);
    }
}
