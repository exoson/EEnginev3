package client.graphics;

import common.game.Matrix4f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 * @author Lime
 */
public class Window {

    private static long window;

    private final static int WIDTH = 800, HEIGHT = 600;

    private final static String GAMENAME = "";

    public static void init() {
        if(!glfwInit()) {
            System.err.println("GLFW initialization failed!");
        }
//        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        window = glfwCreateWindow(WIDTH, HEIGHT, GAMENAME, NULL, NULL);

        if(window == NULL) {
            System.err.println("Could not create our Window!");
        }

        // creates a bytebuffer object 'vidmode' which then queries
        // to see what the primary monitor is.
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSwapInterval(1);
        // Sets the initial position of our game window.
        glfwSetWindowPos(getWindow(), 100, 100);

        glfwMakeContextCurrent(getWindow());

        glfwShowWindow(getWindow());

        GL.createCapabilities();

        glClearColor(0,0,0,1);

        glActiveTexture(GL_TEXTURE1);

        glDisable(GL_DEPTH_TEST);

        Shader.loadAll();
        Shader.defShader.enable();
        Matrix4f pr_matrix = Matrix4f.orthographic(0, WIDTH, HEIGHT, 0, -1, 1);
        Shader.defShader.setUniformMat4f("vw_matrix", Matrix4f.identity());
        Shader.defShader.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.defShader.setUniform1i("tex", 1);
        Shader.defShader.disable();
    }
    /**
     * @return the window
     */
    public static long getWindow() {
        return window;
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
    }

    public static void update() {
        int i = glGetError();
        if(i != GL_NO_ERROR)
            System.err.println(i);

        glfwSwapBuffers(getWindow());
    }

    public static void cleanUp() {
        glfwDestroyWindow(getWindow());
        glfwTerminate();
    }
}
