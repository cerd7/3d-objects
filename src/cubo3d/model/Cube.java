package cubo3d.model;

import java.awt.Color;

import cubo3d.math.Vector3;

public class Cube {
    public static Mesh create(double size){
        double s = size / 2.0;

        Vector3[] vertices = {
            new Vector3(-s, -s, -s),
            new Vector3( s, -s, -s),
            new Vector3( s,  s, -s),
            new Vector3(-s,  s, -s),
            new Vector3(-s, -s,  s),
            new Vector3( s, -s,  s),
            new Vector3( s,  s,  s),
            new Vector3(-s,  s,  s),
        };

        int[][] faces = {
            {0,1,2,3},
            {4,5,6,7},
            {0,1,5,4},
            {2,3,7,6},
            {1,2,6,5},
            {0,3,7,4}
        };

        int[][] edges = {
            {0,1},{1,2},{2,3},{3,0},
            {4,5},{5,6},{6,7},{7,4},
            {0,4},{1,5},{2,6},{3,7}
        };

        Color[] faceColors ={
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.CYAN, Color.MAGENTA
        };
        return new Mesh(vertices, faces, edges, faceColors);
    }
}
