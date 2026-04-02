package main;

import java.awt.CardLayout;
import java.io.Serializable;
import javax.swing.*;

public class Game implements Serializable {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Top Down Shooter");

        JPanel container = new JPanel(new CardLayout());

        // paneller
        GamePanel gamePanel = new GamePanel(container);
        MainMenu mainMenu = new MainMenu(gamePanel, container);

        // containere ekle
        container.add(mainMenu, "Menu");
        container.add(gamePanel, "Game");
        window.add(container);

        window.pack(); 
        window.setLocationRelativeTo(null); 
        window.setVisible(true);

        
        CardLayout cl = (CardLayout) (container.getLayout());
        cl.show(container, "Menu");
    }
}
