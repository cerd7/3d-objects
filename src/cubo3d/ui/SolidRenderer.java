package cubo3d.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import cubo3d.math.Matrix4;
import cubo3d.model.Mesh;
import cubo3d.scene.Camera;
import cubo3d.scene.Entity;
import cubo3d.scene.EntityBuffers;
import cubo3d.scene.Scene;

public final class SolidRenderer implements Renderer {
    private static final double EPSILON = 1e-9;
    private static final double LIGHT_X = 0.18814417367671946;
    private static final double LIGHT_Y = -0.9407208683835973;
    private static final double LIGHT_Z = -0.28221626051507914;

    private Entity[] queueEntity = new Entity[32];
    private int[] queueFace = new int[32];
    private int[] queueShade = new int[32];
    private double[] queueDepth = new double[32];
    private int queueSize;
    private final double[] temp4 = new double[4];

    private double[] clipAx = new double[8];
    private double[] clipAy = new double[8];
    private double[] clipAz = new double[8];
    private double[] clipBx = new double[8];
    private double[] clipBy = new double[8];
    private double[] clipBz = new double[8];

    private int[] xPoints = new int[8];
    private int[] yPoints = new int[8];

    @Override
    public void render(Graphics2D g, Scene scene, Camera camera, int cx, int cy) {
        List<Entity> entities = scene.entities();
        Matrix4 view = camera.viewMatrix();
        Matrix4 projection = camera.projectionMatrix4();

        int faceCapacity = 0;
        int polygonCapacity = 0;
        for (Entity entity : entities) {
            faceCapacity += entity.mesh().faceCount();
            polygonCapacity = Math.max(polygonCapacity, entity.mesh().maxFaceSize() + 2);
        }

        ensureFaceQueue(faceCapacity);
        ensurePolygonBuffers(polygonCapacity);
        queueSize = 0;

        for (Entity entity : entities) {
            transformVertices(entity);
            enqueueVisibleFaces(entity);
        }

        if (queueSize > 1) quickSortFaces(0, queueSize - 1);

        for (int i = 0; i < queueSize; i++) {
            drawFace(g, queueEntity[i], queueFace[i], queueShade[i], camera, view, projection, cx, cy);
        }
    }

    private void transformVertices(Entity entity) {
        Mesh mesh = entity.mesh();
        EntityBuffers b = entity.buffers();
        Matrix4 matrix = entity.transform().matrix();

        double[] v = mesh.vertices();
        double[] m = matrix.raw();

        for (int i = 0, j = 0; i < mesh.vertexCount(); i++, j += 3) {
            double x = v[j];
            double y = v[j + 1];
            double z = v[j + 2];

            b.worldX[i] = m[0] * x + m[1] * y + m[2] * z + m[3];
            b.worldY[i] = m[4] * x + m[5] * y + m[6] * z + m[7];
            b.worldZ[i] = m[8] * x + m[9] * y + m[10] * z + m[11];
        }
    }

    private void enqueueVisibleFaces(Entity entity) {
        Mesh mesh = entity.mesh();
        EntityBuffers b = entity.buffers();

        for (int f = 0; f < mesh.faceCount(); f++) {
            int[] face = mesh.face(f);
            if (face.length < 3) continue;

            int i0 = face[0];
            int i1 = face[1];
            int i2 = face[2];

            double ax = b.worldX[i1] - b.worldX[i0];
            double ay = b.worldY[i1] - b.worldY[i0];
            double az = b.worldZ[i1] - b.worldZ[i0];

            double bx = b.worldX[i2] - b.worldX[i0];
            double by = b.worldY[i2] - b.worldY[i0];
            double bz = b.worldZ[i2] - b.worldZ[i0];

            double nx = ay * bz - az * by;
            double ny = az * bx - ax * bz;
            double nz = ax * by - ay * bx;

            double len2 = nx * nx + ny * ny + nz * nz;
            if (len2 < EPSILON || nz >= 0.0) continue;

            double invLen = 1.0 / Math.sqrt(len2);
            nx *= invLen;
            ny *= invLen;
            nz *= invLen;

            double intensity = Math.max(0.0, nx * LIGHT_X + ny * LIGHT_Y + nz * LIGHT_Z);
            int shade = (int) Math.round(intensity * (Mesh.SHADE_LEVELS - 1));
            if (shade < 0) shade = 0;
            if (shade >= Mesh.SHADE_LEVELS) shade = Mesh.SHADE_LEVELS - 1;

            double depth = 0.0;
            for (int index : face) depth += b.worldZ[index];
            depth /= face.length;

            queueEntity[queueSize] = entity;
            queueFace[queueSize] = f;
            queueShade[queueSize] = shade;
            queueDepth[queueSize] = depth;
            queueSize++;
        }
    }

    private void drawFace(Graphics2D g, Entity entity, int faceIndex, int shade, Camera camera, Matrix4 view, Matrix4 projection, int cx, int cy) {
        Mesh mesh = entity.mesh();
        EntityBuffers b = entity.buffers();
        int[] face = mesh.face(faceIndex);

        for(int i = 0; i < face.length;i++){
            int index = face[i];
            view.transform(b.worldX[index], b.worldY[index], b.worldZ[index], 1.0, temp4);
            clipAx[i] = temp4[0];
            clipAy[i] = temp4[1];
            clipAz[i] = temp4[2];
        }

        int clipped = clipNear(face.length, camera.near());
        if (clipped < 3) return;

        for (int i = 0; i < clipped; i++) {
            projection.transform(clipBx[i], clipBy[i], clipBz[i], 1.0, temp4);
            
            if(temp4[3] <= 0.0) return;

            double invW = 1.0 / temp4[3];
            double ndcX = temp4[0] * invW;
            double ndcY = temp4[1] * invW;
            double ndcZ = temp4[2] * invW;
            
            xPoints[i] = (int) Math.round(cx + ndcX * cx);
            yPoints[i] = (int) Math.round(cy - ndcY * cy);
        }

        g.setColor(mesh.shade(mesh.colorIndex(faceIndex), shade));
        g.fillPolygon(xPoints, yPoints, clipped);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, clipped);
    }

    private int clipNear(int count, double nearZ) {
        int out = 0;

        double sx = clipAx[count - 1];
        double sy = clipAy[count - 1];
        double sz = clipAz[count - 1];
        boolean sInside = sz > nearZ;

        for (int i = 0; i < count; i++) {
            double ex = clipAx[i];
            double ey = clipAy[i];
            double ez = clipAz[i];
            boolean eInside = ez > nearZ;

            if (eInside) {
                if (!sInside) {
                    double t = (nearZ - sz) / (ez - sz);
                    clipBx[out] = sx + (ex - sx) * t;
                    clipBy[out] = sy + (ey - sy) * t;
                    clipBz[out++] = nearZ;
                }

                clipBx[out] = ex;
                clipBy[out] = ey;
                clipBz[out++] = ez;
            } else if (sInside) {
                double t = (nearZ - sz) / (ez - sz);
                clipBx[out] = sx + (ex - sx) * t;
                clipBy[out] = sy + (ey - sy) * t;
                clipBz[out++] = nearZ;
            }

            sx = ex;
            sy = ey;
            sz = ez;
            sInside = eInside;
        }

        return out;
    }

    private void ensureFaceQueue(int capacity) {
        if (queueEntity.length >= capacity) return;

        int next = Math.max(capacity, queueEntity.length * 2);
        queueEntity = new Entity[next];
        queueFace = new int[next];
        queueShade = new int[next];
        queueDepth = new double[next];
    }

    private void ensurePolygonBuffers(int capacity) {
        if (clipAx.length >= capacity) return;

        clipAx = new double[capacity];
        clipAy = new double[capacity];
        clipAz = new double[capacity];
        clipBx = new double[capacity];
        clipBy = new double[capacity];
        clipBz = new double[capacity];
        xPoints = new int[capacity];
        yPoints = new int[capacity];
    }

    private void quickSortFaces(int low, int high) {
        int i = low;
        int j = high;
        double pivot = queueDepth[(low + high) >>> 1];

        while (i <= j) {
            while (queueDepth[i] > pivot) i++;
            while (queueDepth[j] < pivot) j--;

            if (i <= j) {
                swap(i, j);
                i++;
                j--;
            }
        }

        if (low < j) quickSortFaces(low, j);
        if (i < high) quickSortFaces(i, high);
    }

    private void swap(int a, int b) {
        Entity e = queueEntity[a];
        queueEntity[a] = queueEntity[b];
        queueEntity[b] = e;

        int face = queueFace[a];
        queueFace[a] = queueFace[b];
        queueFace[b] = face;

        int shade = queueShade[a];
        queueShade[a] = queueShade[b];
        queueShade[b] = shade;

        double depth = queueDepth[a];
        queueDepth[a] = queueDepth[b];
        queueDepth[b] = depth;
    }
}