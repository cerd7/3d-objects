import javax.swing.JFrame;
import javax.swing.JPanel;

public class App extends JPanel{
    public static void main(String[] args) throws Exception {
        JFrame janela = new JFrame("CUBO 3D");

        Cubo3DonFill painel = new Cubo3DonFill();
        janela.add(painel);

        janela.setSize(800,600);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
    }
}
