package cubo3d.scene;

import cubo3d.math.Vector3;

public final class Transform {
    public double rotX;
    public double rotY;
    public double rotZ;

    public Vector3 apply(Vector3 v){
        return v.rotateX(rotX).rotateY(rotY).rotateZ(rotZ);
    }
}
