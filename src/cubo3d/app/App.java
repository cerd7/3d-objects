package cubo3d.app;

import javax.swing.JFrame;
import cubo3d.model.Cube;
import cubo3d.scene.Scene;
import cubo3d.scene.Entity;
import cubo3d.scene.Transform;
import cubo3d.scene.Camera;
import cubo3d.ui.RenderPanel;
import cubo3d.ui.Renderer;
import cubo3d.ui.SolidRenderer;

public class App{
    public static void main(String[] args) throws Exception {
        JFrame janela = new JFrame("Cubo 3D");

        Scene scene = new Scene();
        Renderer renderer = new SolidRenderer();
        Camera camera =  new Camera(400);
        RenderPanel panel = new RenderPanel(scene, renderer, camera);
        
        scene.add(new Entity(Cube.create(200), new Transform()));
        
        janela.add(panel);
        janela.setSize(800, 600);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);

        javax.swing.SwingUtilities.invokeLater(panel::requestFocusInWindow);
    }
}
