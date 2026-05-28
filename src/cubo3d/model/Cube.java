package cubo3d.model;

import java.awt.Color;

public class Cube {
    public Cube(){}

    public static Mesh create(double size){
        double s = size * 0.5;

        double[] vertices = {
            -s, -s, -s,
             s, -s, -s,
             s,  s, -s,
            -s,  s, -s,
            -s, -s,  s,
             s, -s,  s,
             s,  s,  s,
            -s,  s,  s,
        };

        int[][] faces = {
            {0,3,2,1},
            {4,5,6,7},
            {0,1,5,4},
            {3,7,6,2},
            {1,2,6,5},
            {0,4,7,3}
        };

        Color[] palette ={
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.CYAN, Color.MAGENTA
        };
        return new Mesh(vertices, faces, palette, new int[] {0,1,2,3,4,5});
    }
}
