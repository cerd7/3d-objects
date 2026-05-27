package cubo3d.ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import cubo3d.scene.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import cubo3d.scene.Scene;
import cubo3d.scene.Transform;
import cubo3d.scene.Camera;

public class RenderPanel extends JPanel{
    private final Scene scene;
    private final Renderer renderer;
    private final Camera camera;
    private final InputState input = new InputState();

    private long lastTime = System.nanoTime();

    public RenderPanel(Scene scene, Renderer renderer, Camera camera){
        this.scene = scene;
        this.renderer = renderer;
        this.camera = camera;

        setFocusable(true);
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);

        Timer timer = new Timer(16, e -> tick());
        timer.start();
    }

    private void tick(){
        long now = System.nanoTime();
        double dt = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        update(dt);
        repaint();
    }

    private void update(double dt){
        double speed = 1.5;

        for(Entity e : scene.getEntities() ){
            Transform t = e.getTransform();
            
            if(input.left)  t.rotY -= speed * dt;
            if(input.right) t.rotY += speed * dt;
            if(input.up)    t.rotX -= speed * dt;
            if(input.down)  t.rotX += speed * dt;

            t.rotY += input.dragYaw;
            t.rotX += input.dragPitch;

            t.updateMatrix();
        }
        input.dragYaw = 0;
        input.dragPitch = 0;
    }

    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        renderer.render(g2, scene, camera, getWidth() / 2, getHeight() / 2);
    }
}
