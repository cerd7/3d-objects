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
            {0,3,2,1},
            {4,5,6,7},
            {0,1,5,4},
            {3,7,6,2},
            {1,2,6,5},
            {0,4,7,3}
        };

        Color[] faceColors ={
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.CYAN, Color.MAGENTA
        };
        int[] faceColorIndex = {0,1,2,3,4,5};
        return new Mesh(vertices, faces, faceColors, faceColorIndex);
    }
}
