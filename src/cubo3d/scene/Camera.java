package cubo3d.scene;

import cubo3d.math.Matrix4;

public class Camera {
    private final CameraTransform transform = new CameraTransform();
    private final Matrix4 projection = new Matrix4();

    private double fovYRadians;
    private double near;
    private double far;
    private double aspect = 1.0;

    
    public Camera(double fovYDegrees, double near, double far){
        this.fovYRadians = Math.toRadians(fovYDegrees);
        this.near = near;
        this.far = far;
    }

    public CameraTransform transform(){
        return transform;
    }

    public void setViewport(int width, int height){
        if(height > 0) aspect = (double) width/height;
    }

    public double near(){
        return near;
    }

    public Matrix4 viewMatrix(){
        return transform.viewMatrix();
    }

    public Matrix4 projectionMatrix4(){
        return projection.setPerspective(fovYRadians, aspect, near, far);
    }
}
