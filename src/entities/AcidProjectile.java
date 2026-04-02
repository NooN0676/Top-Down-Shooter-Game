package entities;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AcidProjectile extends Projectile {
    private static final int SPLASH_RADIUS = 60; 
    private static final int SPLASH_DAMAGE = 10; 
    private BufferedImage acidImage;
    private double targetX; 
    private double targetY; 
    private double startX; 
    private double startY; 
    public AcidSpitterZombie creator; 

    public AcidProjectile(GamePanel gp, double startX, double startY, double targetX, double targetY, int damage,
            AcidSpitterZombie creator) {
        super(gp, startX, startY, targetX, targetY, damage);
        this.creator = creator; 

        
        try {
            acidImage = ImageIO.read(getClass().getResourceAsStream("/res/projectiles/acid_blob.png"));
            if (acidImage != null) {
                this.size = Math.max(acidImage.getWidth(), acidImage.getHeight());
            }
        } catch (IOException e) {
            e.printStackTrace();
            acidImage = null;
        }

        // aside özel değerler
        this.speed = 5; 
        this.maxDistance = 400; 
        this.size = acidImage != null ? size : 8; 
    }

    @Override
    public void update() {
        super.update();

        
        if (collidesWithPlayer()) {
            onHitPlayer();
            alive = false; 
            return;
        }
    }

    private boolean collidesWithPlayer() {
        double playerCenterX = gp.player.worldX + gp.player.collidableArea.width / 2.0;
        double playerCenterY = gp.player.worldY + gp.player.collidableArea.height / 2.0;
        double distance = Math.sqrt(Math.pow(x - playerCenterX, 2) + Math.pow(y - playerCenterY, 2));
        return distance < (size + gp.player.collidableArea.width / 2.0);
    }

    private void onHitPlayer() {
        gp.player.takeDamage(damage); 
    }

    @Override
    protected void onHit(Zombie zombie) {
        if (zombie != creator) { 
            super.onHit(zombie);
            splashDamage();
        }
    }

    @Override
    protected void onWallHit() {
        splashDamage();
    }

    
    protected void splashDamage() {
        for (Zombie zombie : gp.zombieManager.getZombies()) {
            if (zombie.isAlive() && zombie != creator) { 
                double distance = Math.sqrt(Math.pow(zombie.worldX - x, 2) +
                        Math.pow(zombie.worldY - y, 2));

                if (distance < SPLASH_RADIUS) {
                    int scaledDamage = (int) (SPLASH_DAMAGE * (1 - (distance / SPLASH_RADIUS)));
                    zombie.takeDamage(scaledDamage);
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        int screenX = (int) (x - gp.player.worldX + gp.player.screenX);
        int screenY = (int) (y - gp.player.worldY + gp.player.screenY);

        if (isOnScreen(screenX, screenY)) {
            double angle = Math.atan2(targetY - startY, targetX - startX);
            drawProjectile(g2d, screenX, screenY, angle);
        }
    }

    @Override //playere bakarak döndürme
    protected void drawProjectile(Graphics2D g2d, int screenX, int screenY, double angle) {
        if (acidImage != null) {
            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(angle, screenX, screenY);
            g2dRotated.drawImage(acidImage, screenX - acidImage.getWidth() / 2,
                    screenY - acidImage.getHeight() / 2, null);
            g2dRotated.dispose();
        } else {
            g2d.setColor(color);
            g2d.fillOval(screenX - size / 2, screenY - size / 2, size, size);
        }
    }

    private boolean isOnScreen(int screenX, int screenY) {
        return screenX > -size * 2 && screenX < gp.scrWidth + size * 2 &&
                screenY > -size * 2 && screenY < gp.scrHeight + size * 2;
    }
}