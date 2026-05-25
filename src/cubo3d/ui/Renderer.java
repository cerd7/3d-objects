package cubo3d.ui;

import java.awt.Graphics2D;

import cubo3d.scene.Camera;
import cubo3d.scene.Scene;

public interface Renderer {
    void render(Graphics2D g, Scene scene, Camera camera, int cx, int cy);
}
