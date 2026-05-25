package cubo3d.ui;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import cubo3d.scene.Scene;
import cubo3d.scene.Camera;

public class RenderPanel extends JPanel{
    private final Scene scene;
    private final Renderer renderer;
    private final Camera camera;

    public RenderPanel(Scene scene, Renderer renderer, Camera camera){
        this.scene = scene;
        this.renderer = renderer;
        this.camera = camera;

        Timer timer = new Timer(16, e -> {
            scene.update(0.01);
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        renderer.render(g2, scene, camera, getWidth() / 2, getHeight() / 2);
    }
}
