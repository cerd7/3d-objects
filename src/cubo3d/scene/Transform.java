package cubo3d.scene;

import cubo3d.math.Matrix4;
import cubo3d.math.Quaternion;

public final class Transform {
    private final Matrix4 matrix = new Matrix4();
    private final Quaternion rotation = new Quaternion();
    private final Quaternion delta = new Quaternion();

    private double x;
    private double y;
    private double z;
    private double sx = 1.0;
    private double sy = 1.0;
    private double sz = 1.0;
    private boolean dirty = true;

    public Transform setPosition(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        dirty = true;
        return this;
    }

    public Transform setScale(double sx, double sy, double sz){
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        dirty = true;
        return this;
    }

    public Transform rotateWorld(double ax, double ay, double az, double angle){
        delta.setFromAxisAngle(ax, ay, az, angle);
        rotation.preMultiply(delta).normalize();
        dirty = true;
        return this;
    }

    public Transform rotateLocal(double ax, double ay, double az, double angle){
        delta.setFromAxisAngle(ax, ay, az, angle);
        rotation.multiply(delta).normalize();
        dirty = true;
        return this;
    }

    public Matrix4 matrix(){
        if(dirty){
            matrix.setTranslationRotationScale(x, y, z, rotation, sx, sy, sz);
            dirty = false;
        }
        return matrix;
    }
}
