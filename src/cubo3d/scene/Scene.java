package cubo3d.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Scene {
    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> readonlyEntities = Collections.unmodifiableList(entities);

    public void add(Entity e) {
        entities.add(e);
    }

    public List<Entity> entities() {
        return readonlyEntities;
    }
}