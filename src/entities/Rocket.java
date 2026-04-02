package entities;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class Rocket extends Projectile {
    private BufferedImage image;

    public Rocket(GamePanel gp, double startX, double startY,
            double targetX, double targetY, int damage, String projectileImagePath) {
        super(gp, startX, startY, targetX, targetY, damage);
        this.speed = 8;
        this.maxDistance = 700;
        
        this.size = 8;

        // Load the image
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(projectileImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.image = null;
        }
    }

    @Override
    protected void onHit(Zombie zombie) {
        createExplosionEffect(); // patlama efekti oluştur
        for (Zombie z : gp.zombieManager.getZombies()) {
            if (z.isAlive()) {
                double distance = Math.sqrt(Math.pow(z.worldX - x, 2) +
                        Math.pow(z.worldY - y, 2));
                if (distance < 100) { // patlama yarıçapı
                    z.takeDamage((int) (damage * (1 - distance / 100))); //patlamaya uzaklığa göre hasar ver
                }
            }
        }
    }

    @Override
    protected void onWallHit() {
        createExplosionEffect(); 
        onHit(null); 
    }

    private void createExplosionEffect() {
        gp.projectileManager.addProjectile(new ExplosionEffect(gp, x, y));
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
