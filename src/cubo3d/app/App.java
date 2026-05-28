package cubo3d.app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cubo3d.model.Cube;
import cubo3d.model.Mesh;
import cubo3d.scene.Camera;
import cubo3d.scene.Entity;
import cubo3d.scene.Scene;
import cubo3d.scene.Transform;
import cubo3d.ui.RenderPanel;
import cubo3d.ui.SolidRenderer;

public final class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::start);
    }

    private static void start() {
        Scene scene = new Scene();
        Mesh cube = Cube.create(160.0);

        scene.add(new Entity(cube, new Transform().setPosition(-120.0, 0.0, 0.0)));
        scene.add(new Entity(cube, new Transform().setPosition(120.0, 0.0, 80.0).setScale(0.7, 0.7, 0.7)));

        RenderPanel panel = new RenderPanel(scene, new SolidRenderer(), new Camera(400.0));

        JFrame frame = new JFrame("Cubo 3D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.requestFocusInWindow();
    }
}
