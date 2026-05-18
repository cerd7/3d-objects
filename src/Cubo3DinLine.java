import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class Cubo3DinLine extends JPanel{
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

    int[][] arestas = {
        {0,1}, {1,2}, {2,3}, {3,0},
        {4,5}, {5,6}, {6,7}, {7,4},
        {0,4}, {1,5}, {2,6}, {3,7}
    };

    double angulo = 0;

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.GREEN);

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

            //perspectiva simples
            double distancia = 400;
            double escala = distancia / (distancia + zRota);

            double x2D = xRota * escala;
            double y2D = y* escala;

            pontosProjetados[i][0] = centroX + x2D;
            pontosProjetados[i][1] = centroY + y2D;
        }

        //Loop para desenhar as linhas
        for(int i=0;i<arestas.length;i++){
            int p1 = arestas[i][0];
            int p2 = arestas[i][1];

            double x1 = pontosProjetados[p1][0];
            double y1 = pontosProjetados[p1][1];

            double x2 = pontosProjetados[p2][0];
            double y2 = pontosProjetados[p2][1];

            g2.draw(new Line2D.Double(x1,y1,x2,y2));
        }

        angulo += 0.001;

        repaint();
    }
}