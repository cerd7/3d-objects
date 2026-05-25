package cubo3d.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import cubo3d.math.Vector2;
import cubo3d.math.Vector3;
import cubo3d.model.Mesh;
import cubo3d.scene.Camera;
import cubo3d.scene.Entity;
import cubo3d.scene.Scene;
import cubo3d.scene.Transform;

public final class SolidRenderer implements Renderer{

    @Override
    public void render(Graphics2D g, Scene scene, Camera camera, int cx, int cy) {
        for(Entity e : scene.getEntities()){
            Mesh mesh = e.getMesh();
            Transform t = e.getTransform();

            Vector2[] projected = new Vector2[mesh.vertices.length];
            for(int i=0;i<mesh.vertices.length;i++){
                Vector3 v = t.apply(mesh.vertices[i]);
                Vector2 p = camera.project(v);
                projected[i] = new Vector2(cx + p.x, cy + p.y);
            }

            for(int i=0;i<mesh.faces.length;i++){
                int[] face = mesh.faces[i];
                Polygon poly = new Polygon();
                for(int idx : face){
                    poly.addPoint((int) projected[idx].x, (int) projected[idx].y);
                }
                g.setColor(mesh.faceColors[i]);
                g.fillPolygon(poly);
                g.setColor(Color.BLACK);
                g.drawPolygon(poly);
            }
        }
    }

}
