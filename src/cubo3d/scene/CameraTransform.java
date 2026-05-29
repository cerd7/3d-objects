package cubo3d.scene;

import cubo3d.math.Matrix4;
import cubo3d.math.Quaternion;

public final class CameraTransform {
    private final Matrix4 view = new Matrix4();
    private final Quaternion rotation = new Quaternion();
    private final Quaternion delta = new Quaternion();

    private double x, y, z;
    private boolean dirty = true;

    public CameraTransform setPosition(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dirty = true;
        return this;
    }

    public CameraTransform rotateWorld(double ax, double ay, double az, double angle){
        delta.setFromAxisAngle(ax, ay, az, angle);
        rotation.preMultiply(delta).normalize();
        dirty = true;
        return this;
    }

    public Matrix4 viewMatrix(){
        if(dirty){
            rebuildView();
            dirty = false;
        }
        return view;
    }

    public void rebuildView(){
        double xx = rotation.x * rotation.x;
        double yy = rotation.y * rotation.y;
        double zz = rotation.z * rotation.z;
        double xy = rotation.x * rotation.y;
        double xz = rotation.x * rotation.z;
        double yz = rotation.y * rotation.z;
        double wx = rotation.w * rotation.x;
        double wy = rotation.w * rotation.y;
        double wz = rotation.w * rotation.z;

        double r00 = 1.0 - 2.0 * (yy + zz);
        double r01 = 2.0 * (xy - wz);
        double r02 = 2.0 * (xz + wy);

        double r10 = 2.0 * (xy + wz); 
        double r11 = 1.0 - 2.0 * (xx + zz);
        double r12 = 2.0 * (yz - wx);

        double r20 = 2.0 * (xz - wy); 
        double r21 = 2.0 * (yz + wx);
        double r22 = 1.0 - 2.0 * (xx + yy);

        double[] m = view.raw();

        m[0] = r00;
        m[1] = r10;
        m[2] = r20;
        m[3] = -(m[0] * x + m[1] * y + m[2] * z);

        m[4] = r01;
        m[5] = r11;
        m[6] = r21;
        m[7] = -(m[4] * x + m[5] * y + m[6] * z);

        m[8] = r02;
        m[9] = r12;
        m[10] = r22;
        m[11] = -(m[8] * x + m[9] * y + m[10] * z);

        m[12] = 0.0; 
        m[13] = 0.0;
        m[14] = 0.0;
        m[15] = 1.0;
    }

}
