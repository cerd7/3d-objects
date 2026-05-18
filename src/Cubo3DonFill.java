import javax.swing.*;
import java.awt.*;

public class Cubo3DonFill extends JPanel{
    //Vértices do cubo
    double[][] vertices = {
        {-100, -100, -100},
        { 100, -100, -100},
        { 100,  100, -100},
        {-100,  100, -100},
        {-100, -100,  100},
        { 100, -100,  100},
        { 100,  100,  100},
        {-100,  100,  100}
    };

    int[][] faces = {
        {0,1,2,3},
        {4,5,6,7},
        {0,1,5,4},
        {2,3,7,6},
        {1,2,6,5},
        {0,3,7,4}
    };

    Color[] cores = {
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.CYAN,
        Color.MAGENTA
    };

    double angulo = 0;

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        double[][] pontosProjetados = new double[vertices.length][2];

        //Loop principal | Rotraciona e projeta os vertices
        for(int i=0;i<vertices.length;i++){
            double x = vertices[i][0];
            double y = vertices[i][1];
            double z = vertices[i][2];

            //rotação no eixo Y
            double xRota = x * Math.cos(angulo) - z * Math.sin(angulo);
            double zRota = x * Math.sin(angulo) + z * Math.cos(angulo);

            //rotação no eixo X
            double yRota = y * Math.cos(angulo) - zRota * Math.sin(angulo);
            double zFinal = y * Math.sin(angulo) + zRota * Math.cos(angulo);

            //perspectiva simples
            double distancia = 400;
            double escala = distancia / (distancia + zFinal);

            double x2D = xRota * escala;
            double y2D = yRota * escala;

            pontosProjetados[i][0] = centroX + x2D;
            pontosProjetados[i][1] = centroY + y2D;
        }

        //Loop para desenhar as faces
        for(int i=0;i<faces.length;i++){
            int[] face = faces[i];

            int[] xPoints = new int[4];
            int[] yPoints = new int[4];

            for(int j=0;j<4;j++){
                int indice = face[j];

                xPoints[j] = (int) pontosProjetados[indice][0];
                yPoints[j] = (int) pontosProjetados[indice][1];
            }

            g2.setColor(cores[i]);
            g2.fillPolygon(xPoints, yPoints, 4);

            // Contorno
            g2.setColor(Color.BLACK);
            g2.drawPolygon(xPoints, yPoints, 4);
        }

        angulo += 0.01;

        repaint();
    }
}