package cubo3d.scene;

import cubo3d.math.Vector2;
import cubo3d.math.Vector3;

public class Camera {
    private final double distance;

    public Camera(double distance){
        this.distance = distance;
    }

    public Vector2 project(Vector3 v){
        double scale = distance / (distance + v.z);
        return new Vector2(v.x * scale, v.y * scale);
    }

    public double getDistance(){
        return distance;
    }
}
