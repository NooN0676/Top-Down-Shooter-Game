package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class DeathMenu extends JPanel {
    private JLabel waveLabel;
    private JLabel zombiesKilledLabel;
    private JLabel totalScoreLabel;
    GamePanel gp;
    JPanel container;

    public DeathMenu(GamePanel gp, JPanel container) {
        this.gp = gp;
        this.container = container;
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 200));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("GAME OVER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.RED);
        titlePanel.add(titleLabel);

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new GridLayout(3, 1, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        waveLabel = new JLabel();
        waveLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        waveLabel.setForeground(Color.WHITE);

        zombiesKilledLabel = new JLabel();
        zombiesKilledLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        zombiesKilledLabel.setForeground(Color.WHITE);

        totalScoreLabel = new JLabel();
        totalScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        totalScoreLabel.setForeground(Color.WHITE);

        statsPanel.add(waveLabel);
        statsPanel.add(zombiesKilledLabel);
        statsPanel.add(totalScoreLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton mainMenuButton = createMenuButton("Main Menu");
        buttonPanel.add(mainMenuButton);

        add(titlePanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        mainMenuButton.addActionListener((ActionEvent e) -> {
            gp.setGameState(GameState.MAIN_MENU);
            CardLayout cl = (CardLayout) container.getLayout();
            cl.show(container, "Menu");
        });
    }

    public void updateStats(GamePanel gp) {
        waveLabel.setText("Wave Reached: " + gp.zombieManager.getWaveNumber());
        zombiesKilledLabel.setText("Zombies Killed: " + gp.zombieManager.getTotalZombiesKilled());
        totalScoreLabel.setText("Total Score: " + gp.score);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(Color.BLACK.getRGB()));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }
}
