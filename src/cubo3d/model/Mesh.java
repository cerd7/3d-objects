package cubo3d.model;

import java.awt.Color;
import java.util.Arrays;

public class Mesh {
    public static final int SHADE_LEVELS = 16;

    public final double[] vertices;
    public final int[][] faces;
    public final Color[][] shadeLut;
    public final int[] faceColorIndex;
    public final int maxFaceSize;

    public Mesh(double[] vertices, int[][] faces, Color[] palette, int[] faceColorIndex){
        if(vertices.length % 3 != 0) throw new IllegalArgumentException("vertices must be xyz triplets");
        if(faces.length != faceColorIndex.length) throw new IllegalArgumentException("face color index mismatch");

        this.vertices = Arrays.copyOf(vertices, vertices.length);
        this.faces = copyFaces(faces);
        this.faceColorIndex = Arrays.copyOf(faceColorIndex, faceColorIndex.length);
        this.shadeLut = buildShadeLut(Arrays.copyOf(palette, palette.length));
        
        int max = 0;
        for(int[] face : faces) max = Math.max(max, face.length);
        this.maxFaceSize = max;
    }

    public double[] vertices(){
        return vertices;
    }

    public int vertexCount(){
        return vertices.length / 3;
    }

    public int faceCount(){
        return faces.length;
    }

    public int[] face(int index){
        return faces[index];
    }

    public int maxFaceSize(){
        return maxFaceSize;
    }

    public int colorIndex(int faceIndex){
        return faceColorIndex[faceIndex];
    }

    public Color shade(int colorIndex, int level){
        return shadeLut[colorIndex][level];
    }

    private static int[][] copyFaces(int[][] src){
        int[][] out = new int[src.length][];
        for(int i = 0; i < src.length; i++) out[i] = Arrays.copyOf(src[i], src[i].length);
        return out;
    }

    private static Color[][] buildShadeLut(Color[] palette){
        Color[][] lut = new Color[palette.length][SHADE_LEVELS];

        for(int i = 0; i < palette.length; i++){
            for(int level = 0; level < SHADE_LEVELS; level++){
                double t = (double) level / (SHADE_LEVELS - 1);
                double s = 0.2 + 0.8 * t;

                lut[i][level] = new Color(
                    clamp((int) Math.round(palette[i].getRed() * s)),
                    clamp((int) Math.round(palette[i].getGreen() * s)),
                    clamp((int) Math.round(palette[i].getBlue() * s))
                );
            }
        }
        return lut;
    }

    private static int clamp(int value){
        return value < 0 ? 0 : Math.min(value, 255);
    }
}
