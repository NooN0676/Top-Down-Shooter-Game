package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.SwingUtilities;

public class KeyHandler implements KeyListener, MouseListener, Serializable {
    // klavye kontrolleri
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean number1Pressed, number2Pressed, number3Pressed,
            number4Pressed, number5Pressed, reloadPressed;
    public GamePanel gp;

    // mouse kontrolleri
    public boolean shootPressed;
    public boolean rightClickPressed; 

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        
        gp.handleInput(keyCode);

        
        switch (keyCode) {
            case KeyEvent.VK_1:
                number1Pressed = true;
                break;
            case KeyEvent.VK_2:
                number2Pressed = true;
                break;
            case KeyEvent.VK_3:
                number3Pressed = true;
                break;
            case KeyEvent.VK_4:
                number4Pressed = true;
                break;
            case KeyEvent.VK_5:
                number5Pressed = true;
                break;
            case KeyEvent.VK_R:
                reloadPressed = true;
                break;
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_9:
                gp.saveGame(); // Quicksave
                break;
            case KeyEvent.VK_0:
                gp.loadGame(); // Quickload
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_1:
                number1Pressed = false;
                break;
            case KeyEvent.VK_2:
                number2Pressed = false;
                break;
            case KeyEvent.VK_3:
                number3Pressed = false;
                break;
            case KeyEvent.VK_4:
                number4Pressed = false;
                break;
            case KeyEvent.VK_5:
                number5Pressed = false;
                break;
            case KeyEvent.VK_R:
                reloadPressed = false;
                break;
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
        }
    }

    
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            shootPressed = true;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            rightClickPressed = true; 
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            shootPressed = false;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            rightClickPressed = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void resetInputFlags() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        number1Pressed = false;
        number2Pressed = false;
        number3Pressed = false;
        number4Pressed = false;
        number5Pressed = false;
        reloadPressed = false;
        shootPressed = false;
        rightClickPressed = false;
    }
}