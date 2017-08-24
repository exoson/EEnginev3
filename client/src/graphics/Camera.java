
package graphics;

import math.Matrix4f;
import math.Vector2f;
import math.Vector3f;

public class Camera {

    public Matrix4f cameraMat = new Matrix4f();
    public Vector3f position = new Vector3f();

    private float pitch;
    private float yaw;
    private float roll;

    public Camera(Matrix4f cameraMat){
        this.cameraMat = cameraMat;
    }

    public void move(Vector3f dir) {
        position = position.add(dir);
    }
    public void move(Vector2f dir) {
        position = position.add(new Vector3f(dir));
    }
    
    public void rotate(Vector3f rot) {
        pitch += rot.getX();
        yaw += rot.getY();
        roll += rot.getZ();
    }

    public void setPosition(Vector3f pos){
        this.position = pos;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
    
    public Matrix4f getViewMatrix() {
        return Matrix4f.translate(
                        new Vector3f(-position.getX(), -position.getY(), -position.getZ())).multiply(
                        Matrix4f.rotateZ(roll).multiply(
                        Matrix4f.rotateY(yaw)).multiply(
                        Matrix4f.rotateX(pitch)));
    }
    
    public void enable() {

        Shader.defShader.enable();

        // Uncomment different lines to see different rotation effects
//		Shader.shader1.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotateX(rot)));
//		Shader.shader1.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotateY(rot)));
        Shader.defShader.setUniformMat4f("vw_matrix", getViewMatrix());

        Shader.defShader.disable();
    }
    
    public void disable() {
        Shader.defShader.enable();

        Shader.defShader.setUniformMat4f("vw_matrix", Matrix4f.identity());

        Shader.defShader.disable();
    }
}