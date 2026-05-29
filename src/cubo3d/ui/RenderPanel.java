package cubo3d.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;

import cubo3d.scene.Camera;
import cubo3d.scene.Entity;
import cubo3d.scene.Scene;
import cubo3d.scene.Transform;

public final class RenderPanel extends JPanel {
    private final Scene scene;
    private final Renderer renderer;
    private final Camera camera;
    private final InputState input = new InputState();

    private long lastTime = System.nanoTime();

    public RenderPanel(Scene scene, Renderer renderer, Camera camera) {
        this.scene = scene;
        this.renderer = renderer;
        this.camera = camera;

        setFocusable(true);
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);

        new Timer(16, e -> tick()).start();
    }

    private void tick() {
        long now = System.nanoTime();
        double dt = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        update(dt);
        repaint();
    }

    private void update(double dt) {
        double speed = 1.5;
        double yaw = input.dragYaw;
        double pitch = input.dragPitch;

        if (input.left) yaw -= speed * dt;
        if (input.right) yaw += speed * dt;
        if (input.up) pitch -= speed * dt;
        if (input.down) pitch += speed * dt;

        if (Math.abs(yaw) > 1e-12 || Math.abs(pitch) > 1e-12) {
            for (Entity entity : scene.entities()) {
                Transform transform = entity.transform();
                transform.rotateWorld(0.0, 1.0, 0.0, yaw);
                transform.rotateLocal(1.0, 0.0, 0.0, pitch);
            }
        }

        input.dragYaw = 0.0;
        input.dragPitch = 0.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        camera.setViewport(getWidth(), getHeight());
        renderer.render(g2, scene, camera, getWidth() / 2, getHeight() / 2);
    }
}
