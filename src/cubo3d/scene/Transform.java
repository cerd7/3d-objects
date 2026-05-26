package cubo3d.scene;

import cubo3d.math.Matrix4;
import cubo3d.math.Vector3;

public final class Transform {
    public double rotX;
    public double rotY;
    public double rotZ;

    public final Matrix4 matrix = new Matrix4();

    public void updateMatrix(){
        matrix.setRotationXYZ(rotX, rotY, rotZ);
    }

    public Vector3 apply(Vector3 src, Vector3 dst){
    return matrix.transform(src, dst);
    }

    /* DEAD CODE */
    
    // public Vector3 apply(Vector3 v){
    //     return v.rotateX(rotX).rotateY(rotY).rotateZ(rotZ);
    // }
}
