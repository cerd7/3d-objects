package cubo3d.scene;

import java.util.ArrayList;
import java.util.List;

public final class Scene {
    private final List<Entity> entities = new ArrayList<>();

    public void add(Entity e) {
        entities.add(e);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void update(double dt) {
        for (Entity e : entities) {
            e.getTransform().rotY += dt;
            e.getTransform().rotX += dt * 0.5;
        }
    }
}