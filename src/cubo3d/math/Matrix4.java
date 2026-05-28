package cubo3d.math;

public class Matrix4 {
    public final double[] m = new double[16];

    public Matrix4 identity(){
        for(int i=0;i<16;i++) m[i] = 0;
        m[0] = m[5] = m[10] = m[15] = 1;
        return this;
    }
    
    public Matrix4 setRotationXYZ(double rx, double ry, double rz){
        double cx = Math.cos(rx), sx = Math.sin(rx);
        double cy = Math.cos(ry), sy = Math.sin(ry);
        double cz = Math.cos(rz), sz = Math.sin(rz);

        m[0] = cz * cy;
        m[1] = cz * sy * sx - sz * cx;
        m[2] = cz * sy * cx + sz * sx;
        m[3] = 0;

        m[4] = sz * cy;
        m[5] = sz * sy * sx + cz * cx;
        m[6] = sz * sy * cx - cz * sx;
        m[7] = 0;

        m[8]  = -sy;
        m[9]  = cy * sx;
        m[10] = cy * cx;
        m[11] = 0;

        m[12] = 0; m[13] = 0; m[14] = 0; m[15] = 1;
        return this;
    }

    public Vector3 transform(Vector3 v, Vector3 out){
        double x = v.x, y = v.y, z = v.z;
        out.x = m[0] * x + m[1] * y + m[2] * z + m[3];
        out.y = m[4] * x + m[5] * y + m[6] * z + m[7];
        out.z = m[8] * x + m[9] * y + m[10] * z + m[11];
        return out;
    }
}
