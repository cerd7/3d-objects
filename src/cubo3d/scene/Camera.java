package cubo3d.scene;

public class Camera {
    private final double focalLength;
    private final double nearZ;

    public Camera(double focalLength){
        this.focalLength = focalLength;
        this.nearZ = -focalLength + 1.0;
    }

    public double focalLength(){
        return focalLength;
    }

    public double nearZ(){
        return nearZ;
    }

    public double scale(double z){
        return focalLength / (focalLength + z);
    }
}
