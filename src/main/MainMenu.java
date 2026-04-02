package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JPanel {
    private GamePanel gp;
    private JPanel container;

    public MainMenu(GamePanel gp, JPanel container) {
        this.gp = gp;
        this.container = container;
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 200)); 

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Top Down Shooter");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton startButton = createMenuButton("Start New Game");
        JButton loadButton = createMenuButton("Load Game");
        JButton exitButton = createMenuButton("Exit");

        buttonPanel.add(startButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);

       
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // listener ekle
        startButton.addActionListener(e -> {
            gp.startNewGame();
            switchToGame();
        });

        loadButton.addActionListener(e -> {
            if (gp.loadGame()) {
                gp.setGameState(GameState.PLAYING); 
                gp.gameRunning = true; 
                switchToGame(); 
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load game", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 90, 70));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void switchToGame() {
        CardLayout cl = (CardLayout) container.getLayout();
        cl.show(container, "Game");
        gp.requestFocusInWindow(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(new Color(0, 0, 123, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void handleInput(int keyCode) {
        if (!isEnabled()) {
            return; 
        }

        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                gp.setGameState(GameState.PLAYING);
                gp.startNewGame();
                break;
            case KeyEvent.VK_L:
                if (gp.loadGame()) {
                    gp.setGameState(GameState.PLAYING);
                    gp.gameRunning = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to load game", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
    }
}