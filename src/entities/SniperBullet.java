package entities;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class SniperBullet extends Projectile {
    private BufferedImage image;

    public SniperBullet(GamePanel gp, double startX, double startY,
            double targetX, double targetY, int damage, String projectileImagePath) {
        super(gp, startX, startY, targetX, targetY, damage);
        this.speed = 30;
        this.maxDistance = 1000;
        
        this.size = 5;

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(projectileImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.image = null;
        }
    }

    @Override
    protected void onHit(Zombie zombie) {
        zombie.takeDamage(damage);
        
    }

    @Override
    protected void checkZombieCollisions() {
        for (Zombie zombie : gp.zombieManager.getZombies()) {
            if (zombie.isAlive() && collidesWith(zombie)) {
                onHit(zombie);
                // içinden geçmesi için return yok
            }
        }
    }

    @Override
    protected void drawProjectile(Graphics2D g2d, int screenX, int screenY, double angle) {
        if (image != null) {
            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(angle, screenX, screenY);
            g2dRotated.drawImage(image, screenX - image.getWidth() / 2,
                    screenY - image.getHeight() / 2, null);
            g2dRotated.dispose();
        } else {
            g2d.setColor(color);
            g2d.fillOval(screenX - size / 2, screenY - size / 2, size, size);
        }
    }
}
