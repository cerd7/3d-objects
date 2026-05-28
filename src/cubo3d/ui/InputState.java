package cubo3d.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputState extends KeyAdapter implements MouseListener, MouseMotionListener{
    public boolean left, right, up, down;
    public double dragYaw, dragPitch;

    private int lastX, lastY;
    private boolean dragging;

    @Override public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: left = true; break;
            case KeyEvent.VK_RIGHT: right = true; break;
            case KeyEvent.VK_UP: up = true; break;
            case KeyEvent.VK_DOWN: down = true; break;
        }
    }

    @Override public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: left = false; break;
            case KeyEvent.VK_RIGHT: right = false; break;
            case KeyEvent.VK_UP: up = false; break;
            case KeyEvent.VK_DOWN: down = false; break;
        }
    }

    @Override public void mousePressed(MouseEvent e){
        dragging = true;
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override public void mouseReleased(MouseEvent e){
        dragging = false;
    }

    @Override public void mouseDragged(MouseEvent e){
        if(!dragging) return;
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;
        lastX = e.getX();
        lastY = e.getY();
        dragYaw += dx * 0.01;
        dragPitch += dy * 0.01;
    }

    @Override public void mouseMoved(MouseEvent e){}
    @Override public void mouseClicked(MouseEvent e){}
    @Override public void mouseEntered(MouseEvent e){}
    @Override public void mouseExited(MouseEvent e){}
}
