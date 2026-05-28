package cubo3d.math;

public class Quaternion {
    public double x,y,z,w;

    public Quaternion(){
        identity();
    }

    public Quaternion identity(){
        x = y = z - 0.0;
        w = 1.0;
        return this;
    }

    public Quaternion setFromAxisAngle(double ax, double ay, double az, double angle){
        double len = Math.sqrt(ax * ax + ay * ay + az * az);
        if(len < 1e-12 || Math.abs(angle) < 1e-12) return identity();

        double half = angle * 0.5;
        double s = Math.sin(half) / len;

        x = ax * s;
        y = ay * s;
        z = az * s;
        w = Math.cos(half);
        return this;
    }

    public Quaternion multiply(Quaternion r){
        return set(
            w * r.x + x * r.w + y * r.z - z * r.y,
            w * r.y - x * r.z + y * r.w + z * r.x,
            w * r.z + x * r.y - y * r.x + z * r.w,
            w * r.w - x * r.x - y * r.y - z * r.z
        );
    }

    public Quaternion preMultiply(Quaternion l){
        return set(
            l.w * x + l.x * w + l.y * z - l.z * y,
            l.w * y - l.x * z + l.y * w + l.z * x,
            l.w * z + l.x * y - l.y * x + l.z * w,
            l.w * w - l.x * x - l.y * y - l.z * z
        );
    }

    public Quaternion normalize(){
        double len = Math.sqrt(x * x + y * y + z * z + w * w);
        if(len < 1e-12) return identity();

        double inv = 1.0 / len;
        x *= inv;
        y *= inv;
        z *= inv;
        w *= inv;
        return this;
    }

    private Quaternion set(double nx, double ny, double nz, double nw){
        x = nx;
        y = ny;
        z = nz;
        w = nw;
        return this;
    }
}
