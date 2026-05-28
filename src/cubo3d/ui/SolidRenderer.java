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

    private Vector3[] clipA = new Vector3[0];
    private Vector3[] clipB = new Vector3[0];
    private Vector3[] clipPool = new Vector3[0];
    private int clipPoolIndex = 0;

    private int[] xBuf = new int[0];
    private int[] yBuf = new int[0];

    @Override public void render(Graphics2D g, Scene scene, Camera camera, int cx, int cy){
        for(Entity e : scene.getEntities()){
            Mesh mesh = e.getMesh();
            Transform t = e.getTransform();

            ensureBuffers(mesh);
            ensureClipBuffers(mesh);

            EntityBuffers b = e.getBuffer();
            for(int i=0;i<mesh.vertices.length;i++){
                t.apply(mesh.vertices[i], b.world[i]);
            }

            double dist = camera.getDistance();
            double zNear = -dist + 1.0; 

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
                    clipA[i] = b.world[face[i]];
                }

                int clipped = clipPolygon(clipA, face.length, clipB, zNear);
                if(clipped < 3) continue;

                for(int i=0;i<clipped;i++){
                    Vector3 v = clipB[i];
                    double scale = dist / (dist + v.z);
                    xBuf[i] = (int) (cx + v.x * scale);
                    yBuf[i] = (int) (cy + v.y * scale);
                }

                int colorIndex = mesh.faceColorIndex[f];
                g.setColor(mesh.shadeLut[colorIndex][level]);
                g.fillPolygon(xBuf, yBuf, clipped);
                g.setColor(Color.BLACK);
                g.drawPolygon(xBuf, yBuf, clipped);
            }
        }
    }

    private void ensureBuffers(Mesh mesh){
        int needed = mesh.maxFaceSize + 2;
        if(xBuf.length < needed){
            xBuf = new int[needed];
            yBuf = new int[needed];
        }
    }

    private void ensureClipBuffers(Mesh mesh){
        int needed = mesh.maxFaceSize + 2;
        if(clipA.length < needed){
            clipA = new Vector3[needed];
            clipB = new Vector3[needed];
            clipPool = new Vector3[needed];
            for(int i=0;i<needed;i++){
                clipPool[i] = new Vector3();
            }
        }
    }

    private static boolean inside(Vector3 v, double zNear){
        return v.z > zNear;
    }

    private Vector3 intersectZPlane(Vector3 a, Vector3 b, double zNear, Vector3 out){
        double t = (zNear - a.z) / (b.z - a.z);
        out.x = a.x + (b.x - a.x) * t;
        out.y = a.y + (b.y - a.y) * t;
        out.z = zNear;
        return out;
    }

    private int clipPolygon(Vector3[] in, int inCount, Vector3[] out, double zNear){
        if(inCount == 0) return 0;

        clipPoolIndex = 0;
        Vector3 S = in[inCount - 1];
        boolean S_in = inside(S, zNear);
        int outCount = 0;

        for(int i=0;i<inCount;i++){
            Vector3 E = in[i];
            boolean E_in = inside(E, zNear);

            if(E_in){
                if(!S_in){
                    Vector3 p = clipPool[clipPoolIndex++];
                    out[outCount++] = intersectZPlane(S, E, zNear, p);
                }
                out[outCount++] = E;
            } else if(S_in){
                Vector3 p = clipPool[clipPoolIndex++];
                out[outCount++] = intersectZPlane(S, E, zNear, p);
            }
            S = E;
            S_in = E_in;
        }
        return outCount;
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

}
