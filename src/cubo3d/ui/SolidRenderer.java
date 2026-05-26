package cubo3d.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import cubo3d.math.Vector3;
import cubo3d.model.Mesh;
import cubo3d.scene.Camera;
import cubo3d.scene.Entity;
import cubo3d.scene.EntityBuffers;
import cubo3d.scene.Scene;
import cubo3d.scene.Transform;

public final class SolidRenderer implements Renderer{
    private final Vector3 tmpA = new Vector3();
    private final Vector3 tmpB = new Vector3();
    private final Vector3 normal = new Vector3();
    private final Vector3 lightDir = new Vector3(0.2, -1.0, -0.3).normalize();
    private final Vector3 viewDir = new Vector3(0, 0, 1);

    private int[] xBuf = new int[0];
    private int[] yBuf = new int[0];

    @Override public void render(Graphics2D g, Scene scene, Camera camera, int cx, int cy){
        for(Entity e : scene.getEntities()){
            Mesh mesh = e.getMesh();
            Transform t = e.getTransform();

            ensureBuffers(mesh);

            EntityBuffers b = e.getBuffer();
            for(int i=0;i<mesh.vertices.length;i++){
                t.apply(mesh.vertices[i], b.world[i]);
            }

            double dist = camera.getDistance();
            for(int i=0;i<b.world.length;i++){
                Vector3 v = b.world[i];
                double scale = dist / (dist + v.z);
                b.projX[i] = cx + v.x * scale;
                b.projY[i] = cy + v.y * scale;
            }

            for(int f=0;f<mesh.faces.length;f++){
                int[] face = mesh.faces[f];
                double z = 0;
                for(int idx : face) z += b.world[idx].z;
                b.faceDepth[f] = z / face.length;
                b.faceOrder[f] = f;
            }

            sortFacesByDepth(b.faceOrder, b.faceDepth);

            for(int k=0;k< b.faceOrder.length;k++){
                int f = b.faceOrder[k];
                int[] face = mesh.faces[f];

                Vector3 v0 = b.world[face[0]]; 
                Vector3 v1 = b.world[face[1]];
                Vector3 v2 = b.world[face[2]];

                tmpA.sub(v1, v0);
                tmpB.sub(v2, v0);
                normal.cross(tmpA, tmpB).normalize();

                if (normal.dot(viewDir) >= 0) {
                    continue;
                }

                double intensity = Math.max(0.0, normal.dot(lightDir));
                int level = (int) Math.round(intensity * (Mesh.SHADE_LEVELS - 1));
                if(level < 0) level = 0;
                if(level >= Mesh.SHADE_LEVELS) level = Mesh.SHADE_LEVELS - 1;

                for(int i=0;i<face.length;i++){
                    int idx = face[i];
                    xBuf[i] = (int) b.projX[idx];
                    yBuf[i] = (int) b.projY[idx];
                }

                int colorIndex = mesh.faceColorIndex[f];
                g.setColor(mesh.shadeLut[colorIndex][level]);
                g.fillPolygon(xBuf, yBuf, face.length);
                g.setColor(Color.BLACK);
                g.drawPolygon(xBuf, yBuf, face.length);
            }
        }
    }

    private void ensureBuffers(Mesh mesh){
        if(xBuf.length < mesh.maxFaceSize){
            xBuf = new int[mesh.maxFaceSize];
            yBuf = new int[mesh.maxFaceSize];
        }
    }

    private void sortFacesByDepth(int[] order, double[] depth){
        for(int i=1;i<order.length;i++){
            int key = order[i];
            double keyDepth = depth[key];
            int j = i - 1;
            while (j >= 0 && depth[order[j]] < keyDepth) {
                order[j + 1] = order[j];
                j--;
            }
            order[j + 1] = key;
        }
    }


    /* DEAD CODE */

    // @Override
    // public void render(Graphics2D g, Scene scene, Camera camera, int cx, int cy) {
    //     for(Entity e : scene.getEntities()){
    //         Mesh mesh = e.getMesh();
    //         Transform t = e.getTransform();

    //         Vector2[] projected = new Vector2[mesh.vertices.length];
    //         for(int i=0;i<mesh.vertices.length;i++){
    //             Vector3 v = t.apply(mesh.vertices[i]);
    //             Vector2 p = camera.project(v);
    //             projected[i] = new Vector2(cx + p.x, cy + p.y);
    //         }

    //         for(int i=0;i<mesh.faces.length;i++){
    //             int[] face = mesh.faces[i];
    //             Polygon poly = new Polygon();
    //             for(int idx : face){
    //                 poly.addPoint((int) projected[idx].x, (int) projected[idx].y);
    //             }
    //             g.setColor(mesh.faceColors[i]);
    //             g.fillPolygon(poly);
    //             g.setColor(Color.BLACK);
    //             g.drawPolygon(poly);
    //         }
    //     }
    // }

}
