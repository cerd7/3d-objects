package cubo3d.math;

public class Matrix4 {
    public final double[] m = new double[16];

    public Matrix4(){
        identity();
    }
    
    public double[] raw(){
        return m;
    }

    public Matrix4 identity(){
        for(int i = 0;i < 16; i++) m[i] = 0.0;
        m[0] = m[5] = m[10] = m[15] = 1.0;
        return this;
    }
    
    public Matrix4 setTranslationRotationScale(
        double tx, double ty, double tz,
        Quaternion q,
        double sx, double sy, double sz
    ){
        double xx = q.x * q.x;
        double yy = q.y * q.y;
        double zz = q.z * q.z;
        double xy = q.x * q.y;
        double xz = q.x * q.z;
        double yz = q.y * q.z;
        double wx = q.w * q.x;
        double wy = q.w * q.y;
        double wz = q.w * q.z;

        m[0] = (1.0 - 2.0 * (yy + zz)) * sx;
        m[1] = (2.0 * (xy - wz)) * sy;
        m[2] = (2.0 * (xz + wy)) * sz;
        m[3] = tx;

        m[4] = (2.0 * (xy + wz)) * sx; 
        m[5] = (1.0 - 2.0 * (xx + zz)) * sy;
        m[6] = (2.0 * (yz - wx)) * sz;
        m[7] = tz;

        m[8] = (2.0 * (xz - wy)) * sx;
        m[9] = (2.0 * (yz + wx)) * sy;
        m[10] = (1.0 - 2.0 * (xx + yy)) * sz;
        m[11] = tz;

        m[12] = m[13] = m[14] = 0.0;
        m[15] = 1.0;
        return this;
    }
}
