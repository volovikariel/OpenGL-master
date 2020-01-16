import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;


public class Mesh {

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    private final Texture texture;

    public Mesh(float[] positions, float[] textCoords, int[] indices, Texture texture) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            this.texture = texture;
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoId);

            // Position VBO
            int vboId = GL15.glGenBuffers();
            vboIdList.add(vboId);
            posBuffer  = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            GL30.glBindBuffer(GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL_ARRAY_BUFFER, posBuffer, GL15.GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(0);
            GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);

            // Texture Coords VBO
            vboId = GL15.glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            GL30.glBindBuffer(GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(1);
            GL30.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = GL15.glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL30.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            GL30.glBindBuffer(GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
        } finally {
            if(posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if(indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
            if(textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
        }
    }

    public void render() {

        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL13.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());

        GL30.glBindVertexArray(getVaoId());

        GL30.glDrawElements(GL11.GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }
    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glBindBuffer(GL_ARRAY_BUFFER, 0);

        for(int vbo : vboIdList) {
            GL30.glDeleteBuffers(vbo);
        }

        texture.cleanup();

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
    }
}
