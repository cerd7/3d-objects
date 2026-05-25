package cubo3d.scene;

import cubo3d.model.Mesh;

public final class Entity {
    private final Mesh mesh;
    private final Transform transform;

    public Entity(Mesh mesh, Transform transform) {
        this.mesh = mesh;
        this.transform = transform;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Transform getTransform() {
        return transform;
    }
}