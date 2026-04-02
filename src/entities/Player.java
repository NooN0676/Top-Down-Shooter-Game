package entities;

import main.KeyHandler;
import main.GamePanel;
import java.awt.*;
import java.awt.geom.AffineTransform;

import java.io.IOException;

import javax.imageio.ImageIO;
import entities.weapons.*;

public class Player extends Entity {
    
    private GamePanel gp;
    private KeyHandler kh;

    // Ekran ortası
    public final int screenX;
    public final int screenY;
    private boolean movingUp, movingDown, movingLeft, movingRight;

    private final int WEAPON_OFFSET_X = 16; // silah çizimi için
    private final int WEAPON_OFFSET_Y = 8; 

    // can sistemi
    private int maxHealth = 100;
    public int currentHealth;
    private boolean isInvulnerable = false;
    private int invulnerabilityTimer = 0;
    private final int INVULNERABILITY_DURATION = 60; // hasar yedikten sonra bir daha hasar alınamayacak süre (1s)

    // silah sistemi
    public Gun[] weapons;
    public int currentWeaponIndex = 0;
    private boolean isReloading = false;
    private long reloadStartTime = 0;

    // can barı boyutu
    private final int HEALTH_BAR_WIDTH = 100;
    private final int HEALTH_BAR_HEIGHT = 10;
     final int HEALTH_BAR_X = 20;
     final int HEALTH_BAR_Y = 20;
    private double angle;

    public Player(GamePanel gp, KeyHandler kh) {
        this.gp = gp;
        this.kh = kh;

        
        screenX = gp.scrWidth / 2 - (gp.tileSize / 2);
        screenY = gp.scrHeight / 2 - (gp.tileSize / 2);

        // collision kutusu
        collidableArea = new Rectangle(12, 9, 20, 20);

        
        currentHealth = maxHealth;

        
        weapons = new Gun[] {
                new Pistol(gp), // 0 - Pistol
                new Rifle(gp), // 1 - Rifle
                new Shotgun(gp), // 2 - Shotgun
                new SniperRifle(gp), // 3 - Sniper
                new RocketLauncher(gp) // 4 - Rocket Launcher
        };

        // Ssadece pistol açık en başta
        weapons[0].unlock(); 
        for (int i = 1; i < weapons.length; i++) {
            weapons[i].lock(); 
        }

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 25;
        worldY = gp.tileSize * 24;
        speed = 4;
        direction = "right";
        movingUp = movingDown = movingLeft = movingRight = false;
    }

    public void getPlayerImage() {
        try {
            standing = ImageIO.read(getClass().getResourceAsStream("/res/player/p_standing.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void takeDamage(int damage) {
        if (isInvulnerable)
            return;

        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            die();
        }

        isInvulnerable = true;
        invulnerabilityTimer = INVULNERABILITY_DURATION;
    }

    

    public void die() {
        if (!isAlive())
            return; // ölü

        currentHealth = 0;
        gp.gameOver();
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void update() {
        if (!isAlive()) {
            gp.gameOver(); 
            return;
        }
        checkWeaponUnlocks();
        handleWeaponInput();
        handleMovement();
        updateInvulnerability();
        updateReloading();

        getCurrentWeapon().update();

        updateFacingAngle();
    }

    private void updateFacingAngle() {
        // ekran ortasından mouse tıklanan yere olan açı
        int playerCenterX = screenX + gp.tileSize / 2;
        int playerCenterY = screenY + gp.tileSize / 2;
        this.angle = Math.atan2(gp.mouseY - playerCenterY, gp.mouseX - playerCenterX);
    }

    private void checkWeaponUnlocks() {
        int wave = gp.zombieManager.getWaveNumber();

        if (wave > 1 && !weapons[1].isUnlocked()) {
            unlockWeapon(1); // wave 1'de Rifle açılır
        }
        if (wave > 3 && !weapons[2].isUnlocked()) {
            unlockWeapon(2); // wave 3'te Shotgun açılır
        }
        if (wave > 5 && !weapons[3].isUnlocked()) {
            unlockWeapon(3); // wave 5'te Sniper açılır
        }
        if (wave > 10 && !weapons[4].isUnlocked()) {
            unlockWeapon(4); // wave 10'da Rocket Launcher açılır
        }
    }

    private void unlockWeapon(int weaponIndex) {
        weapons[weaponIndex].unlock();
        System.out.println("Weapon unlocked: " + weapons[weaponIndex].getName());
    }

    private void handleWeaponInput() {
        // silah değiştirme
        if (kh.number1Pressed)
            switchWeapon(0); 
        if (kh.number2Pressed && weapons[1].isUnlocked())
            switchWeapon(1);
        if (kh.number3Pressed && weapons[2].isUnlocked())
            switchWeapon(2);
        if (kh.number4Pressed && weapons[3].isUnlocked())
            switchWeapon(3);
        if (kh.number5Pressed && weapons[4].isUnlocked())
            switchWeapon(4);

        
        if (kh.shootPressed) {
            shoot();
        }

        
        if (kh.reloadPressed) {
            reload();
        }
    }

    public void switchWeapon(int weaponIndex) {
        if (weaponIndex >= 0 && weaponIndex < weapons.length && weapons[weaponIndex].isUnlocked()) {
            currentWeaponIndex = weaponIndex;
        }
    }

    private void shoot() {
        Gun currentWeapon = getCurrentWeapon();
        if (currentWeapon.canShoot()) {
            double targetX = gp.mouseX + worldX - screenX;
            double targetY = gp.mouseY + worldY - screenY;
            currentWeapon.shoot(targetX, targetY);
            
        } else {
            
        }
    }

    private void reload() {
        if (!isReloading && getCurrentWeapon().canReload()) {
            isReloading = true;
            reloadStartTime = System.currentTimeMillis();
            
        }
    }

    private void updateReloading() {
        if (isReloading && System.currentTimeMillis() - reloadStartTime > getCurrentWeapon().reloadTime) {
            isReloading = false;
            getCurrentWeapon().finishReload();
        }
    }

    private void handleMovement() {
        movingUp = kh.upPressed;
        movingDown = kh.downPressed;
        movingLeft = kh.leftPressed;
        movingRight = kh.rightPressed;

        if (movingUp || movingDown || movingLeft || movingRight) {
            if (movingUp)
                direction = "up";
            if (movingDown)
                direction = "down";
            if (movingLeft)
                direction = "left";
            if (movingRight)
                direction = "right";
        }

        collisionOn = false;
        gp.colCheck.checkTile(this);

        if (!collisionOn) {
            if (movingUp)
                worldY -= speed;
            if (movingDown)
                worldY += speed;
            if (movingLeft)
                worldX -= speed;
            if (movingRight)
                worldX += speed;
        }
    }

    private void updateInvulnerability() {
        if (isInvulnerable) {
            invulnerabilityTimer--;
            if (invulnerabilityTimer <= 0) {
                isInvulnerable = false;
            }
        }
    }

    public Gun getCurrentWeapon() {
        return weapons[currentWeaponIndex];
    }

    public void draw(Graphics2D g2d) {
        drawWeapon(g2d);
        drawPlayer(g2d);
    }

    private void drawPlayer(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();

        // oyuncuyu döndür
        g2d.translate(screenX + gp.tileSize / 2, screenY + gp.tileSize / 2);
        g2d.rotate(angle);
        g2d.translate(-gp.tileSize / 2, -gp.tileSize / 2);

        if (isInvulnerable && (invulnerabilityTimer / 5) % 2 == 0) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2d.setColor(Color.RED);
            g2d.fillRect(0, 0, gp.tileSize, gp.tileSize);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        g2d.drawImage(standing, 0, 0, gp.tileSize, gp.tileSize, null);
        g2d.setTransform(oldTransform);
    }

    private void drawWeapon(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();

        
        int playerCenterX = screenX + gp.tileSize / 2;
        int playerCenterY = screenY + gp.tileSize / 2;

       
        int weaponDrawX = playerCenterX + WEAPON_OFFSET_X;
        int weaponDrawY = playerCenterY - WEAPON_OFFSET_Y;

        
        g2d.translate(playerCenterX, playerCenterY);
        g2d.rotate(angle);
        g2d.translate(-playerCenterX, -playerCenterY);

        
        getCurrentWeapon().draw(g2d, weaponDrawX, weaponDrawY);

        g2d.setTransform(oldTransform);

       
        if (isReloading) {
            float reloadProgress = (float) (System.currentTimeMillis() - reloadStartTime)
                    / getCurrentWeapon().reloadTime;
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(screenX, screenY - 15, (int) (gp.tileSize * reloadProgress), 5);
        }
    }

    public void drawHealthBar(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x, y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        float healthPercentage = (float) currentHealth / maxHealth;
        Color healthColor = healthPercentage > 0.6f ? Color.GREEN : healthPercentage > 0.3f ? Color.YELLOW : Color.RED;

        g2d.setColor(healthColor);
        g2d.fillRect(x, y, (int) (HEALTH_BAR_WIDTH * healthPercentage), HEALTH_BAR_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    public void addScore(Zombie zombie) {
        //puan ekle
        if (zombie instanceof NormalZombie) {
            gp.score += 10; 
        } else if (zombie instanceof CrawlerZombie) {
            gp.score += 15; 
        } else if (zombie instanceof TankZombie) {
            gp.score += 20; 
        } else if (zombie instanceof AcidSpitterZombie) {
           gp.score += 30; 
        }
    }
}