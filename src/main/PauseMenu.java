package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
//durdurma menüsü
public class PauseMenu extends JPanel {
     GamePanel gp;
     JPanel container;

    public PauseMenu(GamePanel gp, JPanel container) {
        this.gp = gp;
        this.container = container;
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 200)); 

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("PAUSED");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton continueButton = createMenuButton("Continue");
        JButton saveButton = createMenuButton("Save Game");
        JButton loadButton = createMenuButton("Load Game");
        JButton exitButton = createMenuButton("Exit to Main Menu");

        buttonPanel.add(continueButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);

        
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        
        continueButton.addActionListener((ActionEvent e) -> {
            gp.setGameState(GameState.PLAYING);
            gp.requestFocusInWindow();
        });

        saveButton.addActionListener((ActionEvent e) -> {
            gp.saveGame();
            JOptionPane.showMessageDialog(this, "Game saved successfully!", "Save Game",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        loadButton.addActionListener((ActionEvent e) -> {
            if (gp.loadGame()) {
                gp.setGameState(GameState.PLAYING);
                gp.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load game", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener((ActionEvent e) -> {
            gp.setGameState(GameState.MAIN_MENU);
            CardLayout cl = (CardLayout) container.getLayout();
            cl.show(container, "Menu");
        });
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
}
