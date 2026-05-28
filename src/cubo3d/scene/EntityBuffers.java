package cubo3d.scene;

import cubo3d.math.Vector3;
import cubo3d.model.Mesh;

public class EntityBuffers {
    public final Vector3[] world;
    public final double[] projX;
    public final double[] projY;
    public final int[] faceOrder;
    public final double[] faceDepth;

    public EntityBuffers(Mesh mesh){
        world = new Vector3[mesh.vertices.length];
        for(int i=0;i<world.length;i++) world[i] = new Vector3();

        projX = new double[mesh.vertices.length];
        projY = new double[mesh.vertices.length];
        
        faceOrder = new int[mesh.faces.length];
        faceDepth = new double[mesh.faces.length];
    }
}
