package cubo3d.math;

public final class Vector3 {
    public double x, y, z;

    public Vector3(){}

    public Vector3(double x, double y, double z){
        set(x, y, z);
    }

    public Vector3 set(double x, double y, double z){
        this.x = x; this.y = y; this.z = z;
        return this;
    }

    public Vector3 copyFrom(Vector3 v){
        this.x = v.x; this.y = v.y; this.z = v.z;
        return this;
    }

    public Vector3 sub(Vector3 a, Vector3 b){
        this.x = a.x - b.x; 
        this.y = a.y - b.y;
        this.z = a.z - b.z;
        return this;
    }

    public Vector3 cross(Vector3 a, Vector3 b){
        double cx = a.y * b.z - a.z * b.y;
        double cy = a.z * b.x - a.x * b.z;
        double cz = a.x * b.y - a.y * b.x;
        this.x = cx; this.y = cy; this.z = cz;
        return this;
    }

    public double dot(Vector3 v){
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3 normalize(){
        double len = Math.sqrt(x * x + y * y + z * z);
        if(len > 1e-9){
            double inv = 1.0 / len;
            x *= inv; y *= inv; z *= inv;
        }
        return this;
    }
}
