package cubo3d.model;

import java.awt.Color;
import cubo3d.math.Vector3;

public class Mesh {
    public static final int SHADE_LEVELS = 16;

    public final Vector3[] vertices;
    public final int[][] faces;
    public final Color[] faceColors;
    public final int maxFaceSize;
    public final int[] faceColorIndex;
    public final Color[][] shadeLut;

    public Mesh(Vector3[] vertices, int[][] faces, Color[] palette, int[] faceColorIndex){
        this.vertices = vertices;
        this.faces = faces;
        this.faceColors = palette;
        this.faceColorIndex = faceColorIndex;

        int max = 0;
        for(int i=0;i<faces.length;i++){
            if(faces[i].length > max) max = faces[i].length;
        }
        maxFaceSize = max;

        shadeLut = buildShadeLut(faceColors, SHADE_LEVELS);
    }

    private static Color[][] buildShadeLut(Color[] base, int levels){
        Color[][] lut = new Color[base.length][levels];
        double ambient = 0.2;
        double diffuse = 0.8;

        for(int i=0;i<base.length;i++){
            for(int l=0;l<levels;l++){
                double t = (double) l / (levels - 1);
                double s = ambient + diffuse * t;

                int r = clamp((int) Math.round(base[i].getRed() * s));
                int g = clamp((int) Math.round(base[i].getGreen() * s));
                int b = clamp((int) Math.round(base[i].getBlue() * s));
            
                lut[i][l] = new Color(r, g, b);
            }
        }
        return lut;
    }

    private static int clamp(int v){
        return v < 0 ? 0 : (v > 255 ? 255 : v);
    }
}
