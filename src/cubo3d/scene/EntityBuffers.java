package cubo3d.scene;

import cubo3d.model.Mesh;

public class EntityBuffers {
    public final double[] worldX;
    public final double[] worldY;
    public final double[] worldZ;

    public EntityBuffers(Mesh mesh){
        int count = mesh.vertexCount();
        worldX = new double[count];
        worldY = new double[count];
        worldZ = new double[count];
    }
}
