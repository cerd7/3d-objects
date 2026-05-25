package cubo3d.math;

public final class Vector3 {
    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 rotateX(double a){
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        double y2 = x * cos - z * sin;
        double z2 = x * sin + z * sin;
        return new Vector3(x, y2, z2);
    }

    public Vector3 rotateY(double a){
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        double x2 = x * cos - z * sin;
        double z2 = x * sin + z * sin;
        return new Vector3(x2, y, z2);
    }

    public Vector3 rotateZ(double a){
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        double x2 = x * cos - y * sin;
        double y2 = x * sin + y * sin;
        return new Vector3(x2, y2, z);
    }
}
