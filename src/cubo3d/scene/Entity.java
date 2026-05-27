package cubo3d.scene;

import cubo3d.model.Mesh;

public final class Entity {
    private final Mesh mesh;
    private final Transform transform;
    private final EntityBuffers buffers;

    public Entity(Mesh mesh, Transform transform) {
        this.mesh = mesh;
        this.transform = transform;
        this.transform.updateMatrix();
        this.buffers = new EntityBuffers(mesh);
    }

    public Mesh getMesh() { return mesh; }
    public Transform getTransform() { return transform; }
    public EntityBuffers getBuffer(){ return buffers; }
}