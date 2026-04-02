package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.Component;

import javax.swing.JPanel;

import entities.Player;
import entities.ProjectileManager;
import entities.Zombie;
import entities.ZombieManager;
import tile.TileOperations;

public class GamePanel extends JPanel implements Runnable, MouseMotionListener {
    final int defaultTileSize = 32; // default character 32x32 pixel olcak
    final int scale = 2;

    public final int tileSize = defaultTileSize * scale;
    public final int maxScreenColumns = 16;
    public final int maxScreenRows = 12; // 4:3 yani
    public final int scrWidth = maxScreenColumns * tileSize; // 1024 px
    public final int scrHeight = maxScreenRows * tileSize; // 768 px

    GameState gameState = GameState.MAIN_MENU;

    public int mouseX;
    public int mouseY;

    // map
    public final int maxMapCol = 50;
    public final int maxMapRow = 50;
    public final int mapWidth = tileSize * maxMapCol;
    public final int mapHeight = tileSize * maxMapRow;
    public File saveFile = new File("/src/res/saveFile.dat");

    KeyHandler kH = new KeyHandler(this);

    public CollisionCheck colCheck = new CollisionCheck(this);
    int FPS = 60;
    public Player player = new Player(this, kH);
    public TileOperations to = new TileOperations(this);

    Thread gameThread;
    public ProjectileManager projectileManager;
    public ZombieManager zombieManager;
    boolean gameRunning = true;
    public int score = 0;
    int waveNumber = 0;

    private JPanel container;

    public GamePanel(JPanel container) {
        this.container = container;
        this.setPreferredSize(new Dimension(scrWidth, scrHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // render optimizasyonu için
        this.setLayout(null);

        addMouseMotionListener(this);
        this.addKeyListener(kH);
        this.addMouseListener(kH);
        this.setFocusable(true);
        zombieManager = new ZombieManager(this);
        projectileManager = new ProjectileManager(this);
        zombieManager.startNewWave();

        DeathMenu deathMenu = new DeathMenu(this, container);
        container.add(deathMenu, "DeathMenu");

        PauseMenu pauseMenu = new PauseMenu(this, container);
        container.add(pauseMenu, "PauseMenu");

        startGameThread();
    }

    public void startGameThread() {
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void setGameState(GameState state) {
        this.gameState = state;

        CardLayout cl = (CardLayout) container.getLayout();
        if (state == GameState.GAME_OVER) {
            
            for (Component component : container.getComponents()) {
                if (component instanceof DeathMenu) {
                    DeathMenu deathMenu = (DeathMenu) component;
                    deathMenu.updateStats(this); // ölüm menüsünü güncelle
                    break;
                }
            }
            cl.show(container, "DeathMenu");
        } else if (state == GameState.MAIN_MENU) {
            cl.show(container, "Menu");
        } else if (state == GameState.PLAYING) {
            cl.show(container, "Game");
        } else if (state == GameState.PAUSED) {
            cl.show(container, "PauseMenu");
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // saniye bölü fps (0.1666 saniyede bir drawlıcak)
        double nextDrawTime = drawInterval + System.nanoTime();

        while (gameThread != null && gameRunning) {
            

            update();

            repaint();

            try {
                double remainingDrawTime = nextDrawTime - System.nanoTime();
                remainingDrawTime = remainingDrawTime / 1000000;
                if (remainingDrawTime < 0) {
                    remainingDrawTime = 0;
                }
                Thread.sleep((long) remainingDrawTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

    }

    public void update() {
        if (gameRunning && gameState == GameState.PLAYING) { 
            player.update();
            zombieManager.update();
            projectileManager.update();

            // collision mantığı
            for (Zombie zombie : zombieManager.getZombies()) {
                if (zombie.collisionOn) {
                    zombie.stopMovement();
                }
            }

            if (zombieManager.isWaveComplete()) {
                zombieManager.startNewWave();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // double buffer
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, scrWidth, scrHeight);

        if (gameState == GameState.PLAYING) {

            to.draw(g2d);
            zombieManager.draw(g2d);
            projectileManager.draw(g2d);
            player.draw(g2d);
        }

        drawUI(g2d);
    }

    private void drawUI(Graphics2D g2d) {
        // daha yumuşak bir yazı için
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);

        // silah ve mermi bilgisi
        String weaponName = player.weapons[player.currentWeaponIndex].getName();
        String ammoText = player.weapons[player.currentWeaponIndex].getCurrentAmmo() + "/" +
                player.weapons[player.currentWeaponIndex].getAmmoReserve();
        g2d.drawString(weaponName, 20, 30);
        g2d.drawString(ammoText, 20, 50);

        // can barı
        drawPlayerHealthBar(g2d, scrWidth - 140, 20);

        // skor
        String scoreText = "Score: " + score;
        g2d.drawString(scoreText, scrWidth - 140, 60);

        // dalga sayısı ve dalga numarası
        String waveText = "Wave: " + zombieManager.getWaveNumber();
        String zombiesText = "Zombies: " + zombieManager.getAliveZombies();
        g2d.drawString(waveText, 20, scrHeight - 40);
        g2d.drawString(zombiesText, 20, scrHeight - 20);
    }

    private void drawPlayerHealthBar(Graphics2D g2d, int x, int y) {
        int barWidth = 100;
        int barHeight = 10;

        g2d.setColor(Color.GRAY);
        g2d.fillRect(x, y, barWidth, barHeight);

        float healthPercentage = (float) player.getCurrentHealth() / player.getMaxHealth();
        Color healthColor = healthPercentage > 0.6f ? Color.GREEN : healthPercentage > 0.3f ? Color.YELLOW : Color.RED;

        g2d.setColor(healthColor);
        g2d.fillRect(x, y, (int) (barWidth * healthPercentage), barHeight);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, barWidth, barHeight);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

    }

    public void addScore(int i) {
        score += i;
    }

    public void gameOver() {
        setGameState(GameState.GAME_OVER);
        gameRunning = false;
    }

    public void startNewGame() {

        player = new Player(this, kH);
        zombieManager = new ZombieManager(this);
        projectileManager = new ProjectileManager(this);
        score = 0;
        waveNumber = 0;

        // sadece pistol başta
        player.weapons[0].unlock();
        player.weapons[0].setAmmoReserve(9999);
        player.currentWeaponIndex = 0;
        for (int i = 1; i < player.weapons.length; i++) {
            player.weapons[i].lock(); // diğer silahları kilitle
        }

        zombieManager.startNewWave();
        gameRunning = true;
        setGameState(GameState.PLAYING);
        requestFocusInWindow();

        startGameThread();
    }

    public void saveGame() {
        try {
            File saveFile = new File(System.getProperty("user.dir") + "/saveFile.dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                GameData data = new GameData(player, zombieManager, projectileManager, this);
                oos.writeObject(data);
                System.out.println("Game quicksaved successfully!");
            }
        } catch (IOException e) {
            System.err.println("Failed to quicksave game: " + e.getMessage());
        }
    }

    public boolean loadGame() {
        try {
            File saveFile = new File(System.getProperty("user.dir") + "/saveFile.dat");
            if (!saveFile.exists()) {
                System.out.println("No quicksave file found");
                return false;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
                GameData data = (GameData) ois.readObject();
                // oyuncu durumunu geri yükle
                player = new Player(this, kH);
                player.setDefaultValues();
                player.worldX = data.playerX;
                player.worldY = data.playerY;
                player.currentHealth = data.playerHealth;
                player.currentWeaponIndex = data.currentWeaponIndex;
                for (int i = 0; i < player.weapons.length; i++) {
                    player.weapons[i].setAmmoReserve(data.weaponReserve[i]);
                    player.weapons[i].currentAmmo = data.weaponAmmo[i];
                    if (data.weaponsUnlocked[i]) {
                        player.weapons[i].unlock();
                    }
                }
                // oyun durumunu geri yükle
                score = data.score;
                zombieManager.waveNumber = data.waveNumber;

                zombieManager.setTotalZombiesKilled(data.totalZombiesKilled);

                // zombie durumunu geri yükle
                zombieManager.clearZombies();
                for (ZombieData zombieData : data.zombies) {
                    Zombie zombie = zombieData.createZombie(this);
                    zombieManager.addZombie(zombie);
                }

                // projectile durumunu geri yükle
                projectileManager.clear();
                for (ProjectileData projectileData : data.projectiles) {
                    projectileManager.addProjectile(projectileData.createProjectile(this));
                }

                System.out.println("Game quickloaded successfully!");

                kH.resetInputFlags();

                // inputlar bozulmasın diye tekrar ekliyoruz
                this.removeKeyListener(kH);
                this.addKeyListener(kH);

                // threadi başlat
                gameRunning = true;
                setGameState(GameState.PLAYING);
                startGameThread();

                return true;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to quickload game: " + e.getMessage());
            return false;
        }
    }

    public void handleInput(int keyCode) {
        if (gameState == GameState.PLAYING && keyCode == KeyEvent.VK_ESCAPE) {
            setGameState(GameState.PAUSED);
            CardLayout cl = (CardLayout) container.getLayout();
            cl.show(container, "PauseMenu");
        } else if (gameState == GameState.PAUSED && keyCode == KeyEvent.VK_ESCAPE) {
            setGameState(GameState.PLAYING);
            requestFocusInWindow();
        }
    }
}
