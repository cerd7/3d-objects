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
        m[7] = ty;

        m[8] = (2.0 * (xz - wy)) * sx;
        m[9] = (2.0 * (yz + wx)) * sy;
        m[10] = (1.0 - 2.0 * (xx + yy)) * sz;
        m[11] = tz;

        m[12] = m[13] = m[14] = 0.0;
        m[15] = 1.0;
        return this;
    }

    public Matrix4 setPerspective(double fovYRadians, double aspect, double near, double far){
        double f = 1.0 / Math.tan(fovYRadians * 0.5);

        m[0] = f / aspect;
        m[1] = 0.0;
        m[2] = 0.0;
        m[3] = 0.0;

        m[4] = 0.0;
        m[5] = f;
        m[6] = 0.0;
        m[7] = 0.0;

        m[8] = 0.0;
        m[9] = 0.0;
        m[10] = far / (far - near);
        m[11] = (-near * far) / (far - near);
        
        m[12] = 0.0;
        m[13] = 0.0;
        m[14] = 1.0;
        m[15] = 0.0;

        return this;
    }

    public void transform(double x, double y, double z, double w, double out[]){
        out[0] = m[0] * x + m[1] * y + m[2] * z + m[3] * w;
        out[1] = m[4] * x + m[5] * y + m[6] * z + m[7] * w;
        out[2] = m[8] * x + m[9] * y + m[10] * z + m[11] * w;
        out[3] = m[12] * x + m[13] * y + m[14] * z + m[15] * w;
    }
}
