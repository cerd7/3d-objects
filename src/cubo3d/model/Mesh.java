package cubo3d.model;

import cubo3d.math.Vector3;
import java.awt.Color;

public class Mesh {
    public final Vector3[] vertices;
    public final int[][] faces;
    public final int[][] edges;
    public final Color[] faceColors;

    public Mesh(Vector3[] vertices, 
                int [][] faces, 
                int[][] edges, 
                Color[] faceColors
    ){
        this.vertices = vertices;
        this.faces = faces;
        this.edges = edges;
        this.faceColors = faceColors;
    }
}
