package entities;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import entities.weapons.Gun;

import java.util.ArrayList;
import java.io.IOException;

import main.GamePanel;

public abstract class Zombie extends Entity {
    protected GamePanel gp;
    public int health;
    protected int maxHealth;
    protected double speed;
    protected int damage;
    protected Color color;
    protected boolean isAlive = true;
    protected Rectangle collidableArea = new Rectangle();
    protected String imagePath;
    protected BufferedImage image;
    public boolean collisionOn = true;
    protected String direction;

    public Zombie(GamePanel gp, int worldX, int worldY, String imagePath) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;

        // zombi collision alanı
        collidableArea = new Rectangle(8, 8, gp.tileSize - 16, gp.tileSize - 16);

        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        collisionOn = false;
        gp.colCheck.checkTile(this);

        if (!collisionOn) {
            moveTowardsPlayer();
        } else {
            
            return; // collision varsa hareket etmemeyi uygulamaya çalıştım
        }
    }

    public void draw(Graphics2D g2d) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // oyuncuya bakış açısını hesapla
        double angle = calculateAngleToPlayer();

        // döndür ve çiz
        g2d.rotate(angle, screenX + gp.tileSize / 2, screenY + gp.tileSize / 2);
        if (image != null) {
            g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            g2d.setColor(color);
            g2d.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
        }
        g2d.rotate(-angle, screenX + gp.tileSize / 2, screenY + gp.tileSize / 2);

        drawHealthBar(g2d, screenX, screenY);
    }

    protected double calculateAngleToPlayer() {
        return Math.atan2(gp.player.worldY - worldY, gp.player.worldX - worldX);
    }

    protected void drawHealthBar(Graphics2D g2d, int screenX, int screenY) {
        int barWidth = gp.tileSize;
        int barHeight = 5;
        int barX = screenX;
        int barY = screenY - 10;

        double healthPercentage = (double) health / maxHealth;

        
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        
        g2d.setColor(Color.GREEN);
        g2d.fillRect(barX, barY, (int) (barWidth * healthPercentage), barHeight);

        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public boolean collisionWithPlayer() {
        Rectangle zombieRect = new Rectangle(worldX + collidableArea.x, worldY + collidableArea.y,
                collidableArea.width, collidableArea.height);
        Rectangle playerRect = new Rectangle(gp.player.worldX + gp.player.collidableArea.x,
                gp.player.worldY + gp.player.collidableArea.y,
                gp.player.collidableArea.width, gp.player.collidableArea.height);
        return zombieRect.intersects(playerRect);
    }

    public void dropAmmo() {
        // mermi alabilecek silahları kontrol et
        ArrayList<Gun> possibleWeapons = new ArrayList<>();
        for (Gun weapon : gp.player.weapons) {
            if (weapon.isUnlocked()) {
                possibleWeapons.add(weapon);
            }
        }

        if (!possibleWeapons.isEmpty()) {
            // rastgele bir silah seç
            Gun selectedWeapon = possibleWeapons.get((int) (Math.random() * possibleWeapons.size()));

            
            int amount = selectedWeapon.maxAmmo;

            // mermi ekle
            selectedWeapon.setAmmoReserve(selectedWeapon.getAmmoReserve() + amount);

        }
    }

    public void die() {
        isAlive = false;
        dropAmmo();
       
        
    }

    public boolean isAlive() {
        return isAlive;
    }

    // hareket mantığı
    protected void moveTowardsPlayer() {
        double dx = gp.player.worldX - this.worldX;
        double dy = gp.player.worldY - this.worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            dx /= distance;
            dy /= distance;

            
            double adjustedSpeed = speed * 1.5;

            
            int newWorldX = (int) (worldX + dx * adjustedSpeed);
            int newWorldY = (int) (worldY + dy * adjustedSpeed);

            
            collisionOn = false;
            gp.colCheck.checkTile(this);

            if (!collisionOn) {
                worldX = newWorldX;
                worldY = newWorldY;
            } else {
                
                if (!gp.colCheck.checkCollisionAt(newWorldX, worldY)) {
                    worldX = newWorldX;
                } else if (!gp.colCheck.checkCollisionAt(worldX, newWorldY)) {
                    worldY = newWorldY;
                }
            }
        }
    }
}